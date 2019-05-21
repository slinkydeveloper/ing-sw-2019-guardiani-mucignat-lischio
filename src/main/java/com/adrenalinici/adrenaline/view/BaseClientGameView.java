package com.adrenalinici.adrenaline.view;

import com.adrenalinici.adrenaline.model.common.DashboardChoice;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.PlayersChoice;
import com.adrenalinici.adrenaline.model.common.RulesChoice;
import com.adrenalinici.adrenaline.network.inbox.ChosenMatchMessage;
import com.adrenalinici.adrenaline.network.inbox.InboxMessage;
import com.adrenalinici.adrenaline.network.inbox.NewMatchMessage;
import com.adrenalinici.adrenaline.network.inbox.ViewEventMessage;
import com.adrenalinici.adrenaline.util.Observable;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.Map;
import java.util.Set;

public abstract class BaseClientGameView extends Observable<InboxMessage> implements GameView {

  private String matchId;
  private PlayerColor myPlayer;
  private PlayerColor turnOfPlayer;

  public void sendChosenMatch(String matchId, PlayerColor color) {
    this.matchId = matchId;
    this.myPlayer = color;
    notifyEvent(new ChosenMatchMessage(matchId, color));
  }

  public abstract void showAvailableMatchesAndPlayers(Map<String, Set<PlayerColor>> matches);

  public void sendStartNewMatch(String matchId, DashboardChoice dashboard, PlayersChoice players, RulesChoice rules) {
    notifyEvent(new NewMatchMessage(dashboard, players, rules, matchId));
  }

  public void sendViewEvent(ViewEvent event) {
    notifyEvent(new ViewEventMessage(event));
  }

  public PlayerColor getMyPlayer() {
    return myPlayer;
  }

  public PlayerColor getTurnOfPlayer() {
    return turnOfPlayer;
  }

  public BaseClientGameView setTurnOfPlayer(PlayerColor turnOfPlayer) {
    this.turnOfPlayer = turnOfPlayer;
    return this;
  }

  public boolean isMyTurn() {
    return this.turnOfPlayer == this.myPlayer;
  }

  public String getMatchId() {
    return matchId;
  }
}
