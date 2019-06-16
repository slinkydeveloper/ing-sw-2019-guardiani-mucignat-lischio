package com.adrenalinici.adrenaline.server.testutil;

import com.adrenalinici.adrenaline.server.model.Dashboard;
import com.adrenalinici.adrenaline.server.model.DashboardCell;

public class MyAssertions {

  public static DashboardAssert assertThat(Dashboard actual) {
    return new DashboardAssert(actual);
  }

  public static DashboardCellAssert assertThat(DashboardCell actual) {
    return new DashboardCellAssert(actual);
  }

}
