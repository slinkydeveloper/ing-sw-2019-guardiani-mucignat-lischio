package com.adrenalinici.adrenaline.testutil;

import com.adrenalinici.adrenaline.model.Dashboard;
import com.adrenalinici.adrenaline.model.DashboardCell;
import com.adrenalinici.adrenaline.model.Position;
import org.assertj.core.api.AbstractAssert;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class DashboardAssert extends AbstractAssert<DashboardAssert, Dashboard> {

  public DashboardAssert(Dashboard dashboard) {
    super(dashboard, DashboardAssert.class);
  }

  public DashboardCellAssert extractingCell(int line, int cell) {
    return this.extractingCell(Position.of(line, cell));
  }

  public DashboardCellAssert extractingCell(Position position) {
    Optional<DashboardCell> cell = actual.getDashboardCell(position);

    assertThat(cell)
      .isPresent();

    return new DashboardCellAssert(cell.get());
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
    assertThat(actual.getDashboardCell(position)).isNotPresent();

    return this;
  }

}
