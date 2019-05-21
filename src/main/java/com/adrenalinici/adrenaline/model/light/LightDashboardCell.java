package com.adrenalinici.adrenaline.model.light;

import com.adrenalinici.adrenaline.model.common.CellColor;
import com.adrenalinici.adrenaline.model.common.PlayerColor;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

public abstract class LightDashboardCell implements Serializable {

  private final List<PlayerColor> playersInCell;
  private final CellColor cellColor;

  public LightDashboardCell(List<PlayerColor> playersInCell, CellColor cellColor) {
    this.playersInCell = playersInCell;
    this.cellColor = cellColor;
  }

  public List<PlayerColor> getPlayersInCell() {
    return playersInCell;
  }

  public abstract void visit(Consumer<LightRespawnDashboardCell> visitRespawnDashboardCell, Consumer<LightPickupDashboardCell> visitPickupDashboardCell);

  @Override
  public String toString() {
    return "LightDashboardCell{" +
      "playersInCell=" + playersInCell +
      ", cellColor=" + cellColor +
      '}';
  }
}
