package com.adrenalinici.adrenaline.common.model.light;

import com.adrenalinici.adrenaline.common.model.DashboardChoice;
import com.adrenalinici.adrenaline.common.model.Position;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Stream;

public class LightDashboard implements Serializable {

  private LightDashboardCell[][] dashboardCells;
  private DashboardChoice dashboardChoice;

  public LightDashboard(LightDashboardCell[][] dashboardCells, DashboardChoice dashboardChoice) {
    this.dashboardCells = dashboardCells;
    this.dashboardChoice = dashboardChoice;
  }

  public LightDashboardCell getDashboardCell(int line, int cell) {
    return getDashboardCell(Position.of(line, cell));
  }

  public LightDashboardCell getDashboardCell(Position position) {
    try {
      return dashboardCells[position.line()][position.cell()];
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

  public DashboardChoice getDashboardChoice() {
    return dashboardChoice;
  }

  public int lines() {
    return dashboardCells.length;
  }

  public int cells() {
    return dashboardCells[0].length;
  }

  @Override
  public String toString() {
    return "LightDashboard{" +
      "dashboardCells=" + Arrays.toString(dashboardCells) +
      "dashboardChoice=" + dashboardChoice +
      '}';
  }

  public Stream<LightDashboardCell> stream() {
    return Arrays.stream(dashboardCells).flatMap(Arrays::stream);
  }
}
