package com.adrenalinici.adrenaline.model.light;

import com.adrenalinici.adrenaline.model.common.PlayerColor;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

public abstract class LightDashboardCell implements Serializable {

  private final List<PlayerColor> playersInCell;

  public LightDashboardCell(List<PlayerColor> playersInCell) {
    this.playersInCell = playersInCell;
  }

  public List<PlayerColor> getPlayersInCell() {
    return playersInCell;
  }

  public abstract void visit(Consumer<LightRespawnDashboardCell> visitRespawnDashboardCell, Consumer<LightPickupDashboardCell> visitPickupDashboardCell);
}
