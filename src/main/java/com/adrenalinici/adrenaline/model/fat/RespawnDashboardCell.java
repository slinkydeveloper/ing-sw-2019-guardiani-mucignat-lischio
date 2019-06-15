package com.adrenalinici.adrenaline.model.fat;

import com.adrenalinici.adrenaline.controller.GunLoader;
import com.adrenalinici.adrenaline.model.common.CellColor;
import com.adrenalinici.adrenaline.model.light.LightDashboardCell;
import com.adrenalinici.adrenaline.model.light.LightRespawnDashboardCell;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RespawnDashboardCell extends BaseDashboardCell {

  private Set<String> availableGuns;

  public RespawnDashboardCell(DashboardCellBoundType northDashboardCellBoundType, DashboardCellBoundType southDashboardCellBoundType, DashboardCellBoundType eastDashboardCellBoundType, DashboardCellBoundType westDashboardCellBoundType, CellColor cellColor, int line, int cell, Dashboard dashboard) {
    super(northDashboardCellBoundType, southDashboardCellBoundType, eastDashboardCellBoundType, westDashboardCellBoundType, cellColor, line, cell, dashboard);
    this.availableGuns = new HashSet<>();
  }

  public Set<String> getAvailableGuns() {
    return availableGuns;
  }

  public void addAvailableGun(String gun) {
    if (availableGuns.size() == 3) throw new IllegalStateException("Already 3 guns in this cell");
    availableGuns.add(gun);
  }

  public void removeAvailableGun(String gunToRemove) {
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

  @Override
  public LightDashboardCell light() {
    return new LightRespawnDashboardCell(
      getPlayersInCell(),
      getCellColor(),
      availableGuns.stream().map(GunLoader.INSTANCE::getModelGun).collect(Collectors.toSet()),
      getNorthDashboardCellBoundType(),
      getSouthDashboardCellBoundType(),
      getEastDashboardCellBoundType(),
      getWestDashboardCellBoundType(),
      getLine(),
      getCell()
    );
  }
}
