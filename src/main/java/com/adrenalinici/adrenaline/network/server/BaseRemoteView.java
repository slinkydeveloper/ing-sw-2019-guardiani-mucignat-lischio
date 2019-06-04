package com.adrenalinici.adrenaline.network.server;

import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.network.inbox.ViewEventMessage;
import com.adrenalinici.adrenaline.network.outbox.ModelEventMessage;
import com.adrenalinici.adrenaline.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.util.DecoratedEvent;
import com.adrenalinici.adrenaline.util.LogUtils;
import com.adrenalinici.adrenaline.util.Observable;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ExpiredTurnEvent;
import com.adrenalinici.adrenaline.view.event.StartMatchEvent;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.*;
import java.util.logging.Logger;

public abstract class BaseRemoteView extends Observable<DecoratedEvent<ViewEvent, GameView>> implements GameView {

  private static final Logger LOG = LogUtils.getLogger(BaseRemoteView.class);

  private final String matchId;
  private final ServerContext context;
  private final Map<String, PlayerColor> connectedPlayers;
  private final Set<PlayerColor> availablePlayers;
  private final long turnTimerSeconds;

  private boolean matchStarted;
  private OutboxMessage lastSentCommand;
  private Timer actualTimer;

  public BaseRemoteView(String matchId, ServerContext context, Set<PlayerColor> availablePlayers, long turnTimerSeconds) {
    this.matchId = matchId;
    this.context = context;
    this.turnTimerSeconds = turnTimerSeconds;
    this.connectedPlayers = new HashMap<>();
    this.availablePlayers = availablePlayers;
    this.matchStarted = false;
  }

  public Map<String, PlayerColor> getConnectedPlayers() {
    return connectedPlayers;
  }

  public Set<PlayerColor> getAvailablePlayers() {
    return availablePlayers;
  }

  public boolean isMatchStarted() {
    return matchStarted;
  }

  public String getMatchId() {
    return matchId;
  }

  @Override
  public void onEvent(ModelEvent newValue) {
    broadcast(new ModelEventMessage(newValue));
  }

  public void notifyDisconnectedPlayer(String connectionId) {
    if (connectedPlayers.containsKey(connectionId)) {
      LOG.info(String.format("Disconnected %s (connection id %s)", connectedPlayers.get(connectionId), connectionId));
      availablePlayers.add(connectedPlayers.remove(connectionId));
    }
  }

  public boolean notifyNewPlayer(String connectionId, PlayerColor color) {
    if (availablePlayers.contains(color)) {
      availablePlayers.remove(color);
      connectedPlayers.put(connectionId, color);
      return true;
    } else {
      return false;
    }
  }

  public void checkMatchStatus() {
    if (!this.matchStarted)
      checkStartMatch();
    else
      checkResumeMatch();
  }

  public void notifyViewEvent(ViewEventMessage message) {
    if (!availablePlayers.isEmpty() || !this.matchStarted) { // Someone is disconnected or the match is not yet started
      // > Drop message or enqueue? Notify client that match is paused?
      // Noop for now
    } else {
      notifyEvent(new DecoratedEvent<>(message.getViewEvent(), this));
    }
  }

  void broadcast(OutboxMessage en) {
    lastSentCommand = en;
    context.broadcastToMatch(matchId, en);
  }

  void onNewTurn() {
    if (actualTimer != null)
      actualTimer.cancel();
    actualTimer = new Timer();
    actualTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        context.enqueueInboxMessage(matchId, new ViewEventMessage(new ExpiredTurnEvent()));
      }
    }, turnTimerSeconds * 1000);
  }

  private void checkStartMatch() {
    if (this.availablePlayers.isEmpty()) {
      this.matchStarted = true;
      LOG.info("Starting match");
      notifyEvent(new DecoratedEvent<>(new StartMatchEvent(), this));
    }
  }

  private void checkResumeMatch() {
    if (this.availablePlayers.isEmpty())
      broadcast(lastSentCommand);
  }

}
