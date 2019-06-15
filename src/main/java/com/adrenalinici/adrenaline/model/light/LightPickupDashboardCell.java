package com.adrenalinici.adrenaline.model.light;

import com.adrenalinici.adrenaline.model.common.AmmoCard;
import com.adrenalinici.adrenaline.model.common.CellColor;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.fat.DashboardCellBoundType;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class LightPickupDashboardCell extends LightDashboardCell {

  private AmmoCard ammoCard;

  public LightPickupDashboardCell(List<PlayerColor> playersInCell, CellColor cellColor, AmmoCard ammoCard, DashboardCellBoundType northDashboardCellBoundType, DashboardCellBoundType southDashboardCellBoundType, DashboardCellBoundType eastDashboardCellBoundType, DashboardCellBoundType westDashboardCellBoundType, int line, int cell) {
    super(playersInCell, cellColor, northDashboardCellBoundType, southDashboardCellBoundType, eastDashboardCellBoundType, westDashboardCellBoundType, line, cell);
    this.ammoCard = ammoCard;
  }

  public AmmoCard getAmmoCard() {
    return ammoCard;
  }

  @Override
  public void visit(Consumer<LightRespawnDashboardCell> visitRespawnDashboardCell, Consumer<LightPickupDashboardCell> visitPickupDashboardCell) {
    visitPickupDashboardCell.accept(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LightPickupDashboardCell that = (LightPickupDashboardCell) o;
    return Objects.equals(ammoCard, that.ammoCard);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ammoCard);
  }
}
