package com.adrenalinici.adrenaline.network.server;

import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.network.inbox.InboxEntry;
import com.adrenalinici.adrenaline.network.outbox.ChooseMyPlayerMessage;
import com.adrenalinici.adrenaline.network.outbox.ModelEventMessage;
import com.adrenalinici.adrenaline.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.util.DecoratedEvent;
import com.adrenalinici.adrenaline.util.LogUtils;
import com.adrenalinici.adrenaline.util.Observable;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.NewTurnEvent;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public abstract class BaseGameViewServer extends Observable<DecoratedEvent<ViewEvent, GameView>> implements GameView, Runnable {

  private static final Logger LOG = LogUtils.getLogger(BaseGameViewServer.class);

  private final BlockingQueue<InboxEntry> inbox;
  private final BlockingQueue<OutboxMessage> outbox;
  private final Map<String, PlayerColor> connectedPlayers;
  private final Set<PlayerColor> availablePlayers;
  private boolean matchStarted;

  private OutboxMessage lastSentCommand;

  public BaseGameViewServer(BlockingQueue<InboxEntry> inbox, BlockingQueue<OutboxMessage> outbox, Set<PlayerColor> availablePlayers) {
    this.inbox = inbox;
    this.outbox = outbox;
    this.connectedPlayers = new HashMap<>();
    this.availablePlayers = availablePlayers;
    this.matchStarted = false;
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        InboxEntry e = inbox.take();
        LOG.fine(String.format("Received new message from %s: %s", e.getConnectionId(), e.getMessage().getClass().getName()));
        e.getMessage().onConnectedPlayerMessage(connectedPlayerInboxEntry -> {
          outbox.offer(new ChooseMyPlayerMessage(new ArrayList<>(availablePlayers))); // No cache for this one!
        });
        e.getMessage().onChosenMyPlayerColorMessage(chosenPlayerColorInboxEntry -> {
          if (availablePlayers.contains(chosenPlayerColorInboxEntry.getColor())) {
            availablePlayers.remove(chosenPlayerColorInboxEntry.getColor());
            connectedPlayers.put(e.getConnectionId(), chosenPlayerColorInboxEntry.getColor());
            if (!this.matchStarted)
              checkStartMatch();
            else
              checkResumeMatch();
          } else {
            broadcast(new ChooseMyPlayerMessage(new ArrayList<>(availablePlayers)));
          }
        });
        e.getMessage().onDisconnectedPlayerMessage(disconnectedPlayerInboxEntry -> {
          if (connectedPlayers.containsKey(e.getConnectionId()))
            availablePlayers.add(connectedPlayers.remove(e.getConnectionId()));
        });
        e.getMessage().onViewEventMessage(viewEventInboxEntry -> {
          if (!availablePlayers.isEmpty() || !this.matchStarted) { // Someone is disconnected or the match is not yet started
            // > Drop message or enqueue? Notify client that match is paused?
            // Noop for now
          } else {
            notifyEvent(new DecoratedEvent<>(viewEventInboxEntry.getViewEvent(), this));
          }
        });
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }
  }

  private void checkStartMatch() {
    if (this.availablePlayers.isEmpty()) {
      this.matchStarted = true;
      notifyEvent(new DecoratedEvent<>(new NewTurnEvent(), this));
    }
  }

  private void checkResumeMatch() {
    if (this.availablePlayers.isEmpty())
      broadcast(lastSentCommand);
  }

  void broadcast(OutboxMessage en) {
    this.lastSentCommand = en;
    outbox.offer(en);
  }

  @Override
  public void onEvent(ModelEvent newValue) {
    broadcast(new ModelEventMessage(newValue));
  }

  protected Map<String, PlayerColor> getConnectedPlayers() {
    return connectedPlayers;
  }

  protected Set<PlayerColor> getAvailablePlayers() {
    return availablePlayers;
  }
}
