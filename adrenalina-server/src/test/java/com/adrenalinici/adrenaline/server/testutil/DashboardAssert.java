package com.adrenalinici.adrenaline.server.testutil;

import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.server.model.Dashboard;
import com.adrenalinici.adrenaline.server.model.DashboardCell;
import org.assertj.core.api.AbstractAssert;

import static org.assertj.core.api.Assertions.assertThat;


public class DashboardAssert extends AbstractAssert<DashboardAssert, Dashboard> {

  public DashboardAssert(Dashboard dashboard) {
    super(dashboard, DashboardAssert.class);
  }

  public DashboardCellAssert extractingCell(int line, int cell) {
    return this.extractingCell(Position.of(line, cell));
  }

  public DashboardCellAssert extractingCell(Position position) {
    DashboardCell cell = actual.getDashboardCell(position);

    assertThat(cell)
      .isNotNull();

    return new DashboardCellAssert(cell);
  }

  public DashboardAssert hasCell(int line, int cell) {
    extractingCell(line, cell);
    return this;
  }

  public DashboardAssert hasCell(Position position) {
    extractingCell(position);
    return this;
  }

  public DashboardAssert doesntHaveCell(int line, int cell) {
    return doesntHaveCell(Position.of(line, cell));
  }

  public DashboardAssert doesntHaveCell(Position position) {
    assertThat(actual.getDashboardCell(position)).isNull();

    return this;
  }

}
