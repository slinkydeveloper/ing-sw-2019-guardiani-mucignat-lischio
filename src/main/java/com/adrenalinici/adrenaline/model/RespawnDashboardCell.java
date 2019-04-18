package com.adrenalinici.adrenaline.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RespawnDashboardCell extends BaseDashboardCell {

  private List<Gun> availableGuns;

  public RespawnDashboardCell(DashboardCellBoundType northDashboardCellBoundType, DashboardCellBoundType southDashboardCellBoundType, DashboardCellBoundType eastDashboardCellBoundType, DashboardCellBoundType westDashboardCellBoundType, int line, int cell, Dashboard dashboard) {
    super(northDashboardCellBoundType, southDashboardCellBoundType, eastDashboardCellBoundType, westDashboardCellBoundType, line, cell, dashboard);
    this.availableGuns = new ArrayList<>();
  }

  public List<Gun> getAvailableGuns() {
    return availableGuns;
  }

  public void addAvailableGun(Gun gun) {
    availableGuns.add(gun);
  }

  public void removeAvailableGun(Gun gunToRemove) {
    availableGuns.remove(gunToRemove);
  }

  @Override
  public void visit(Consumer<RespawnDashboardCell> visitRespawnDashboardCell, Consumer<PickupDashboardCell> visitPickupDashboardCell) {
    if (visitRespawnDashboardCell != null) {
      visitRespawnDashboardCell.accept(this);
    }
  }

  @Override
  public boolean isRespawnCell() {
    return true;
  }

  @Override
  public boolean isPickupCell() {
    return false;
  }
}
