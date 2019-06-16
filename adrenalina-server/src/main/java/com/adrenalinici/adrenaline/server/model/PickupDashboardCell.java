package com.adrenalinici.adrenaline.server.model;

import com.adrenalinici.adrenaline.common.model.AmmoCard;
import com.adrenalinici.adrenaline.common.model.CellColor;
import com.adrenalinici.adrenaline.common.model.DashboardCellBoundType;
import com.adrenalinici.adrenaline.common.model.light.LightDashboardCell;
import com.adrenalinici.adrenaline.common.model.light.LightPickupDashboardCell;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class PickupDashboardCell extends BaseDashboardCell {

  private AmmoCard ammoCard;

  public PickupDashboardCell(DashboardCellBoundType northDashboardCellBoundType, DashboardCellBoundType southDashboardCellBoundType, DashboardCellBoundType eastDashboardCellBoundType, DashboardCellBoundType westDashboardCellBoundType, CellColor cellColor, int line, int cell, Dashboard dashboard) {
    super(northDashboardCellBoundType, southDashboardCellBoundType, eastDashboardCellBoundType, westDashboardCellBoundType, cellColor, line, cell, dashboard);
  }

  public Optional<AmmoCard> getAmmoCard() {
    return Optional.ofNullable(ammoCard);
  }

  public void setAmmoCard(AmmoCard ammoCard) {
    Objects.requireNonNull(ammoCard);
    this.ammoCard = ammoCard;
  }

  public void removeAmmoCard() {
    this.ammoCard = null;
  }

  @Override
  public void visit(Consumer<RespawnDashboardCell> visitRespawnDashboardCell, Consumer<PickupDashboardCell> visitPickupDashboardCell) {
    if (visitPickupDashboardCell != null) {
      visitPickupDashboardCell.accept(this);
    }
  }

  @Override
  public boolean isPickupCell() {
    return true;
  }

  @Override
  public boolean isRespawnCell() {
    return false;
  }

  @Override
  public LightDashboardCell light() {
    return new LightPickupDashboardCell(
      getPlayersInCell(),
      getCellColor(),
      ammoCard,
      getNorthDashboardCellBoundType(),
      getSouthDashboardCellBoundType(),
      getEastDashboardCellBoundType(),
      getWestDashboardCellBoundType(),
      getLine(),
      getCell()
    );
  }

}
