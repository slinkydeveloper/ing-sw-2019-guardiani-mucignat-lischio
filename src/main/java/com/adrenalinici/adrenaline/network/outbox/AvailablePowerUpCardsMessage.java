package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.*;

import java.util.*;

import java.util.function.Consumer;

public class AvailablePowerUpCardsMessage implements OutboxMessage {

  private PlayerColor player;

  public AvailablePowerUpCardsMessage(PlayerColor player) {
    this.player = player;
  }

  public PlayerColor getPlayer() { return this.player; }

  @Override
  public void onAvailablePowerUpCardsMessage(Consumer<AvailablePowerUpCardsMessage> c) { c.accept(this); }

}
