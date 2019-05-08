package com.adrenalinici.adrenaline.testutil;

import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.controller.GunFactory;
import com.adrenalinici.adrenaline.controller.guns.BaseEffectGunFactory;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.fat.Dashboard;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.model.fat.PlayerDashboard;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.adrenalinici.adrenaline.model.fat.DashboardCellBoundType.*;

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
        c.setEastType(OPEN).setWestType(OPEN).newPickupCell()
      )
      .newEastCell(c ->
        c.setNorthType(DOOR).setWestType(OPEN).newPickupCell()
      ).build();
  }

  public static Dashboard build3x3DashboardWithWalls() {
    return Dashboard.newBuilder()
      .newEastCell(c ->
        c.setEastType(DOOR).setSouthType(OPEN).newRespawnCell()
      )
      .newEastCell(c ->
        c.setSouthType(OPEN).setWestType(DOOR).newPickupCell()
      )
      .newEastCell(c ->
        c.setSouthType(OPEN).newPickupCell()
      )
      .newSouthLine()
      .newEastCell(c ->
        c.setNorthType(OPEN).setEastType(OPEN).newPickupCell()
      )
      .newEastCell(c ->
        c.setNorthType(OPEN).setEastType(WALL).setSouthType(OPEN).setWestType(OPEN).newPickupCell()
      )
      .newEastCell(c ->
        c.setNorthType(OPEN).setSouthType(DOOR).newRespawnCell()
      )
      .newSouthLine()
      .newEastCell(c ->
        c.setEastType(OPEN).newRespawnCell()
      )
      .newEastCell(c ->
        c.setNorthType(OPEN).setEastType(OPEN).setWestType(OPEN).newPickupCell()
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

  public static List<PlayerDashboard> generate4PlayerDashboards() {
    return Arrays.asList(
      new PlayerDashboard(PlayerColor.GREEN, true, Collections.emptyList()),
      new PlayerDashboard(PlayerColor.GRAY, false, Collections.emptyList()),
      new PlayerDashboard(PlayerColor.YELLOW, false, Collections.emptyList()),
      new PlayerDashboard(PlayerColor.CYAN, false, Collections.emptyList())
    );
  }

  public static GameModel generateModel() {
    return new GameModel(8, build3x3Dashboard(), generate3PlayerDashboards());
  }

}
