package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.common.*;

import java.util.*;

import java.util.function.Consumer;

public class AvailableTagbackGrenadeMessage implements OutboxMessage {

  private PlayerColor player;
  private List<PowerUpCard> powerUpCards;

  public AvailableTagbackGrenadeMessage(PlayerColor player, List<PowerUpCard> powerUpCards) {
    this.player = player;
    this.powerUpCards = powerUpCards;
  }

  public PlayerColor getPlayer() { return this.player; }

  public List<PowerUpCard> getPowerUpCards() { return this.powerUpCards; }

  @Override
  public void onAvailableTagbackGrenadeMessage(Consumer<AvailableTagbackGrenadeMessage> c) { c.accept(this); }

}
