package com.adrenalinici.adrenaline.server.model;

import com.adrenalinici.adrenaline.common.model.CellColor;
import com.adrenalinici.adrenaline.common.model.DashboardCellBoundType;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.model.light.LightDashboardCell;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

public interface DashboardCell extends Serializable {

  class Builder {
    private DashboardCellBoundType northDashboardCellBoundType = DashboardCellBoundType.WALL;
    private DashboardCellBoundType southDashboardCellBoundType = DashboardCellBoundType.WALL;
    private DashboardCellBoundType eastDashboardCellBoundType = DashboardCellBoundType.WALL;
    private DashboardCellBoundType westDashboardCellBoundType = DashboardCellBoundType.WALL;
    private CellColor color = CellColor.YELLOW;
    private final int cell;
    private final int line;
    private BaseDashboardCell.DashboardCellFactory cellFactory;

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

    public Builder cellColor(CellColor color) {
      this.color = color;
      return this;
    }

    public void newPickupCell() {
      cellFactory = PickupDashboardCell::new;
    }

    public void newRespawnCell() {
      cellFactory = RespawnDashboardCell::new;
    }

    protected DashboardCell build(Dashboard dashboardInstance) {
      return cellFactory.create(northDashboardCellBoundType, southDashboardCellBoundType, eastDashboardCellBoundType, westDashboardCellBoundType, color, line, cell, dashboardInstance);
    }

  }

  int getLine();

  int getCell();

  default Position getPosition() {
    return Position.of(getLine(), getCell());
  }

  CellColor getCellColor();

  List<PlayerColor> getPlayersInCell();

  void addPlayer(PlayerColor player);

  void removePlayer(PlayerColor player);

  DashboardCellBoundType getNorthDashboardCellBoundType();

  default boolean hasNorthWall() {
    return getNorthDashboardCellBoundType() == DashboardCellBoundType.WALL;
  }

  DashboardCellBoundType getSouthDashboardCellBoundType();

  default boolean hasSouthWall() {
    return getSouthDashboardCellBoundType() == DashboardCellBoundType.WALL;
  }

  DashboardCellBoundType getEastDashboardCellBoundType();

  default boolean hasEastWall() {
    return getEastDashboardCellBoundType() == DashboardCellBoundType.WALL;
  }

  DashboardCellBoundType getWestDashboardCellBoundType();

  default boolean hasWestWall() {
    return getWestDashboardCellBoundType() == DashboardCellBoundType.WALL;
  }

  boolean isRespawnCell();

  boolean isPickupCell();

  DashboardCell getNorthDashboardCell();

  default boolean hasNorthDashboardCell() {
    return getNorthDashboardCell() != null;
  }

  DashboardCell getSouthDashboardCell();

  default boolean hasSouthDashboardCell() {
    return getSouthDashboardCell() != null;
  }

  DashboardCell getEastDashboardCell();

  default boolean hasEastDashboardCell() {
    return getEastDashboardCell() != null;
  }

  DashboardCell getWestDashboardCell();

  default boolean hasWestDashboardCell() {
    return getWestDashboardCell() != null;
  }

  LightDashboardCell light();

  void visit(Consumer<RespawnDashboardCell> visitRespawnDashboardCell, Consumer<PickupDashboardCell> visitPickupDashboardCell);
}
