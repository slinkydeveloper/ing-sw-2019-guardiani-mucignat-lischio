package com.adrenalinici.adrenaline.testutil;

import com.adrenalinici.adrenaline.model.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class MyAssertions {

  @Deprecated
  public static <T> void assertAbsent(Optional<T> optional) {
    assertFalse(optional.isPresent());
  }

  @Deprecated
  public static <T> void assertPresent(Optional<T> optional) {
    assertTrue(optional.isPresent());
  }

  @Deprecated
  public static void assertInstanceOf(Class<?> expected, Object actual) {
    assertEquals(expected, actual.getClass());
  }

  @Deprecated
  public static <T> void assertListEqualsWithoutOrdering(List<T> expected, List<T> actual) {
    assertTrue(actual.containsAll(expected));
    assertTrue(expected.containsAll(actual));
    assertEquals(expected.size(), actual.size());
  }

  @Deprecated
  public static <T> void assertContainsOne(T expected, List<T> list) {
    assertContainsExactlySatisfying(Predicate.isEqual(expected), 1, list);
  }

  @Deprecated
  public static <T> void assertContainsOneSatisfying(Predicate<T> predicate, List<T> list) {
    assertContainsExactlySatisfying(predicate, 1, list);
  }

  @Deprecated
  public static <T> void assertContainsExactly(T expected, int numberOfOccourences, List<T> list) {
    assertContainsExactlySatisfying(Predicate.isEqual(expected), numberOfOccourences, list);
  }

  @Deprecated
  public static <T> void assertContainsExactlySatisfying(Predicate<T> predicate, int numberOfOccourences, List<T> list) {
    long count = list.stream().filter(predicate).count();
    assertEquals(numberOfOccourences, count);
  }

  @Deprecated
  public static <T extends Throwable> void assertThatCodeThrowsExceptionOfType(Runnable code, Class<T> exceptionType) {
    try {
      code.run();
    } catch (Throwable e) {
      assertEquals(exceptionType, e.getClass());
      return;
    }
    fail("Exception of type " + exceptionType.getName() + " must be thrown");
  }

  @Deprecated
  public static void assertDashboardContainsCell(Dashboard dashboard, int line, int cell, Class<?> type) {
    Optional<DashboardCell> dashboardCell = dashboard.getDashboardCell(Position.of(line, cell));
    assertPresent(dashboardCell);
    assertInstanceOf(type, dashboardCell.get());
  }

  @Deprecated
  public static void assertDashboardContainsCell(Dashboard dashboard, int line, int cell, Class<?> type, DashboardCellBoundType north, DashboardCellBoundType east, DashboardCellBoundType south, DashboardCellBoundType west) {
    Optional<DashboardCell> dashboardCell = dashboard.getDashboardCell(Position.of(line, cell));
    assertPresent(dashboardCell);
    assertInstanceOf(type, dashboardCell.get());
    assertDashboardCellWalls(dashboardCell.get(), north, east, south, west);
  }

  @Deprecated
  public static void assertDashboardContainsRespawnCell(Dashboard dashboard, int line, int cell, DashboardCellBoundType north, DashboardCellBoundType east, DashboardCellBoundType south, DashboardCellBoundType west) {
    assertDashboardContainsCell(dashboard, line, cell, RespawnDashboardCell.class, north, east, south, west);
  }

  @Deprecated
  public static void assertDashboardContainsPickupCell(Dashboard dashboard, int line, int cell, DashboardCellBoundType north, DashboardCellBoundType east, DashboardCellBoundType south, DashboardCellBoundType west) {
    assertDashboardContainsCell(dashboard, line, cell, PickupDashboardCell.class, north, east, south, west);
  }

  @Deprecated
  public static void assertDashboardCellWalls(DashboardCell cell, DashboardCellBoundType north, DashboardCellBoundType east, DashboardCellBoundType south, DashboardCellBoundType west) {
    assertEquals(north, cell.getNorthDashboardCellBoundType());
    assertEquals(east, cell.getEastDashboardCellBoundType());
    assertEquals(south, cell.getSouthDashboardCellBoundType());
    assertEquals(west, cell.getWestDashboardCellBoundType());
  }

  public static DashboardAssert assertThat(Dashboard actual) {
    return new DashboardAssert(actual);
  }

  public static DashboardCellAssert assertThat(DashboardCell actual) {
    return new DashboardCellAssert(actual);
  }

}
