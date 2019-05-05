package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.*;

import java.util.*;

import java.util.function.Consumer;

public class AvailableVenomGranadesMessage implements OutboxMessage {

  private PlayerColor player;

  public AvailableVenomGranadesMessage(PlayerColor player) {
    this.player = player;
  }

  public PlayerColor getPlayer() { return this.player; }

  @Override
  public void onAvailableVenomGranadesMessage(Consumer<AvailableVenomGranadesMessage> c) { c.accept(this); }

}
