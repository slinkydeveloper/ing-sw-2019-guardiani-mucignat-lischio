package com.adrenalinici.adrenaline.model.light;

import com.adrenalinici.adrenaline.model.common.Position;

import java.io.Serializable;

public class LightDashboard implements Serializable {

  private LightDashboardCell[][] dashboardCells;

  public LightDashboard(LightDashboardCell[][] dashboardCells) {
    this.dashboardCells = dashboardCells;
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

  public int lines() {
    return dashboardCells.length;
  }

  public int cells() {
    return dashboardCells[0].length;
  }
}
