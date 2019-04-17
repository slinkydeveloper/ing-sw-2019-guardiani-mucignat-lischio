package com.adrenalinici.adrenaline.testutil;

import com.adrenalinici.adrenaline.model.Dashboard;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.model.PlayerDashboard;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.adrenalinici.adrenaline.model.DashboardCellBoundType.DOOR;
import static com.adrenalinici.adrenaline.model.DashboardCellBoundType.OPEN;

public class TestUtils {

  public static Dashboard build3x3Dashboard() {
    return Dashboard.newBuilder()
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
        c.setEastType(OPEN).setWestType(OPEN).setNorthType(OPEN).newPickupCell()
      )
      .newEastCell(c ->
        c.setNorthType(DOOR).setWestType(OPEN).newPickupCell()
      ).build();
  }

  public static List<PlayerDashboard> generate3PlayerDashboards() {
    return Arrays.asList(
      new PlayerDashboard(PlayerColor.GREEN, true, Collections.emptyList()),
      new PlayerDashboard(PlayerColor.GRAY, false, Collections.emptyList()),
      new PlayerDashboard(PlayerColor.YELLOW, false, Collections.emptyList())
    );
  }

}
