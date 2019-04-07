package com.adrenalinici.adrenaline.model.event;

import com.adrenalinici.adrenaline.model.DashboardCell;
import com.adrenalinici.adrenaline.model.GameStatus;

public class DashboardCellUpdatedEvent extends BaseModelEvent {

  DashboardCell cell;

  public DashboardCellUpdatedEvent(GameStatus gameStatus, DashboardCell cell) {
    super(gameStatus);
    this.cell = cell;
  }

  public DashboardCell getCell() {
    return cell;
  }
}
