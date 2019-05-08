package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.*;
import com.adrenalinici.adrenaline.model.common.*;

import java.util.*;

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
