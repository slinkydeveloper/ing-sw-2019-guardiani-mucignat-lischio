package com.adrenalinici.adrenaline.gui;

import com.adrenalinici.adrenaline.model.common.DashboardChoice;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.PlayersChoice;
import com.adrenalinici.adrenaline.model.common.RulesChoice;
import com.adrenalinici.adrenaline.network.client.ClientViewProxy;
import com.adrenalinici.adrenaline.network.inbox.ChosenMatchMessage;
import com.adrenalinici.adrenaline.network.inbox.InboxMessage;
import com.adrenalinici.adrenaline.network.inbox.NewMatchMessage;
import com.adrenalinici.adrenaline.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.util.ObservableImpl;
import com.adrenalinici.adrenaline.util.TriConsumer;
import javafx.application.Platform;

import java.util.*;

public class ViewEventBus extends ObservableImpl<InboxMessage> implements ClientViewProxy  {

  private Map<String, TriConsumer<OutboxMessage, ViewEventBus, String>> eventHandlers;
  private boolean isListening;
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
      if (isListening)
        new HashSet<>(eventHandlers.entrySet()).forEach(e -> e.getValue().accept(message, this, e.getKey()));
      else {
        messageQueue.offer(message);
      }
    });
  }

  public void replayMessage(OutboxMessage message) {
    if (isListening) throw new IllegalStateException("You are trying to replay a message in an active event bus");
    messageQueue.addFirst(message);
  }

  public void stop() {
    isListening = false;
  }

  public void start() {
    isListening = true;
    while (!messageQueue.isEmpty())
      handleNewServerMessage(messageQueue.poll());
  }
}
