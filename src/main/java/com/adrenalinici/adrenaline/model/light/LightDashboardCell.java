package com.adrenalinici.adrenaline.model.light;

import com.adrenalinici.adrenaline.model.common.CellColor;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.fat.DashboardCellBoundType;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

public abstract class LightDashboardCell implements Serializable {

  private final List<PlayerColor> playersInCell;
  private final CellColor cellColor;
  private final DashboardCellBoundType northDashboardCellBoundType;
  private final DashboardCellBoundType southDashboardCellBoundType;
  private final DashboardCellBoundType eastDashboardCellBoundType;
  private final DashboardCellBoundType westDashboardCellBoundType;
  private final int line;
  private final int cell;


  public LightDashboardCell(List<PlayerColor> playersInCell, CellColor cellColor, DashboardCellBoundType northDashboardCellBoundType, DashboardCellBoundType southDashboardCellBoundType, DashboardCellBoundType eastDashboardCellBoundType, DashboardCellBoundType westDashboardCellBoundType, int line, int cell) {
    this.playersInCell = playersInCell;
    this.cellColor = cellColor;
    this.northDashboardCellBoundType = northDashboardCellBoundType;
    this.southDashboardCellBoundType = southDashboardCellBoundType;
    this.eastDashboardCellBoundType = eastDashboardCellBoundType;
    this.westDashboardCellBoundType = westDashboardCellBoundType;
    this.line = line;
    this.cell = cell;
  }

  public List<PlayerColor> getPlayersInCell() {
    return playersInCell;
  }

  public CellColor getCellColor() {
    return cellColor;
  }

  public DashboardCellBoundType getNorthDashboardCellBoundType() {
    return northDashboardCellBoundType;
  }

  public DashboardCellBoundType getSouthDashboardCellBoundType() {
    return southDashboardCellBoundType;
  }

  public DashboardCellBoundType getEastDashboardCellBoundType() {
    return eastDashboardCellBoundType;
  }

  public DashboardCellBoundType getWestDashboardCellBoundType() {
    return westDashboardCellBoundType;
  }

  public int getLine() {
    return line;
  }

  public int getCell() {
    return cell;
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
