package com.adrenalinici.adrenaline.model;

import org.junit.Test;

import static com.adrenalinici.adrenaline.model.DashboardCellBoundType.*;
import static com.adrenalinici.adrenaline.model.MyAssertions.*;

public class DashboardTest {

  @Test
  public void testBuilder() {
    Dashboard d = Dashboard.newBuilder()
      .newEastCell(c ->
        c.setEastType(DashboardCellBoundType.DOOR).newPickupCell()
      )
      .newSouthLine()
      .newEastCell(c ->
        c.setWestType(DashboardCellBoundType.DOOR).newRespawnCell()
      ).newSouthLine()
      .newEmptyCell().build();
    assertDashboardContainsCell(d, 0, 0, PickupDashboardCell.class);
    assertDashboardContainsCell(d, 1, 0, RespawnDashboardCell.class);
    assertAbsent(d.getDashboardCell(0, 1));
    assertAbsent(d.getDashboardCell(1, 1));
    assertAbsent(d.getDashboardCell(2, 0));
  }

  @Test
  public void testBuilder3x3() {
    Dashboard d = Dashboard.newBuilder()
      .newEastCell(c ->
        c.setEastType(OPEN).setSouthType(DOOR).newRespawnCell()
      )
      .newEastCell(c ->
        c.setWestType(OPEN).setEastType(DOOR).newPickupCell()
      )
      .newEastCell(c ->
        c.setWestType(DOOR).setSouthType(OPEN).newPickupCell()
      )
      .newSouthLine()
      .newEastCell(c ->
        c.setNorthType(DOOR).setEastType(OPEN).setSouthType(DOOR).newPickupCell()
      )
      .newEastCell(c ->
        c.setWestType(OPEN).setEastType(DOOR).newPickupCell()
      )
      .newEastCell(c ->
        c.setNorthType(OPEN).setSouthType(DOOR).setWestType(DOOR).newRespawnCell()
      )
      .newSouthLine()
      .newEastCell(c ->
        c.setNorthType(DOOR).setEastType(OPEN).newRespawnCell()
      )
      .newEastCell(c ->
        c.setEastType(OPEN).setWestType(OPEN).newPickupCell()
      )
      .newEastCell(c ->
        c.setNorthType(DOOR).setWestType(OPEN).newPickupCell()
      ).build();

    assertDashboardContainsRespawnCell(d, 0, 0, WALL, OPEN, DOOR, WALL);
    assertDashboardContainsPickupCell(d, 0, 1, WALL, DOOR, WALL, OPEN);
    assertDashboardContainsPickupCell(d, 0, 2, WALL, WALL, OPEN, DOOR);
    assertDashboardContainsPickupCell(d, 1, 0, DOOR, OPEN, DOOR, WALL);
    assertDashboardContainsPickupCell(d, 1, 1, WALL, DOOR, WALL, OPEN);
    assertDashboardContainsRespawnCell(d, 1, 2, OPEN, WALL, DOOR, DOOR);
    assertDashboardContainsRespawnCell(d, 2, 0, DOOR, OPEN, WALL, WALL);
    assertDashboardContainsPickupCell(d, 2, 1, WALL, OPEN, WALL, OPEN);
    assertDashboardContainsPickupCell(d, 2, 2, DOOR, WALL, WALL, OPEN);

    DashboardCell centerCell = d.getDashboardCell(1, 1).get();

    assertPresent(centerCell.getNorthDashboardCell());
    assertInstanceOf(PickupDashboardCell.class, centerCell.getNorthDashboardCell().get());
    assertDashboardCellWalls(centerCell.getNorthDashboardCell().get(), WALL, DOOR, WALL, OPEN);

    assertPresent(centerCell.getEastDashboardCell());
    assertInstanceOf(RespawnDashboardCell.class, centerCell.getEastDashboardCell().get());
    assertDashboardCellWalls(centerCell.getEastDashboardCell().get(), OPEN, WALL, DOOR, DOOR);

    assertPresent(centerCell.getSouthDashboardCell());
    assertInstanceOf(PickupDashboardCell.class, centerCell.getSouthDashboardCell().get());
    assertDashboardCellWalls(centerCell.getSouthDashboardCell().get(), WALL, OPEN, WALL, OPEN);

    assertPresent(centerCell.getWestDashboardCell());
    assertInstanceOf(PickupDashboardCell.class, centerCell.getWestDashboardCell().get());
    assertDashboardCellWalls(centerCell.getWestDashboardCell().get(), DOOR, OPEN, DOOR, WALL);

  }


}
