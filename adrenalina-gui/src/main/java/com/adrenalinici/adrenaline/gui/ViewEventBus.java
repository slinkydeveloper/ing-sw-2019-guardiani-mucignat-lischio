package com.adrenalinici.adrenaline.gui;

import com.adrenalinici.adrenaline.client.ClientViewProxy;
import com.adrenalinici.adrenaline.common.model.DashboardChoice;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.PlayersChoice;
import com.adrenalinici.adrenaline.common.model.RulesChoice;
import com.adrenalinici.adrenaline.common.network.inbox.ChosenMatchMessage;
import com.adrenalinici.adrenaline.common.network.inbox.InboxMessage;
import com.adrenalinici.adrenaline.common.network.inbox.NewMatchMessage;
import com.adrenalinici.adrenaline.common.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.common.util.ObservableImpl;
import com.adrenalinici.adrenaline.common.util.TriConsumer;
import javafx.application.Platform;

import java.util.*;
import java.util.function.Predicate;

public class ViewEventBus extends ObservableImpl<InboxMessage> implements ClientViewProxy {

  private Map<String, TriConsumer<OutboxMessage, ViewEventBus, String>> eventHandlers;
  private boolean isListening;
  private Predicate<OutboxMessage> enqueueFilter;
  private LinkedList<OutboxMessage> messageQueue;

  private String matchId;
  private PlayerColor myPlayer;
  private PlayerColor turnOfPlayer;

  public ViewEventBus() {
    this.isListening = false;
    this.eventHandlers = new HashMap<>();
    this.messageQueue = new LinkedList<>();
  }

  public String registerEventHandler(TriConsumer<OutboxMessage, ViewEventBus, String> consumer) {
    String id = UUID.randomUUID().toString();
    eventHandlers.put(id, consumer);
    return id;
  }

  public void unregisterEventHandler(String id) {
    eventHandlers.remove(id);
  }

  public void sendChosenMatch(String matchId, PlayerColor color) {
    this.matchId = matchId;
    this.myPlayer = color;
    notifyEvent(new ChosenMatchMessage(matchId, color));
  }

  public void sendStartNewMatch(String matchId, DashboardChoice dashboard, PlayersChoice players, RulesChoice rules) {
    notifyEvent(new NewMatchMessage(dashboard, players, rules, matchId));
  }

  @Override
  public void handleNewServerMessage(OutboxMessage message) {
    Platform.runLater(() -> {
      message.onNextTurnMessage(nextTurnMessage -> {
        this.turnOfPlayer = nextTurnMessage.getPlayer();
      });
      if (!isListening || (enqueueFilter != null && enqueueFilter.test(message))) {
        messageQueue.offer(message);
      }
      else {
        new HashSet<>(eventHandlers.entrySet()).forEach(e -> e.getValue().accept(message, this, e.getKey()));
      }
    });
  }

  public void replayMessage(OutboxMessage message) {
    if (isListening) throw new IllegalStateException("You are trying to replay a message in an active event bus");
    messageQueue.addFirst(message);
  }

  public void stop() {
    isListening = false;
    this.enqueueFilter = null;
  }

  public void setEnqueueFilter(Predicate<OutboxMessage> enqueueFilter) {
    this.enqueueFilter = enqueueFilter;
  }

  public void removeEnqueueFilter() {
    this.enqueueFilter = null;
    start();
  }

  public void start() {
    isListening = true;
    while (!messageQueue.isEmpty())
      handleNewServerMessage(messageQueue.poll());
  }

  public PlayerColor getMyPlayer() {
    return myPlayer;
  }

  public PlayerColor getTurnOfPlayer() {
    return turnOfPlayer;
  }
}
