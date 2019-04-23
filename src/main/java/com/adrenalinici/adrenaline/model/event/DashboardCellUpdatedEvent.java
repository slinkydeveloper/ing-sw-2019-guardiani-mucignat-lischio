package com.adrenalinici.adrenaline.model.event;

import com.adrenalinici.adrenaline.model.DashboardCell;
import com.adrenalinici.adrenaline.model.GameModel;

public class DashboardCellUpdatedEvent extends BaseModelEvent {

  DashboardCell cell;

  public DashboardCellUpdatedEvent(GameModel gameModel, DashboardCell cell) {
    super(gameModel);
    this.cell = cell;
  }

  public DashboardCell getCell() {
    return cell;
  }
}
