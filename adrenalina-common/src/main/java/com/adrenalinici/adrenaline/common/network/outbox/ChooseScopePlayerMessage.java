package com.adrenalinici.adrenaline.common.network.outbox;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.PowerUpCard;

import java.util.List;
import java.util.function.Consumer;

public class ChooseScopePlayerMessage implements OutboxMessage {
  private List<PlayerColor> players;
  private List<PowerUpCard> scopes;

  public ChooseScopePlayerMessage(List<PlayerColor> players, List<PowerUpCard> scopes) {
    this.players = players;
    this.scopes = scopes;
  }

  public List<PlayerColor> getPlayers() {
    return this.players;
  }

  public List<PowerUpCard> getScopes() {
    return scopes;
  }

  @Override
  public void onChooseScopePlayerMessage(Consumer<ChooseScopePlayerMessage> c) {
    c.accept(this);
  }
}
