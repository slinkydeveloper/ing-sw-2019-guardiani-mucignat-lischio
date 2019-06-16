package com.adrenalinici.adrenaline.common.model.light;

import com.adrenalinici.adrenaline.common.model.CellColor;
import com.adrenalinici.adrenaline.common.model.DashboardCellBoundType;
import com.adrenalinici.adrenaline.common.model.Gun;
import com.adrenalinici.adrenaline.common.model.PlayerColor;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class LightRespawnDashboardCell extends LightDashboardCell {

  private Set<Gun> availableGuns;

  public LightRespawnDashboardCell(List<PlayerColor> playersInCell, CellColor cellColor, Set<Gun> availableGuns, DashboardCellBoundType northDashboardCellBoundType, DashboardCellBoundType southDashboardCellBoundType, DashboardCellBoundType eastDashboardCellBoundType, DashboardCellBoundType westDashboardCellBoundType, int line, int cell) {
    super(playersInCell, cellColor, northDashboardCellBoundType, southDashboardCellBoundType, eastDashboardCellBoundType, westDashboardCellBoundType, line, cell);
    this.availableGuns = availableGuns;
  }

  public Set<Gun> getAvailableGuns() {
    return availableGuns;
  }

  @Override
  public void visit(Consumer<LightRespawnDashboardCell> visitRespawnDashboardCell, Consumer<LightPickupDashboardCell> visitPickupDashboardCell) {
    visitRespawnDashboardCell.accept(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LightRespawnDashboardCell that = (LightRespawnDashboardCell) o;
    return Objects.equals(availableGuns, that.availableGuns);
  }

  @Override
  public int hashCode() {
    return Objects.hash(availableGuns);
  }
}
