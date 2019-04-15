package com.adrenalinici.adrenaline.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static com.adrenalinici.adrenaline.model.DashboardCellBoundType.DOOR;
import static com.adrenalinici.adrenaline.model.DashboardCellBoundType.OPEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class GameStatusTest {
  @Test
  public void addKillScoreTest() {
    GameStatus gameStatus = new GameStatus(8, null, null);
    gameStatus.addKillScore(PlayerColor.CYAN, false);
    assertThat(gameStatus.getKillScore()).hasSize(1);
    assertThat(gameStatus.getKillScore().get(0).getKey()).isEqualTo(PlayerColor.CYAN);
    assertThat(gameStatus.getKillScore().get(0).getValue()).isEqualTo(false);
  }

  @Test
  public void inizializationTest() {
    GameStatus gameStatus = new GameStatus(8, null, null);
    assertThat(gameStatus.getRemainingSkulls()).isEqualTo(8);
    assertThat(gameStatus.getKillScore().isEmpty()).isTrue();
    assertThat(gameStatus.getDoubleKillScore().isEmpty()).isTrue();
  }

  @Test
  public void calculateAvailableGunsToPickupTest() {
    //devo testare caso limite in cui magari la gun richiede due RED per pickupare e io ne ho solo uno
    BaseGun gun1 = new BaseEffectGun(
      AmmoColor.BLUE,
      Arrays.asList(AmmoColor.RED, AmmoColor.RED, AmmoColor.BLUE),
      "Sword", "terrible sword", null, null, Collections.emptyList(),
      null, Collections.emptyList()
    );
    BaseGun gun2 = new BaseEffectGun(
      AmmoColor.BLUE,
      Arrays.asList(AmmoColor.RED, AmmoColor.BLUE, AmmoColor.YELLOW),
      "Revolver", "terrible revolver", null, null, Collections.emptyList(),
      null, Collections.emptyList()
    );
    BaseGun gun3 = new BaseEffectGun(
      AmmoColor.BLUE,
      Arrays.asList(AmmoColor.RED, AmmoColor.YELLOW, AmmoColor.YELLOW),
      "Rifle", "terrible rifle", null, null, Collections.emptyList(),
      null, Collections.emptyList()
    );
    Dashboard dashboard = Dashboard.newBuilder().build();
    RespawnDashboardCell respawnDashboardCell = new RespawnDashboardCell(OPEN, OPEN, OPEN, OPEN, 0, 0, dashboard);
    respawnDashboardCell.getAvailableGuns().add(gun1);
    respawnDashboardCell.getAvailableGuns().add(gun2);
    respawnDashboardCell.getAvailableGuns().add(gun3);
    List<PowerUpCard> powerUpCards = Arrays.asList(new PowerUpCard(AmmoColor.RED, PowerUpType.KINETIC_RAY), new PowerUpCard(AmmoColor.BLUE, PowerUpType.SCOPE));
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.YELLOW, false, powerUpCards);
    List<PlayerDashboard> playerDashboardList = Arrays.asList(playerDashboard);
    GameStatus gameStatus = new GameStatus(8, dashboard, playerDashboardList);
    assertThat(gameStatus.calculateAvailableGunsToPickup(respawnDashboardCell, PlayerColor.YELLOW)).contains(gun1);
    assertThat(gameStatus.calculateAvailableGunsToPickup(respawnDashboardCell, PlayerColor.YELLOW)).contains(gun2);
    assertThat(gameStatus.calculateAvailableGunsToPickup(respawnDashboardCell, PlayerColor.YELLOW)).doesNotContain(gun3);
  }

  @Test
  public void acquireAmmoCardTest() {
    Dashboard dashboard = Dashboard.newBuilder().build();
    PowerUpCard powerUpCard = new PowerUpCard(AmmoColor.YELLOW, PowerUpType.KINETIC_RAY);
    AmmoCard ammoCard = new AmmoCard(Arrays.asList(AmmoColor.RED, AmmoColor.YELLOW), powerUpCard);
    PickupDashboardCell pickupDashboardCell = new PickupDashboardCell(OPEN, OPEN, OPEN, OPEN, 0, 0, dashboard);
    pickupDashboardCell.setAmmoCard(ammoCard);
    List<PowerUpCard> powerUpCards = Arrays.asList(new PowerUpCard(AmmoColor.RED, PowerUpType.KINETIC_RAY), new PowerUpCard(AmmoColor.BLUE, PowerUpType.SCOPE));
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.YELLOW, false, powerUpCards);
    List<PlayerDashboard> playerDashboardList = Arrays.asList(playerDashboard);
    GameStatus gameStatus = new GameStatus(8, dashboard, playerDashboardList);
    gameStatus.acquireAmmoCard(pickupDashboardCell, PlayerColor.YELLOW);
    assertThat(pickupDashboardCell.getAmmoCard() == null);
    //TODO
  }

}
