package com.adrenalinici.adrenaline.common.network.inbox;

import com.adrenalinici.adrenaline.common.model.DashboardChoice;
import com.adrenalinici.adrenaline.common.model.PlayersChoice;
import com.adrenalinici.adrenaline.common.model.RulesChoice;

import java.util.function.Consumer;

public class NewMatchMessage implements InboxMessage {

  DashboardChoice dashboard;
  PlayersChoice players;
  RulesChoice rules;
  String matchId;

  public NewMatchMessage(DashboardChoice dashboard, PlayersChoice players, RulesChoice rules, String matchId) {
    this.dashboard = dashboard;
    this.players = players;
    this.rules = rules;
    this.matchId = matchId;
  }

  public DashboardChoice getDashboard() {
    return dashboard;
  }

  public PlayersChoice getPlayers() {
    return players;
  }

  public RulesChoice getRules() {
    return rules;
  }

  public String getMatchId() {
    return matchId;
  }

  @Override
  public void onNewMatchMessage(Consumer<NewMatchMessage> consumer) {
    consumer.accept(this);
  }
}
