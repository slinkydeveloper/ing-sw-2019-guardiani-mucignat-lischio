package com.adrenalinici.adrenaline.server.model;

import com.adrenalinici.adrenaline.common.model.DashboardCellBoundType;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.server.testutil.TestUtils;
import org.junit.Test;

import java.util.Map;

import static com.adrenalinici.adrenaline.common.model.DashboardCellBoundType.*;
import static com.adrenalinici.adrenaline.server.testutil.MyAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

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

    assertThat(d)
      .extractingCell(0, 0)
      .isPickupCell();
    assertThat(d)
      .extractingCell(1,  0)
      .isRespawnCell();
    assertThat(d)
      .doesntHaveCell(0, 1)
      .doesntHaveCell(1, 1)
      .doesntHaveCell(2, 0);
  }

  @Test
  public void testBuilder3x3() {
    Dashboard d = TestUtils.build3x3Dashboard();

    assertThat(d)
      .extractingCell(0,  0)
      .isRespawnCell()
      .hasWalls(WALL, OPEN, DOOR, WALL);
    assertThat(d)
      .extractingCell(0,  1)
      .isPickupCell()
      .hasWalls(WALL, DOOR, WALL, OPEN);
    assertThat(d)
      .extractingCell(0, 2)
      .isPickupCell()
      .hasWalls(WALL, WALL, OPEN, DOOR);



    assertThat(d).extractingCell(1, 0).isPickupCell().hasWalls(DOOR, OPEN, DOOR, WALL);
    assertThat(d).extractingCell(1, 1).isPickupCell().hasWalls(WALL, DOOR, WALL, OPEN);
    assertThat(d).extractingCell(1, 2).isRespawnCell().hasWalls(OPEN, WALL, DOOR, DOOR);
    assertThat(d).extractingCell(2, 0).isRespawnCell().hasWalls(DOOR, OPEN, WALL, WALL);
    assertThat(d).extractingCell(2, 1).isPickupCell().hasWalls(WALL, OPEN, WALL, OPEN);
    assertThat(d).extractingCell(2, 2).isPickupCell().hasWalls(DOOR, WALL, WALL, OPEN);

    DashboardCell centerCell = d.getDashboardCell(Position.of(1, 1));

    assertThat(centerCell)
      .extractingNorthCell()
      .isPickupCell()
      .hasWalls(WALL, DOOR, WALL, OPEN);
    assertThat(centerCell)
      .extractingEastCell()
      .isRespawnCell()
      .hasWalls(OPEN, WALL, DOOR, DOOR);
    assertThat(centerCell)
      .extractingSouthCell()
      .isPickupCell()
      .hasWalls(WALL, OPEN, WALL, OPEN);
    assertThat(centerCell)
      .extractingWestCell()
      .isPickupCell()
      .hasWalls(DOOR, OPEN, DOOR, WALL);
  }

  @Test
  public void testCalculateMovements() {
    Dashboard d = TestUtils.build3x3Dashboard();

    assertThat(d.calculateMovements(Position.of(1, 1), 2))
      .containsOnly(
        new Position(1, 1),
        new Position(0, 0),
        new Position(1, 0),
        new Position(2, 0),
        new Position(0, 2),
        new Position(1, 2),
        new Position(2, 2)
      );

    assertThat(d.calculateMovements(Position.of(0, 0), 1))
      .containsOnly(
        new Position(0, 0),
        new Position(0, 1),
        new Position(1, 0)
      );
  }

  @Test
  public void testCalculateMovementsInOneDirection() {
    Dashboard d = TestUtils.build3x3Dashboard();

    assertThat(d.calculateMovementsInOneDirection(Position.of(0, 0), 2))
      .containsOnly(
        new Position(0, 0),
        new Position(0, 1),
        new Position(0, 2),
        new Position(1, 0),
        new Position(2, 0)
      );

    assertThat(d.calculateMovementsInOneDirection(Position.of(1, 1), 2))
      .containsOnly(
        new Position(1, 0),
        new Position(1, 1),
        new Position(1, 2)
      );
  }

  @Test
  public void testGetPlayersPositions() {
    Dashboard d = TestUtils.build3x3Dashboard();

    d.getDashboardCell(Position.of(1, 1)).addPlayer(PlayerColor.CYAN);
    d.getDashboardCell(Position.of(1, 1)).addPlayer(PlayerColor.GRAY);
    d.getDashboardCell(Position.of(2, 0)).addPlayer(PlayerColor.GREEN);

    Map<PlayerColor, Position> players = d.getPlayersPositions();

    assertThat(players)
      .containsOnly(
        entry(PlayerColor.CYAN, Position.of(1, 1)),
        entry(PlayerColor.GRAY, Position.of(1, 1)),
        entry(PlayerColor.GREEN, Position.of(2, 0))
      );
  }

  @Test
  public void testCalculateIfVisible() {
    Dashboard d = TestUtils.build3x3Dashboard();

    assertThat(d.calculateIfVisible(Position.of(0, 0), Position.of(0, 1))).isTrue();
    assertThat(d.calculateIfVisible(Position.of(0, 0), Position.of(1, 1))).isTrue();
    assertThat(d.calculateIfVisible(Position.of(0, 0), Position.of(0, 2))).isFalse();
    assertThat(d.calculateIfVisible(Position.of(1, 0), Position.of(0, 0))).isTrue();
    assertThat(d.calculateIfVisible(Position.of(1, 0), Position.of(0, 1))).isTrue();
    assertThat(d.calculateIfVisible(Position.of(1, 0), Position.of(2, 2))).isTrue();
    assertThat(d.calculateIfVisible(Position.of(1, 0), Position.of(1, 2))).isFalse();
    assertThat(d.calculateIfVisible(Position.of(1, 2), Position.of(2, 0))).isTrue();
    assertThat(d.calculateIfVisible(Position.of(2, 1), Position.of(2, 0))).isTrue();
    assertThat(d.calculateIfVisible(Position.of(2, 1), Position.of(2, 0))).isTrue();
    assertThat(d.calculateIfVisible(Position.of(2, 1), Position.of(1, 2))).isFalse();
    assertThat(d.calculateIfVisible(Position.of(2, 2), Position.of(0, 2))).isTrue();


  }

  @Test
  public void testCalculateDistance() {
    Dashboard d = TestUtils.build3x3DashboardWithWalls();

    assertThat(d.calculateDistance(Position.of(0, 2), Position.of(2, 0))).isEqualTo(4);
    assertThat(d.calculateDistance(Position.of(0, 1), Position.of(0, 2))).isEqualTo(5);
    assertThat(d.calculateDistance(Position.of(0, 0), Position.of(0, 2))).isEqualTo(6);
    assertThat(d.calculateDistance(Position.of(1, 0), Position.of(1, 1))).isEqualTo(1);
    assertThat(d.calculateDistance(Position.of(0, 2), Position.of(0, 2))).isEqualTo(0);
  }

}
