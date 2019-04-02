package com.adrenalinici.adrenaline.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Class representing game dashboard <br/>
 * Each cell is determined by a tuple (line, cell), for example:
 * <table>
 * <tr>
 * <td>0, 0</th>
 * <td>0, 1</th>
 * <td>0, 2</th>
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
   * This builder helps the construction of the cell. It builds in this direction: <br/>
   * <p>
   * 1 -> 2 -> 3<br/>
   * <p>
   * 4 -> 5 -> 6<br/>
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

  public Optional<DashboardCell> getDashboardCell(int line, int cell) {
    try {
      return Optional.ofNullable(dashboardCells[line][cell]);
    } catch (IndexOutOfBoundsException e) {
      return Optional.empty();
    }
  }

  public int lines() {
    return dashboardCells.length;
  }

  public int cells() {
    return dashboardCells[0].length;
  }

}
