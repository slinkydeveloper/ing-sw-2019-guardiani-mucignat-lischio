package com.adrenalinici.adrenaline.common.network.outbox;

import com.adrenalinici.adrenaline.common.model.Position;

import java.util.List;
import java.util.function.Consumer;

public class AvailableMovementsMessage implements OutboxMessage {

  private List<Position> positions;

  public AvailableMovementsMessage(List<Position> positions) {
    this.positions = positions;
  }

  public List<Position> getPositions() { return this.positions; }

  @Override
  public void onAvailableMovementsMessage(Consumer<AvailableMovementsMessage> c) { c.accept(this); }

}
