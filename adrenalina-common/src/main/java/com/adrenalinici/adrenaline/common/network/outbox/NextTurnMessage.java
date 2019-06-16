package com.adrenalinici.adrenaline.common.network.outbox;

import com.adrenalinici.adrenaline.common.model.PlayerColor;

import java.util.function.Consumer;

public class NextTurnMessage implements OutboxMessage {

  private PlayerColor player;

  public NextTurnMessage(PlayerColor player) {
    this.player = player;
  }

  public PlayerColor getPlayer() { return this.player; }

  @Override
  public void onNextTurnMessage(Consumer<NextTurnMessage> c) { c.accept(this); }

}
