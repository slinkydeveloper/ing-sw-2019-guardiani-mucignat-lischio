package com.adrenalinici.adrenaline.common.model.event;

import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.model.light.LightGameModel;

import java.util.function.Consumer;

public class DashboardCellUpdatedEvent extends BaseModelEvent {

  private Position cellPosition;

  public DashboardCellUpdatedEvent(LightGameModel gameModel, Position cellPosition) {
    super(gameModel);
    this.cellPosition = cellPosition;
  }

  public Position getCellPosition() {
    return cellPosition;
  }

  @Override
  public void onDashboardCellUpdatedEvent(Consumer<DashboardCellUpdatedEvent> c) {
    c.accept(this);
  }
}
