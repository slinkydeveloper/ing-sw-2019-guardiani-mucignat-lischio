package com.adrenalinici.adrenaline.model;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class MyAssertions {

    public static <T> void assertAbsent(Optional<T> optional) {
        assertFalse(optional.isPresent());
    }

    public static <T> void assertPresent(Optional<T> optional) {
        assertTrue(optional.isPresent());
    }

    public static void assertInstanceOf(Class<?> expected, Object actual) {
        assertEquals(expected, actual.getClass());
    }

    public static <T> void assertListEqualsWithoutOrdering(List<T> expected, List<T> actual) {
      assertTrue(expected.containsAll(actual));
      assertTrue(actual.containsAll(expected));
    }

    public static <T> void assertContainsExactly(T expectedColor, int numberOfOccourences, List<T> colors) {
        long count = colors.stream().filter(expectedColor::equals).count();
        assertEquals(numberOfOccourences, count);
    }

    public static <T extends Throwable> void assertThatCodeThrowsExceptionOfType(Runnable code, Class<T> exceptionType) {
        try {
            code.run();
        } catch (Throwable e) {
            assertEquals(exceptionType, e.getClass());
            return;
        }
        fail("Exception of type " + exceptionType.getName() + " must be thrown");
    }


  public static void assertDashboardContainsCell(Dashboard dashboard, int line, int cell, Class<?> type) {
    Optional<DashboardCell> dashboardCell = dashboard.getDashboardCell(line, cell);
    assertPresent(dashboardCell);
    assertInstanceOf(type, dashboardCell.get());
  }

  public static void assertDashboardContainsCell(Dashboard dashboard, int line, int cell, Class<?> type, DashboardCellBoundType north, DashboardCellBoundType east, DashboardCellBoundType south, DashboardCellBoundType west) {
    Optional<DashboardCell> dashboardCell = dashboard.getDashboardCell(line, cell);
    assertPresent(dashboardCell);
    assertInstanceOf(type, dashboardCell.get());
    assertDashboardCellWalls(dashboardCell.get(), north, east, south, west);
  }

  public static void assertDashboardContainsRespawnCell(Dashboard dashboard, int line, int cell, DashboardCellBoundType north, DashboardCellBoundType east, DashboardCellBoundType south, DashboardCellBoundType west) {
    assertDashboardContainsCell(dashboard, line, cell, RespawnDashboardCell.class, north, east, south, west);
  }

  public static void assertDashboardContainsPickupCell(Dashboard dashboard, int line, int cell, DashboardCellBoundType north, DashboardCellBoundType east, DashboardCellBoundType south, DashboardCellBoundType west) {
    assertDashboardContainsCell(dashboard, line, cell, PickupDashboardCell.class, north, east, south, west);
  }

  public static void assertDashboardCellWalls(DashboardCell cell, DashboardCellBoundType north, DashboardCellBoundType east, DashboardCellBoundType south, DashboardCellBoundType west) {
    assertEquals(north, cell.getNorthDashboardCellBoundType());
    assertEquals(east, cell.getEastDashboardCellBoundType());
    assertEquals(south, cell.getSouthDashboardCellBoundType());
    assertEquals(west, cell.getWestDashboardCellBoundType());
  }

}
