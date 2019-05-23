package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.common.Position;

import java.util.Set;
import java.util.function.Consumer;

public class AvailableCellsToHitMessage implements OutboxMessage {

  private Set<Position> cells;

  public AvailableCellsToHitMessage(Set<Position> cells) {
    this.cells = cells;
  }

  public Set<Position> getCells() {
    return this.cells;
  }

  @Override
  public void onAvailableCellsToHitMessage(Consumer<AvailableCellsToHitMessage> c) {
    c.accept(this);
  }

}
