package com.adrenalinici.adrenaline.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseDashboardCell implements DashboardCell {
  private final List<PlayerColor> playersInCell;
  private final DashboardCellBoundType northDashboardCellBoundType;
  private final DashboardCellBoundType southDashboardCellBoundType;
  private final DashboardCellBoundType eastDashboardCellBoundType;
  private final DashboardCellBoundType westDashboardCellBoundType;
  private final int line;
  private final int cell;
  private final Dashboard dashboard;

  public BaseDashboardCell(DashboardCellBoundType northDashboardCellBoundType, DashboardCellBoundType southDashboardCellBoundType, DashboardCellBoundType eastDashboardCellBoundType, DashboardCellBoundType westDashboardCellBoundType, int line, int cell, Dashboard dashboard) {
    this.playersInCell = new ArrayList<>();
    this.northDashboardCellBoundType = northDashboardCellBoundType;
    this.southDashboardCellBoundType = southDashboardCellBoundType;
    this.eastDashboardCellBoundType = eastDashboardCellBoundType;
    this.westDashboardCellBoundType = westDashboardCellBoundType;
    this.line = line;
    this.cell = cell;
    this.dashboard = dashboard;
  }

  @Override
  public List<PlayerColor> getPlayersInCell() {
    return playersInCell;
  }

  @Override
  public DashboardCellBoundType getNorthDashboardCellBoundType() {
    return northDashboardCellBoundType;
  }

  @Override
  public DashboardCellBoundType getSouthDashboardCellBoundType() {
    return southDashboardCellBoundType;
  }

  @Override
  public DashboardCellBoundType getEastDashboardCellBoundType() {
    return eastDashboardCellBoundType;
  }

  @Override
  public DashboardCellBoundType getWestDashboardCellBoundType() {
    return westDashboardCellBoundType;
  }

  @Override
  public Optional<DashboardCell> getNorthDashboardCell() {
    return dashboard.getDashboardCell(line - 1, cell);
  }

  @Override
  public Optional<DashboardCell> getSouthDashboardCell() {
    return dashboard.getDashboardCell(line + 1, cell);
  }

  @Override
  public Optional<DashboardCell> getEastDashboardCell() {
    return dashboard.getDashboardCell(line, cell + 1);
  }

  @Override
  public Optional<DashboardCell> getWestDashboardCell() {
    return dashboard.getDashboardCell(line, cell - 1);
  }
}
