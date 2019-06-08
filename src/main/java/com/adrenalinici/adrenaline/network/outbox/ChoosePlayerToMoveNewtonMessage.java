package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.Position;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ChoosePlayerToMoveNewtonMessage implements OutboxMessage {

  private Map<PlayerColor, List<Position>> availableMovements;

  public ChoosePlayerToMoveNewtonMessage(Map<PlayerColor, List<Position>> availableMovements) {
    this.availableMovements = availableMovements;
  }

  public Map<PlayerColor, List<Position>> getAvailableMovements() { return this.availableMovements; }

  @Override
  public void onChoosePlayerToMoveMessage(Consumer<ChoosePlayerToMoveNewtonMessage> c) { c.accept(this); }

}
