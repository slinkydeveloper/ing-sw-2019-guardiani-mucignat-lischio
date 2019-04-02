package com.adrenalinici.adrenaline.model;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface DashboardCell {

  class Builder {
    private DashboardCellBoundType northDashboardCellBoundType = DashboardCellBoundType.WALL;
    private DashboardCellBoundType southDashboardCellBoundType = DashboardCellBoundType.WALL;
    private DashboardCellBoundType eastDashboardCellBoundType = DashboardCellBoundType.WALL;
    private DashboardCellBoundType westDashboardCellBoundType = DashboardCellBoundType.WALL;
    private final int cell;
    private final int line;
    private DashboardCellFactory cellFactory;

    public Builder(int line, int cell) {
      this.cell = cell;
      this.line = line;
    }

    public Builder setNorthType(DashboardCellBoundType northDashboardCellBoundType) {
      this.northDashboardCellBoundType = northDashboardCellBoundType;
      return this;
    }

    public Builder setSouthType(DashboardCellBoundType southDashboardCellBoundType) {
      this.southDashboardCellBoundType = southDashboardCellBoundType;
      return this;
    }

    public Builder setEastType(DashboardCellBoundType eastDashboardCellBoundType) {
      this.eastDashboardCellBoundType = eastDashboardCellBoundType;
      return this;
    }

    public Builder setWestType(DashboardCellBoundType westDashboardCellBoundType) {
      this.westDashboardCellBoundType = westDashboardCellBoundType;
      return this;
    }

    public void newPickupCell() {
      cellFactory = PickupDashboardCell::new;
    }

    public void newRespawnCell() {
      cellFactory = RespawnDashboardCell::new;
    }

    protected DashboardCell build(Dashboard dashboardInstance) {
      return cellFactory.create(northDashboardCellBoundType, southDashboardCellBoundType, eastDashboardCellBoundType, westDashboardCellBoundType, line, cell, dashboardInstance);
    }


  }

  List<PlayerColor> getPlayersInCell();

  DashboardCellBoundType getNorthDashboardCellBoundType();

  DashboardCellBoundType getSouthDashboardCellBoundType();

  DashboardCellBoundType getEastDashboardCellBoundType();

  DashboardCellBoundType getWestDashboardCellBoundType();

  Optional<DashboardCell> getNorthDashboardCell();

  Optional<DashboardCell> getSouthDashboardCell();

  Optional<DashboardCell> getEastDashboardCell();

  Optional<DashboardCell> getWestDashboardCell();

  void visit(Consumer<RespawnDashboardCell> visitRespawnDashboardCell, Consumer<PickupDashboardCell> visitPickupDashboardCell);
}
