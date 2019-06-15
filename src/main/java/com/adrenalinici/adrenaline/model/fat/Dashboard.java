package com.adrenalinici.adrenaline.model.fat;

import com.adrenalinici.adrenaline.model.common.CardinalDirection;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.Position;
import com.adrenalinici.adrenaline.model.light.LightDashboard;
import com.adrenalinici.adrenaline.model.light.LightDashboardCell;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.adrenalinici.adrenaline.model.common.CellColor.*;
import static com.adrenalinici.adrenaline.model.fat.DashboardCellBoundType.DOOR;
import static com.adrenalinici.adrenaline.model.fat.DashboardCellBoundType.OPEN;

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
    String dashboardChoice;

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

    public Builder setDashboardChoice(String dashboardChoice) {
      this.dashboardChoice = dashboardChoice;
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
      dashboard.setDashboardChoice(dashboardChoice);
      return dashboard;
    }


  }

  public static Dashboard.Builder newBuilder() {
    return new Builder();
  }

  public static final String SMALL = "SMALL";
  public static final String MEDIUM_1 = "MEDIUM_1";
  public static final String MEDIUM_2 = "MEDIUM_2";
  public static final String BIG = "BIG";


  private DashboardCell[][] dashboardCells;
  private String dashboardChoice;

  private Dashboard() { }

  private void setDashboardCells(DashboardCell[][] dashboardCells) {
    this.dashboardCells = dashboardCells;
  }

  public void setDashboardChoice(String dashboardChoice) {
    this.dashboardChoice = dashboardChoice;
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


  /**
   * This method calculates the positions in which a player can move,
   * doing only movements in a given direction (e.g. if range is 2
   * it can do two step north but not one north and then one east).
   *
   * @param position
   * @param range    of the distance covered by the movement
   * @return
   */
  public List<Position> calculateMovementsInOneDirection(Position position, int range) {
    DashboardCell startingCellOptional = getDashboardCell(position);
    if (startingCellOptional == null) return Collections.emptyList();

    Set<DashboardCell> result = new HashSet<>();
    result.add(startingCellOptional);

    DashboardCell cell = getDashboardCell(position);
    for (int i = 0; i < range && !cell.hasNorthWall() && cell.hasNorthDashboardCell(); i++) {
      cell = cell.getNorthDashboardCell();
      result.add(cell);
    }

    cell = getDashboardCell(position);
    for (int i = 0; i < range && !cell.hasEastWall() && cell.hasEastDashboardCell(); i++) {
      cell = cell.getEastDashboardCell();
      result.add(cell);
    }

    cell = getDashboardCell(position);
    for (int i = 0; i < range && !cell.hasSouthWall() && cell.hasSouthDashboardCell(); i++) {
      cell = cell.getSouthDashboardCell();
      result.add(cell);
    }

    cell = getDashboardCell(position);
    for (int i = 0; i < range && !cell.hasWestWall() && cell.hasWestDashboardCell(); i++) {
      cell = cell.getWestDashboardCell();
      result.add(cell);
    }

    return result.stream().map(c -> new Position(c.getLine(), c.getCell())).collect(Collectors.toList());
  }

  /**
   * This method calulates in which cardinal direction the "arrival" position is,
   * as compared to "start" position.
   * It returns SAME if the two arguments are equals.
   * It returns NONE if the "arrival" is not in one of the 4 basic cardinal directions
   * as compared to "start".
   *
   * @param start
   * @param arrival
   * @return
   */
  public CardinalDirection calculateCardinalDirection(Position start, Position arrival) {
    if (start.equals(arrival)) return CardinalDirection.SAME_CELL;
    if (start.line() == arrival.line()) {
      if (start.cell() > arrival.cell()) return CardinalDirection.WEST;
      return CardinalDirection.EAST;
    }
    if (start.cell() == arrival.cell()) {
      if (start.line() > arrival.line()) return CardinalDirection.NORTH;
      return CardinalDirection.SOUTH;
    }
    return CardinalDirection.NONE;
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
          c.hasNorthDashboardCell() &&
          !toScan.contains(c.getNorthDashboardCell())
        ) nextScan.add(c.getNorthDashboardCell());

        if (!c.hasEastWall() &&
          c.hasEastDashboardCell() &&
          !toScan.contains(c.getEastDashboardCell())
        ) nextScan.add(c.getEastDashboardCell());

        if (!c.hasSouthWall() &&
          c.hasSouthDashboardCell() &&
          !toScan.contains(c.getSouthDashboardCell())
        ) nextScan.add(c.getSouthDashboardCell());

        if (!c.hasWestWall() &&
          c.hasWestDashboardCell() &&
          !toScan.contains(c.getWestDashboardCell())
        ) nextScan.add(c.getWestDashboardCell());
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
    return Arrays.stream(dashboardCells).flatMap(Arrays::stream).filter(Objects::nonNull);
  }

  /**
   * This method calculates if a cell is visible from a certain position
   *
   * @param from starting position
   * @param to   target position
   * @return
   */
  public boolean calculateIfVisible(Position from, Position to) {
    if (this.getDashboardCell(from).getCellColor() == this.getDashboardCell(to).getCellColor())
      return true;

    if (this.getDashboardCell(from).getNorthDashboardCellBoundType().equals(DOOR) &&
      this.getDashboardCell(from).getNorthDashboardCell().getCellColor() == this.getDashboardCell(to).getCellColor()
    ) return true;

    if (this.getDashboardCell(from).getEastDashboardCellBoundType().equals(DOOR) &&
      this.getDashboardCell(from).getEastDashboardCell().getCellColor() == this.getDashboardCell(to).getCellColor()
    ) return true;

    if (this.getDashboardCell(from).getSouthDashboardCellBoundType().equals(DOOR) &&
      this.getDashboardCell(from).getSouthDashboardCell().getCellColor() == this.getDashboardCell(to).getCellColor()
    ) return true;

    if (this.getDashboardCell(from).getWestDashboardCellBoundType().equals(DOOR) &&
      this.getDashboardCell(from).getWestDashboardCell().getCellColor() == this.getDashboardCell(to).getCellColor()
    ) return true;

    return false;
  }

  public int calculateDistance(Position from, Position to) {
    if (from.equals(to)) return 0;

    DashboardCell startingCell = getDashboardCell(from);
    Set<DashboardCell> toScan = new HashSet<>();
    Set<DashboardCell> nextScan = new HashSet<>();
    toScan.add(startingCell);

    for (int i = 0; i < lines() * cells(); i++) {
      for (DashboardCell c : toScan) {
        if (!c.hasNorthWall() &&
          c.hasNorthDashboardCell() &&
          !toScan.contains(c.getNorthDashboardCell())
        ) {
          if (Position.of(c.getLine() - 1, c.getCell()).equals(to)) return i + 1;
          nextScan.add(c.getNorthDashboardCell());
        }

        if (!c.hasEastWall() &&
          c.hasEastDashboardCell() &&
          !toScan.contains(c.getEastDashboardCell())
        ) {
          if (Position.of(c.getLine(), c.getCell() + 1).equals(to)) return i + 1;
          nextScan.add(c.getEastDashboardCell());
        }

        if (!c.hasSouthWall() &&
          c.hasSouthDashboardCell() &&
          !toScan.contains(c.getSouthDashboardCell())
        ) {
          if (Position.of(c.getLine() + 1, c.getCell()).equals(to)) return i + 1;
          nextScan.add(c.getSouthDashboardCell());
        }

        if (!c.hasWestWall() &&
          c.hasWestDashboardCell() &&
          !toScan.contains(c.getWestDashboardCell())
        ) {
          if (Position.of(c.getLine(), c.getCell() - 1).equals(to)) return i + 1;
          nextScan.add(c.getWestDashboardCell());
        }
      }
      toScan.clear();
      toScan.addAll(nextScan);
      nextScan.clear();
    }
    throw new IllegalStateException("player position not found");
  }

  public LightDashboard light() {
    LightDashboardCell[][] d = new LightDashboardCell[lines()][cells()];

    for (int i = 0; i < lines(); i++) {
      for (int j = 0; j < cells(); j++) {
        if (dashboardCells[i][j] == null) d[i][j] = null;
        else d[i][j] = dashboardCells[i][j].light();
      }
    }

    return new LightDashboard(d, dashboardChoice);
  }

  public static Dashboard createSmallDashboard() {
    return Dashboard.newBuilder()
      .newEastCell(c -> c.setSouthType(DOOR).setEastType(OPEN).cellColor(CYAN).newPickupCell())
      .newEastCell(c -> c.setEastType(OPEN).setWestType(OPEN).cellColor(CYAN).newPickupCell())
      .newEastCell(c -> c.setWestType(OPEN).setSouthType(DOOR).cellColor(CYAN).newRespawnCell())
      .newEmptyCell()
      .newSouthLine()
      .newEastCell(c -> c.setNorthType(DOOR).setEastType(OPEN).cellColor(RED).newRespawnCell())
      .newEastCell(c -> c.setEastType(OPEN).setSouthType(DOOR).setWestType(OPEN).cellColor(RED).newPickupCell())
      .newEastCell(c -> c.setNorthType(DOOR).setEastType(DOOR).setWestType(OPEN).cellColor(RED).newPickupCell())
      .newEastCell(c -> c.setSouthType(OPEN).setWestType(DOOR).cellColor(YELLOW).newPickupCell())
      .newSouthLine()
      .newEmptyCell()
      .newEastCell(c -> c.setNorthType(DOOR).setEastType(OPEN).cellColor(GRAY).newPickupCell())
      .newEastCell(c -> c.setEastType(DOOR).setWestType(OPEN).cellColor(YELLOW).newPickupCell())
      .newEastCell(c -> c.setNorthType(OPEN).setWestType(DOOR).cellColor(YELLOW).newRespawnCell())
      .setDashboardChoice(SMALL)
      .build();
  }

  public static Dashboard createMedium1Dashboard() {
    return Dashboard.newBuilder()
      .newEastCell(c -> c.setSouthType(DOOR).setEastType(OPEN).cellColor(CYAN).newPickupCell())
      .newEastCell(c -> c.setEastType(OPEN).setWestType(OPEN).cellColor(CYAN).newPickupCell())
      .newEastCell(c -> c.setWestType(OPEN).setSouthType(DOOR).setEastType(DOOR).cellColor(CYAN).newRespawnCell())
      .newEastCell(c -> c.setSouthType(DOOR).setWestType(DOOR).cellColor(GREEN).newPickupCell())
      .newSouthLine()
      .newEastCell(c -> c.setNorthType(DOOR).setEastType(OPEN).cellColor(RED).newRespawnCell())
      .newEastCell(c -> c.setSouthType(DOOR).setWestType(OPEN).cellColor(RED).newPickupCell())
      .newEastCell(c -> c.setNorthType(DOOR).setEastType(OPEN).setSouthType(OPEN).cellColor(YELLOW).newPickupCell())
      .newEastCell(c -> c.setNorthType(DOOR).setSouthType(OPEN).setWestType(OPEN).cellColor(YELLOW).newPickupCell())
      .newSouthLine()
      .newEmptyCell()
      .newEastCell(c -> c.setNorthType(DOOR).setEastType(DOOR).cellColor(GRAY).newPickupCell())
      .newEastCell(c -> c.setNorthType(OPEN).setEastType(OPEN).setWestType(DOOR).cellColor(YELLOW).newPickupCell())
      .newEastCell(c -> c.setNorthType(OPEN).setWestType(OPEN).cellColor(YELLOW).newRespawnCell())
      .setDashboardChoice(MEDIUM_1)
      .build();
  }

  public static Dashboard createMedium2Dashboard() {
    return Dashboard.newBuilder()
      .newEastCell(c -> c.setEastType(DOOR).setSouthType(OPEN).cellColor(RED).newPickupCell())
      .newEastCell(c -> c.setEastType(OPEN).setSouthType(DOOR).setWestType(DOOR).cellColor(CYAN).newPickupCell())
      .newEastCell(c -> c.setSouthType(DOOR).setWestType(OPEN).cellColor(CYAN).newRespawnCell())
      .newEmptyCell()
      .newSouthLine()
      .newEastCell(c -> c.setNorthType(OPEN).setSouthType(DOOR).cellColor(RED).newRespawnCell())
      .newEastCell(c -> c.setNorthType(DOOR).setEastType(OPEN).setSouthType(DOOR).cellColor(PURPLE).newPickupCell())
      .newEastCell(c -> c.setNorthType(DOOR).setEastType(DOOR).setWestType(OPEN).cellColor(PURPLE).newPickupCell())
      .newEastCell(c -> c.setSouthType(OPEN).setWestType(DOOR).cellColor(YELLOW).newPickupCell())
      .newSouthLine()
      .newEastCell(c -> c.setNorthType(DOOR).setEastType(OPEN).cellColor(GRAY).newPickupCell())
      .newEastCell(c -> c.setNorthType(DOOR).setEastType(OPEN).setWestType(OPEN).cellColor(GRAY).newPickupCell())
      .newEastCell(c -> c.setEastType(DOOR).setWestType(OPEN).cellColor(GRAY).newPickupCell())
      .newEastCell(c -> c.setNorthType(OPEN).setWestType(DOOR).cellColor(YELLOW).newRespawnCell())
      .setDashboardChoice(MEDIUM_2)
      .build();

  }

  public static Dashboard createLargeDashboard() {
    return Dashboard.newBuilder()
      .newEastCell(c -> c.setEastType(DOOR).setSouthType(OPEN).cellColor(RED).newPickupCell())
      .newEastCell(c -> c.setEastType(OPEN).setSouthType(DOOR).setWestType(DOOR).cellColor(CYAN).newPickupCell())
      .newEastCell(c -> c.setEastType(DOOR).setSouthType(DOOR).setWestType(OPEN).cellColor(CYAN).newRespawnCell())
      .newEastCell(c -> c.setSouthType(DOOR).setWestType(DOOR).cellColor(GREEN).newPickupCell())
      .newSouthLine()
      .newEastCell(c -> c.setNorthType(OPEN).setSouthType(DOOR).cellColor(RED).newRespawnCell())
      .newEastCell(c -> c.setNorthType(DOOR).setSouthType(DOOR).cellColor(PURPLE).newPickupCell())
      .newEastCell(c -> c.setNorthType(DOOR).setEastType(OPEN).setSouthType(OPEN).cellColor(YELLOW).newPickupCell())
      .newEastCell(c -> c.setNorthType(DOOR).setSouthType(OPEN).setWestType(OPEN).cellColor(YELLOW).newPickupCell())
      .newSouthLine()
      .newEastCell(c -> c.setNorthType(DOOR).setEastType(OPEN).cellColor(GRAY).newPickupCell())
      .newEastCell(c -> c.setNorthType(DOOR).setEastType(DOOR).setWestType(OPEN).cellColor(GRAY).newPickupCell())
      .newEastCell(c -> c.setNorthType(OPEN).setEastType(OPEN).setWestType(DOOR).cellColor(YELLOW).newPickupCell())
      .newEastCell(c -> c.setNorthType(OPEN).setWestType(OPEN).cellColor(YELLOW).newRespawnCell())
      .setDashboardChoice(BIG)
      .build();
  }

}
