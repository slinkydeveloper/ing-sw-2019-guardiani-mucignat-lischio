package com.adrenalinici.adrenaline.testutil;

import com.adrenalinici.adrenaline.model.DashboardCell;
import com.adrenalinici.adrenaline.model.DashboardCellBoundType;
import com.adrenalinici.adrenaline.model.PickupDashboardCell;
import com.adrenalinici.adrenaline.model.RespawnDashboardCell;
import org.assertj.core.api.AbstractAssert;

import static org.assertj.core.api.Assertions.assertThat;

public class DashboardCellAssert extends AbstractAssert<DashboardCellAssert, DashboardCell> {
  public DashboardCellAssert(DashboardCell dashboardCell) {
    super(dashboardCell, DashboardCellAssert.class);
  }

  public DashboardCellAssert isRespawnCell() {
    assertThat(actual).isInstanceOf(RespawnDashboardCell.class);
    return this;
  }

  public DashboardCellAssert isPickupCell() {
    assertThat(actual).isInstanceOf(PickupDashboardCell.class);
    return this;
  }

  public DashboardCellAssert extractingNorthCell() {
    return new DashboardCellAssert(actual.getNorthDashboardCell()).isNotNull();
  }

  public DashboardCellAssert extractingEastCell() {
    return new DashboardCellAssert(actual.getEastDashboardCell()).isNotNull();
  }

  public DashboardCellAssert extractingSouthCell() {
    return new DashboardCellAssert(actual.getSouthDashboardCell()).isNotNull();
  }

  public DashboardCellAssert extractingWestCell() {
    return new DashboardCellAssert(actual.getWestDashboardCell()).isNotNull();
  }

  public DashboardCellAssert hasNorthWall(DashboardCellBoundType bound) {
    assertThat(actual.getNorthDashboardCellBoundType()).isEqualTo(bound);
    return this;
  }

  public DashboardCellAssert hasEastWall(DashboardCellBoundType bound) {
    assertThat(actual.getEastDashboardCellBoundType()).isEqualTo(bound);
    return this;
  }

  public DashboardCellAssert hasSouthWall(DashboardCellBoundType bound) {
    assertThat(actual.getSouthDashboardCellBoundType()).isEqualTo(bound);
    return this;
  }

  public DashboardCellAssert hasWestWall(DashboardCellBoundType bound) {
    assertThat(actual.getWestDashboardCellBoundType()).isEqualTo(bound);
    return this;
  }
  public DashboardCellAssert hasWalls(DashboardCellBoundType north, DashboardCellBoundType east, DashboardCellBoundType south, DashboardCellBoundType west) {
    hasNorthWall(north);
    hasEastWall(east);
    hasSouthWall(south);
    hasWestWall(west);
    return this;
  }
}
