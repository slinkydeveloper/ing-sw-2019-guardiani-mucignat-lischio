package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.model.common.Position;

import java.util.Objects;
import java.util.function.Consumer;

public class CellToHitChosenEvent implements ViewEvent {
  private Position cellPosition;

  public CellToHitChosenEvent(Position cellPosition) {
    this.cellPosition = cellPosition;
  }

  public Position getCellPosition() {
    return cellPosition;
  }

  @Override
  public void onCellToHitChosenEvent(Consumer<CellToHitChosenEvent> consumer) {
    consumer.accept(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CellToHitChosenEvent that = (CellToHitChosenEvent) o;
    return cellPosition == that.cellPosition;
  }

  @Override
  public int hashCode() {
    return Objects.hash(cellPosition);
  }
}
