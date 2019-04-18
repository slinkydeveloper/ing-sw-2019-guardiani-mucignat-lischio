package com.adrenalinici.adrenaline.testutil;

import com.adrenalinici.adrenaline.model.Dashboard;
import com.adrenalinici.adrenaline.model.DashboardCell;

public class MyAssertions {

  public static DashboardAssert assertThat(Dashboard actual) {
    return new DashboardAssert(actual);
  }

  public static DashboardCellAssert assertThat(DashboardCell actual) {
    return new DashboardCellAssert(actual);
  }

}
