package com.adrenalinici.adrenaline.server.network;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.event.ModelEvent;
import com.adrenalinici.adrenaline.common.network.inbox.ViewEventMessage;
import com.adrenalinici.adrenaline.common.network.outbox.ModelEventMessage;
import com.adrenalinici.adrenaline.common.network.outbox.NextTurnMessage;
import com.adrenalinici.adrenaline.common.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.common.util.DecoratedEvent;
import com.adrenalinici.adrenaline.common.util.LogUtils;
import com.adrenalinici.adrenaline.common.util.ObservableImpl;
import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.StartMatchEvent;
import com.adrenalinici.adrenaline.common.view.UnavailablePlayerEvent;
import com.adrenalinici.adrenaline.common.view.ViewEvent;

import java.util.*;
import java.util.logging.Logger;

public abstract class BaseRemoteView extends ObservableImpl<DecoratedEvent<ViewEvent, GameView>> implements GameView {

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

  public PlayerColor resolvePlayerColor(String connectionId) {
    return connectedPlayers.get(connectionId);
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

  public void disconnectedPlayer(String connectionId) {
    if (connectedPlayers.containsKey(connectionId)) {
      LOG.info(String.format("Disconnected %s (connection id %s)", connectedPlayers.get(connectionId), connectionId));
      availablePlayers.add(connectedPlayers.remove(connectionId));
    }
  }

  public boolean newPlayer(String connectionId, PlayerColor color) {
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
    if (this.matchStarted) {
      notifyEvent(new DecoratedEvent<>(message.getViewEvent(), this));
    }
  }

  void broadcast(OutboxMessage en) {
    lastSentCommand = en;
    context.broadcastToMatch(matchId, en);
  }

  void onNewTurn(PlayerColor player) {
    if (!connectedPlayers.containsValue(player)) {
      context.enqueueInboxMessage(matchId, new ViewEventMessage(new UnavailablePlayerEvent(player)));
      return;
    }

    if (actualTimer != null)
      actualTimer.cancel();
    actualTimer = new Timer();
    actualTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        LOG.info(String.format("Turn timer expired for player %s in match %s", player.name(), matchId));
        context.enqueueInboxMessage(matchId, new ViewEventMessage(new UnavailablePlayerEvent(player)));
      }
    }, turnTimerSeconds * 1000);
    broadcast(new NextTurnMessage(player));
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
