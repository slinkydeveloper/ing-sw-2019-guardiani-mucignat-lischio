package com.adrenalinici.adrenaline.model.event;

import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.model.common.Position;

public class DashboardCellUpdatedEvent extends BaseModelEvent {

  private Position cellPosition;

  public DashboardCellUpdatedEvent(GameModel gameModel, Position cellPosition) {
    super(gameModel);
    this.cellPosition = cellPosition;
  }

  public Position getCellPosition() {
    return cellPosition;
  }
}
