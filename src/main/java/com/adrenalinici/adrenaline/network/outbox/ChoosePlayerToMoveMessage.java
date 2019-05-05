package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.*;

import java.util.*;

import java.util.function.Consumer;

public class ChoosePlayerToMoveMessage implements OutboxMessage {

  private Map<PlayerColor, List<Position>> availableMovements;

  public ChoosePlayerToMoveMessage(Map<PlayerColor, List<Position>> availableMovements) {
    this.availableMovements = availableMovements;
  }

  public Map<PlayerColor, List<Position>> getAvailableMovements() { return this.availableMovements; }

  @Override
  public void onChoosePlayerToMoveMessage(Consumer<ChoosePlayerToMoveMessage> c) { c.accept(this); }

}
