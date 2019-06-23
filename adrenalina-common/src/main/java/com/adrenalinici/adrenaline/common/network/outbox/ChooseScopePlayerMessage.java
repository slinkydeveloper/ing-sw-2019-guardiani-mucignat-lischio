package com.adrenalinici.adrenaline.common.network.outbox;

import com.adrenalinici.adrenaline.common.model.PlayerColor;

import java.util.List;
import java.util.function.Consumer;

public class ChooseScopePlayerMessage implements OutboxMessage {
  private List<PlayerColor> players;

  public ChooseScopePlayerMessage(List<PlayerColor> players) {
    this.players = players;
  }

  public List<PlayerColor> getPlayers() {
    return this.players;
  }

  @Override
  public void onChooseScopePlayerMessage(Consumer<ChooseScopePlayerMessage> c) {
    c.accept(this);
  }
}
