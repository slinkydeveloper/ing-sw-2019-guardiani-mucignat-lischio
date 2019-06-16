package com.adrenalinici.adrenaline.common.network.outbox;

import com.adrenalinici.adrenaline.common.model.PlayerColor;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class AvailableMatchesMessage implements OutboxMessage {

  Map<String, Set<PlayerColor>> matchesRemainingPlayers;

  public AvailableMatchesMessage(Map<String, Set<PlayerColor>> matchesRemainingPlayers) {
    this.matchesRemainingPlayers = matchesRemainingPlayers;
  }

  public Map<String, Set<PlayerColor>> getMatchesRemainingPlayers() {
    return matchesRemainingPlayers;
  }

  @Override
  public void onAvailableMatchesMessage(Consumer<AvailableMatchesMessage> c) {
    c.accept(this);
  }
}
