package com.adrenalinici.adrenaline.model.light;

import com.adrenalinici.adrenaline.model.common.CellColor;
import com.adrenalinici.adrenaline.model.common.Gun;
import com.adrenalinici.adrenaline.model.common.PlayerColor;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class LightRespawnDashboardCell extends LightDashboardCell {

  private Set<Gun> availableGuns;

  public LightRespawnDashboardCell(List<PlayerColor> playersInCell, CellColor cellColor, Set<Gun> availableGuns) {
    super(playersInCell, cellColor);
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
