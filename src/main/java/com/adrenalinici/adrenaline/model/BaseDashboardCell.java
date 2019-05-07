package com.adrenalinici.adrenaline.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class BaseDashboardCell implements DashboardCell {

  @FunctionalInterface
  public static interface DashboardCellFactory {
    DashboardCell create(DashboardCellBoundType northDashboardCellBoundType, DashboardCellBoundType southDashboardCellBoundType, DashboardCellBoundType eastDashboardCellBoundType, DashboardCellBoundType westDashboardCellBoundType, int cell, int line, Dashboard dashboard);
  }

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
  public void addPlayer(PlayerColor player) {
    playersInCell.add(player);
  }

  @Override
  public void removePlayer(PlayerColor player) {
    playersInCell.remove(player);
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
  public DashboardCell getNorthDashboardCell() {
    return dashboard.getDashboardCell(Position.of(line - 1, cell));
  }

  @Override
  public DashboardCell getSouthDashboardCell() {
    return dashboard.getDashboardCell(Position.of(line + 1, cell));
  }

  @Override
  public DashboardCell getEastDashboardCell() {
    return dashboard.getDashboardCell(Position.of(line, cell + 1));
  }

  @Override
  public DashboardCell getWestDashboardCell() {
    return dashboard.getDashboardCell(Position.of(line, cell - 1));
  }

  @Override
  public int getLine() {
    return line;
  }

  @Override
  public int getCell() {
    return cell;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BaseDashboardCell that = (BaseDashboardCell) o;
    return line == that.line &&
      cell == that.cell &&
      dashboard.equals(that.dashboard);
  }

  @Override
  public int hashCode() {
    return Objects.hash(line, cell, dashboard);
  }
}
