package com.adrenalinici.adrenaline.model;

import com.adrenalinici.adrenaline.testutil.TestUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.adrenalinici.adrenaline.model.DashboardCellBoundType.*;
import static com.adrenalinici.adrenaline.testutil.MyAssertions.*;
import static org.junit.Assert.assertEquals;

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
    assertAbsent(d.getDashboardCell(Position.of(0, 1)));
    assertAbsent(d.getDashboardCell(Position.of(1, 1)));
    assertAbsent(d.getDashboardCell(Position.of(2, 0)));
  }

  @Test
  public void testBuilder3x3() {
    Dashboard d = TestUtils.build3x3Dashboard();

    assertDashboardContainsRespawnCell(d, 0, 0, WALL, OPEN, DOOR, WALL);
    assertDashboardContainsPickupCell(d, 0, 1, WALL, DOOR, WALL, OPEN);
    assertDashboardContainsPickupCell(d, 0, 2, WALL, WALL, OPEN, DOOR);
    assertDashboardContainsPickupCell(d, 1, 0, DOOR, OPEN, DOOR, WALL);
    assertDashboardContainsPickupCell(d, 1, 1, WALL, DOOR, WALL, OPEN);
    assertDashboardContainsRespawnCell(d, 1, 2, OPEN, WALL, DOOR, DOOR);
    assertDashboardContainsRespawnCell(d, 2, 0, DOOR, OPEN, WALL, WALL);
    assertDashboardContainsPickupCell(d, 2, 1, WALL, OPEN, WALL, OPEN);
    assertDashboardContainsPickupCell(d, 2, 2, DOOR, WALL, WALL, OPEN);

    DashboardCell centerCell = d.getDashboardCell(Position.of(1, 1)).get();

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

  @Test
  public void testCalculateMovements() {
    Dashboard d = TestUtils.build3x3Dashboard();

    List<Position> calculated = d.calculateMovements(Position.of(1, 1), 2);
    assertListEqualsWithoutOrdering(
      Arrays.asList(
        new Position(1, 1),
        new Position(0, 0),
        new Position(1, 0),
        new Position(2, 0),
        new Position(0, 2),
        new Position(1, 2),
        new Position(2, 2)
      ), calculated
    );
  }

  @Test
  public void testGetPlayersPositions() {
    Dashboard d = TestUtils.build3x3Dashboard();

    d.getDashboardCell(Position.of(1, 1)).get().addPlayer(PlayerColor.CYAN);
    d.getDashboardCell(Position.of(1, 1)).get().addPlayer(PlayerColor.GRAY);
    d.getDashboardCell(Position.of(2, 0)).get().addPlayer(PlayerColor.GREEN);

    Map<PlayerColor, Position> players = d.getPlayersPositions();
    assertEquals(new Position(1, 1), players.get(PlayerColor.CYAN));
    assertEquals(new Position(1, 1), players.get(PlayerColor.GRAY));
    assertEquals(new Position(2, 0), players.get(PlayerColor.GREEN));
  }

}
