package com.adrenalinici.adrenaline.model;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class representing game dashboard <br>
 * Each cell is determined by a tuple (line, cell), for example:
 * <table>
 * <tr>
 * <td>0, 0</td>
 * <td>0, 1</td>
 * <td>0, 2</td>
 * </tr>
 * <tr>
 * <td>1, 0</td>
 * <td>1, 1</td>
 * <td>1, 2</td>
 * </tr>
 * <tr>
 * <td>2, 0</td>
 * <td>2, 1</td>
 * <td>2, 2</td>
 * </tr>
 * </table>
 */
public class Dashboard {

  /**
   * This builder helps the construction of the cell. It builds in this direction: <br>
   * <p>
   * 1 | 2 | 3<br>
   * <p>
   * 4 | 5 | 6<br>
   */
  public static class Builder {
    List<List<DashboardCell.Builder>> lines = new ArrayList<>();
    List<DashboardCell.Builder> actualLine = new ArrayList<>();

    int actualCellNumber = -1;
    int actualLineNumber = 0;

    public Builder newEastCell(Consumer<DashboardCell.Builder> c) {
      actualCellNumber++;
      DashboardCell.Builder b = new DashboardCell.Builder(actualLineNumber, actualCellNumber);
      actualLine.add(b);
      c.accept(b);
      return this;
    }

    public Builder newEmptyCell() {
      actualCellNumber++;
      actualLine.add(null);
      return this;
    }

    public Builder newSouthLine() {
      this.lines.add(actualLine);
      this.actualLine = new ArrayList<>();
      actualLineNumber++;
      actualCellNumber = -1;
      return this;
    }

    public Dashboard build() {
      this.lines.add(actualLine);
      int width = actualLine.size();
      if (lines.stream().mapToInt(List::size).anyMatch(i -> i != width))
        throw new IllegalStateException("Cannot build board with different length lines");

      Dashboard dashboard = new Dashboard();
      DashboardCell[][] cells = new DashboardCell[this.lines.size()][width];
      for (int i = 0; i < this.lines.size(); i++) {
        for (int j = 0; j < this.actualLine.size(); j++) {
          DashboardCell.Builder b = this.lines.get(i).get(j);
          cells[i][j] = b != null ? b.build(dashboard) : null;
        }
      }
      dashboard.setDashboardCells(cells);
      return dashboard;
    }


  }

  public static Dashboard.Builder newBuilder() {
    return new Builder();
  }

  private DashboardCell[][] dashboardCells;

  private Dashboard() {
  }

  private void setDashboardCells(DashboardCell[][] dashboardCells) {
    this.dashboardCells = dashboardCells;
  }

  public DashboardCell getDashboardCell(Position position) {
    try {
      return dashboardCells[position.line()][position.cell()];
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

  public int lines() {
    return dashboardCells.length;
  }

  public int cells() {
    return dashboardCells[0].length;
  }

  public List<Position> calculateMovements(Position position, int range) {
    DashboardCell startingCellOptional = getDashboardCell(position);
    if (startingCellOptional == null) return Collections.emptyList();

    Set<DashboardCell> scanned = new HashSet<>();
    Set<DashboardCell> toScan = new HashSet<>();
    Set<DashboardCell> nextScan = new HashSet<>();
    toScan.add(startingCellOptional);

    for (int i = 0; i < range; i++) {
      for (DashboardCell c : toScan) {
        if (!c.hasNorthWall() &&
          c.getNorthDashboardCell().isPresent() &&
          !toScan.contains(c.getNorthDashboardCell().get())
        ) nextScan.add(c.getNorthDashboardCell().get());

        if (!c.hasEastWall() &&
          c.getEastDashboardCell().isPresent() &&
          !toScan.contains(c.getEastDashboardCell().get())
        ) nextScan.add(c.getEastDashboardCell().get());

        if (!c.hasSouthWall() &&
          c.getSouthDashboardCell().isPresent() &&
          !toScan.contains(c.getSouthDashboardCell().get())
        ) nextScan.add(c.getSouthDashboardCell().get());

        if (!c.hasWestWall() &&
          c.getWestDashboardCell().isPresent() &&
          !toScan.contains(c.getWestDashboardCell().get())
        ) nextScan.add(c.getWestDashboardCell().get());
      }
      scanned.addAll(toScan);
      toScan.clear();
      toScan.addAll(nextScan);
      nextScan.clear();
    }

    scanned.addAll(toScan);
    scanned.addAll(nextScan);

    return scanned.stream().map(c -> new Position(c.getLine(), c.getCell())).collect(Collectors.toList());

  }

  public Map<PlayerColor, Position> getPlayersPositions() {
    return stream()
      .filter(c -> c.getPlayersInCell().size() != 0)
      .flatMap(c -> c.getPlayersInCell().stream().map(p -> new SimpleImmutableEntry<>(p, new Position(c.getLine(), c.getCell()))))
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  public Stream<DashboardCell> stream() {
    return Arrays.stream(dashboardCells).flatMap(Arrays::stream);
  }

  public boolean calculateIfVisible(Position from, Position to) {
    if (from.equals(to)) return true;

    List<DashboardCell> walled;
    if (from.line() == to.line()) {
      if (from.cell() < to.cell()) {
        walled = stream()
          .filter(dashboardCell ->
            dashboardCell.getLine() == from.line() &&
              dashboardCell.hasEastWall() &&
              dashboardCell.getCell() >= from.cell() &&
              dashboardCell.getCell() < to.cell()
          ).collect(Collectors.toList());
        return walled.isEmpty();
      } else {
        walled = stream()
          .filter(dashboardCell ->
            dashboardCell.getLine() == from.line() &&
              dashboardCell.hasWestWall() &&
              dashboardCell.getCell() <= from.cell() &&
              dashboardCell.getCell() > to.cell()
          ).collect(Collectors.toList());
        return walled.isEmpty();
      }
    } else if (from.cell() == to.cell()) {
      if (from.line() < to.line()) {
        walled = stream()
          .filter(dashboardCell ->
            dashboardCell.getCell() == from.cell() &&
              dashboardCell.hasSouthWall() &&
              dashboardCell.getLine() >= from.line() &&
              dashboardCell.getLine() < to.line()
          ).collect(Collectors.toList());
        return walled.isEmpty();
      } else {
        walled = stream()
          .filter(dashboardCell ->
            dashboardCell.getCell() == from.cell() &&
              dashboardCell.hasNorthWall() &&
              dashboardCell.getLine() <= from.line() &&
              dashboardCell.getLine() > to.line()
          ).collect(Collectors.toList());
        return walled.isEmpty();
      }
    } else return false;
  }

  public int calculateDistance(Position from, Position to) {
    if (from.equals(to)) return 0;

    DashboardCell startingCell = getDashboardCell(from);
    Set<DashboardCell> toScan = new HashSet<>();
    Set<DashboardCell> nextScan = new HashSet<>();
    toScan.add(startingCell);

    for (int i = 0; ; i++) {
      for (DashboardCell c : toScan) {
        if (!c.hasNorthWall() &&
          c.getNorthDashboardCell().isPresent() &&
          !toScan.contains(c.getNorthDashboardCell().get())
        ) {
          if (Position.of(c.getLine() - 1, c.getCell()).equals(to)) return i + 1;
          nextScan.add(c.getNorthDashboardCell().get());
        }

        if (!c.hasEastWall() &&
          c.getEastDashboardCell().isPresent() &&
          !toScan.contains(c.getEastDashboardCell().get())
        ) {
          if (Position.of(c.getLine(), c.getCell() + 1).equals(to)) return i + 1;
          nextScan.add(c.getEastDashboardCell().get());
        }

        if (!c.hasSouthWall() &&
          c.getSouthDashboardCell().isPresent() &&
          !toScan.contains(c.getSouthDashboardCell().get())
        ) {
          if (Position.of(c.getLine() + 1, c.getCell()).equals(to)) return i + 1;
          nextScan.add(c.getSouthDashboardCell().get());
        }

        if (!c.hasWestWall() &&
          c.getWestDashboardCell().isPresent() &&
          !toScan.contains(c.getWestDashboardCell().get())
        ) {
          if (Position.of(c.getLine(), c.getCell() - 1).equals(to)) return i + 1;
          nextScan.add(c.getWestDashboardCell().get());
        }
      }
      toScan.clear();
      toScan.addAll(nextScan);
      nextScan.clear();
    }
  }
}
