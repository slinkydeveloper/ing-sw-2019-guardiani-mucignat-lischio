package com.adrenalinici.adrenaline.model;

import com.adrenalinici.adrenaline.model.event.GameModelUpdatedEvent;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.testutil.TestUtils;
import org.junit.Test;

import java.util.*;

import static com.adrenalinici.adrenaline.model.DashboardCellBoundType.OPEN;
import static com.adrenalinici.adrenaline.testutil.MyConditions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class GameModelTest {
  @Test
  public void addKillScoreTest() {
    GameModel gameModel = new GameModel(8, null, null);
    gameModel.addKillScore(PlayerColor.CYAN, false);
    assertThat(gameModel.getKillScore()).hasSize(1);
    assertThat(gameModel.getKillScore().get(0).getKey()).isEqualTo(PlayerColor.CYAN);
    assertThat(gameModel.getKillScore().get(0).getValue()).isEqualTo(false);
  }

  @Test
  public void inizializationTest() {
    GameModel gameModel = new GameModel(8, null, null);
    assertThat(gameModel.getRemainingSkulls()).isEqualTo(8);
    assertThat(gameModel.getKillScore().isEmpty()).isTrue();
    assertThat(gameModel.getDoubleKillScore().isEmpty()).isTrue();
  }

  @Test
  public void acquireGunUsingOnlyAmmosTest() {
    Dashboard dashboard = Dashboard.newBuilder().build();
    PowerUpCard blueKineticRay = new PowerUpCard(AmmoColor.BLUE, PowerUpType.KINETIC_RAY);
    PowerUpCard redTeleport = new PowerUpCard(AmmoColor.RED, PowerUpType.TELEPORT);
    List<PowerUpCard> powerUpCards = Arrays.asList(blueKineticRay, redTeleport);
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.GREEN, false, powerUpCards);
    RespawnDashboardCell respawnDashboardCell = new RespawnDashboardCell(OPEN, OPEN, OPEN, OPEN, 0, 0, dashboard);
    BaseGun gun1 = new BaseEffectGun(
      "sword",
      AmmoColor.BLUE,
      Arrays.asList(AmmoColor.RED, AmmoColor.YELLOW),
      "Sword", "terrible sword", null, null, Collections.emptyList(),
      null, Collections.emptyList()
    );
    BaseGun gun2 = new BaseEffectGun(
      "revolver",
      AmmoColor.BLUE,
      Arrays.asList(AmmoColor.BLUE),
      "Revolver", "terrible revolver", null, null, Collections.emptyList(),
      null, Collections.emptyList()
    );
    respawnDashboardCell.addAvailableGun(gun1);
    respawnDashboardCell.addAvailableGun(gun2);
    List<PlayerDashboard> playerDashboardList = Collections.singletonList(playerDashboard);
    GameModel gameModel = new GameModel(8, dashboard, playerDashboardList);
    gameModel.acquireGun(PlayerColor.GREEN, gun1);
    assertThat(playerDashboard.getAmmos()).containsOnly(AmmoColor.BLUE);
    assertThat(playerDashboard.getPowerUpCards()).containsExactlyInAnyOrder(blueKineticRay, redTeleport);
    gameModel.acquireGun(PlayerColor.GREEN, gun2);
    assertThat(playerDashboard.getAmmos().isEmpty()).isTrue();
    assertThat(playerDashboard.getPowerUpCards()).containsExactlyInAnyOrder(blueKineticRay, redTeleport);
  }

  @Test
  public void acquireGunUsingOnlyPowerupsTest() {
    Dashboard dashboard = Dashboard.newBuilder().build();
    PowerUpCard blueKineticRay = new PowerUpCard(AmmoColor.BLUE, PowerUpType.KINETIC_RAY);
    PowerUpCard blueTeleport = new PowerUpCard(AmmoColor.BLUE, PowerUpType.TELEPORT);
    PowerUpCard redTeleport = new PowerUpCard(AmmoColor.RED, PowerUpType.TELEPORT);
    List<PowerUpCard> powerUpCards = Arrays.asList(blueKineticRay, blueTeleport, redTeleport);
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.GREEN, false, powerUpCards);
    playerDashboard.removeAmmos(Arrays.asList(AmmoColor.RED, AmmoColor.YELLOW, AmmoColor.BLUE));
    RespawnDashboardCell respawnDashboardCell = new RespawnDashboardCell(OPEN, OPEN, OPEN, OPEN, 0, 0, dashboard);
    BaseGun gun1 = new BaseEffectGun(
      "sword",
      AmmoColor.BLUE,
      Arrays.asList(AmmoColor.RED),
      "Sword", "terrible sword", null, null, Collections.emptyList(),
      null, Collections.emptyList()
    );
    BaseGun gun2 = new BaseEffectGun(
      "revolver",
      AmmoColor.BLUE,
      Arrays.asList(AmmoColor.BLUE),
      "Revolver", "terrible revolver", null, null, Collections.emptyList(),
      null, Collections.emptyList()
    );
    respawnDashboardCell.addAvailableGun(gun1);
    respawnDashboardCell.addAvailableGun(gun2);
    List<PlayerDashboard> playerDashboardList = Arrays.asList(playerDashboard);
    GameModel gameModel = new GameModel(8, dashboard, playerDashboardList);
    assertThat(playerDashboard.getAmmos().isEmpty()).isTrue();
    gameModel.acquireGun(PlayerColor.GREEN, gun1);
    assertThat(playerDashboard.getPowerUpCards()).containsOnly(blueKineticRay, blueTeleport);
    gameModel.acquireGun(PlayerColor.GREEN, gun2);
    assertThat(playerDashboard.getPowerUpCards()).containsOnly(blueTeleport);
  }

  @Test
  public void acquireGunUsingBothAmmosAndPowerupsTest() {
    Dashboard dashboard = Dashboard.newBuilder().build();
    PowerUpCard blueKineticRay = new PowerUpCard(AmmoColor.BLUE, PowerUpType.KINETIC_RAY);
    PowerUpCard redTeleport = new PowerUpCard(AmmoColor.RED, PowerUpType.TELEPORT);
    List<PowerUpCard> powerUpCards = Arrays.asList(blueKineticRay, redTeleport);
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.GREEN, false, powerUpCards);
    RespawnDashboardCell respawnDashboardCell = new RespawnDashboardCell(OPEN, OPEN, OPEN, OPEN, 0, 0, dashboard);

    BaseGun gun1 = new BaseEffectGun(
      "sword",
      AmmoColor.BLUE,
      Arrays.asList(AmmoColor.RED, AmmoColor.RED, AmmoColor.YELLOW),
      "Sword", "terrible sword", null, null, Collections.emptyList(),
      null, Collections.emptyList()
    );
    BaseGun gun2 = new BaseEffectGun(
      "revolver",
      AmmoColor.BLUE,
      Arrays.asList(AmmoColor.BLUE),
      "Revolver", "terrible revolver", null, null, Collections.emptyList(),
      null, Collections.emptyList()
    );
    respawnDashboardCell.addAvailableGun(gun1);
    respawnDashboardCell.addAvailableGun(gun2);
    List<PlayerDashboard> playerDashboardList = Arrays.asList(playerDashboard);

    GameModel gameModel = new GameModel(8, dashboard, playerDashboardList);
    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    gameModel.registerObserver(receivedModelEvents::add);

    gameModel.acquireGun(PlayerColor.GREEN, gun1);
    assertThat(playerDashboard.getAmmos()).containsOnly(AmmoColor.BLUE);
    assertThat(playerDashboard.getPowerUpCards()).containsOnly(blueKineticRay);

    gameModel.acquireGun(PlayerColor.GREEN, gun2);
    assertThat(playerDashboard.getAmmos().isEmpty()).isTrue();
    assertThat(playerDashboard.getPowerUpCards()).containsOnly(blueKineticRay);

    assertThat(receivedModelEvents).haveExactly(2, isDashboardCellUpdatedEvent(0, 0));
    assertThat(receivedModelEvents).haveExactly(2, isPlayerDashboardUpdateEvent(PlayerColor.GREEN, gameModel));
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
    GameModel gameModel = new GameModel(8, dashboard, playerDashboardList);
    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    gameModel.registerObserver(receivedModelEvents::add);
    gameModel.acquireAmmoCard(pickupDashboardCell, PlayerColor.YELLOW);
    assertThat(pickupDashboardCell.getAmmoCard()).isNotPresent();
    assertThat(playerDashboard.getAmmos())
      .containsExactlyInAnyOrderElementsOf(Arrays.asList(AmmoColor.BLUE, AmmoColor.RED, AmmoColor.RED, AmmoColor.YELLOW, AmmoColor.YELLOW));

    assertThat(playerDashboard.getPowerUpCards())
      .containsExactlyInAnyOrderElementsOf(Arrays.asList(
        new PowerUpCard(AmmoColor.RED, PowerUpType.KINETIC_RAY),
        new PowerUpCard(AmmoColor.BLUE, PowerUpType.SCOPE),
        new PowerUpCard(AmmoColor.YELLOW, PowerUpType.KINETIC_RAY)
      ));
    assertThat(receivedModelEvents).haveExactly(1, isDashboardCellUpdatedEvent(0, 0));
    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW, gameModel));
  }

  @Test
  public void hitPlayerTest() {
    GameModel gameModel = new GameModel(8, null,
      TestUtils.generate3PlayerDashboards());

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    gameModel.registerObserver(receivedModelEvents::add);
    boolean killed = gameModel.hitPlayer(PlayerColor.GREEN, PlayerColor.YELLOW, 10);

    assertThat(killed).isFalse();
    assertThat(gameModel.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .isEqualTo(Collections.nCopies(10, PlayerColor.GREEN));

    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW, gameModel));
    assertThat(gameModel.getRemainingSkulls()).isEqualTo(8);
    assertThat(gameModel.getKillScore().size()).isEqualTo(0);
  }

  @Test
  public void hitPlayerWithKillDamageTest() {
    GameModel gameModel = new GameModel(8, null,
      TestUtils.generate3PlayerDashboards());

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    gameModel.registerObserver(receivedModelEvents::add);
    boolean killed = gameModel.hitPlayer(PlayerColor.GREEN, PlayerColor.YELLOW, 9);

    assertThat(killed).isFalse();
    assertThat(gameModel.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .isEqualTo(Collections.nCopies(9, PlayerColor.GREEN));

    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW, gameModel));

    receivedModelEvents = new ArrayList<>();
    gameModel.registerObserver(receivedModelEvents::add);
    killed = gameModel.hitPlayer(PlayerColor.GREEN, PlayerColor.YELLOW, 2);

    assertThat(killed).isTrue();
    assertThat(gameModel.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .isEqualTo(Collections.nCopies(11, PlayerColor.GREEN));

    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW, gameModel));
    assertThat(receivedModelEvents).haveExactly(1, isGameModelUpdatedEvent(gameModel));
    assertThat(gameModel.getRemainingSkulls()).isEqualTo(7);
    assertThat(gameModel.getKillScore().get(0)).isEqualTo(new AbstractMap.SimpleEntry<>(PlayerColor.GREEN, Boolean.FALSE));
  }

  @Test
  public void hitPlayerWithCruelDamageAndMarksTest() {
    GameModel gameModel = new GameModel(8, null,
      TestUtils.generate3PlayerDashboards());
    gameModel.getPlayerDashboard(PlayerColor.YELLOW).addMarks(Arrays.asList(PlayerColor.GREEN, PlayerColor.GREEN));

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    gameModel.registerObserver(receivedModelEvents::add);
    boolean killed = gameModel.hitPlayer(PlayerColor.GREEN, PlayerColor.YELLOW, 11);

    assertThat(killed).isTrue();
    assertThat(gameModel.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .isEqualTo(Collections.nCopies(12, PlayerColor.GREEN));

    assertThat(gameModel.getPlayerDashboard(PlayerColor.YELLOW).getMarks().size()).isEqualTo(0);
    assertThat(gameModel.getPlayerDashboard(PlayerColor.GREEN).getMarks()).containsExactly(PlayerColor.YELLOW);

    assertThat(gameModel.getRemainingSkulls()).isEqualTo(7);
    assertThat(gameModel.getKillScore().get(0)).isEqualTo(new AbstractMap.SimpleEntry<>(PlayerColor.GREEN, Boolean.TRUE));

    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW, gameModel));
    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GREEN, gameModel));
    assertThat(receivedModelEvents).haveExactly(1, isGameModelUpdatedEvent(gameModel));
  }

  @Test
  public void markPlayerTest() {
    GameModel gameModel = new GameModel(8, null,
      TestUtils.generate3PlayerDashboards());
    gameModel.getPlayerDashboard(PlayerColor.YELLOW).addMarks(Collections.nCopies(3, PlayerColor.GREEN));

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    gameModel.registerObserver(receivedModelEvents::add);
    gameModel.markPlayer(PlayerColor.GREEN, PlayerColor.YELLOW, 1);
    gameModel.markPlayer(PlayerColor.GREEN, PlayerColor.GRAY, 2);

    assertThat(gameModel.getPlayerDashboard(PlayerColor.YELLOW).getMarks())
      .isEqualTo(Collections.nCopies(3, PlayerColor.GREEN));

    assertThat(gameModel.getPlayerDashboard(PlayerColor.GRAY).getMarks().size()).isEqualTo(0);
    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW, gameModel));
    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY, gameModel));

    gameModel.getPlayerDashboard(PlayerColor.YELLOW).removeMarks(Collections.nCopies(3, PlayerColor.GREEN));
    gameModel.getPlayerDashboard(PlayerColor.GRAY).addMarks(Collections.nCopies(2, PlayerColor.GREEN));

    receivedModelEvents = new ArrayList<>();
    gameModel.registerObserver(receivedModelEvents::add);
    gameModel.markPlayer(PlayerColor.GREEN, PlayerColor.GRAY, 2);

    assertThat(gameModel.getPlayerDashboard(PlayerColor.GRAY).getMarks())
      .isEqualTo(Collections.nCopies(3, PlayerColor.GREEN));

    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY, gameModel));
  }

  @Test
  public void hitAndMarkPlayerTest() {
    GameModel gameModel = new GameModel(8, null,
      TestUtils.generate3PlayerDashboards());
    gameModel.getPlayerDashboard(PlayerColor.YELLOW).addMarks(Collections.singletonList(PlayerColor.GREEN));
    gameModel.getPlayerDashboard(PlayerColor.GRAY).addMarks(Collections.singletonList(PlayerColor.GREEN));

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    gameModel.registerObserver(receivedModelEvents::add);
    boolean killed = gameModel.hitAndMarkPlayer(PlayerColor.GREEN, PlayerColor.YELLOW, 11, 3);

    assertThat(killed).isTrue();
    assertThat(gameModel.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .isEqualTo(Collections.nCopies(12, PlayerColor.GREEN));

    assertThat(gameModel.getPlayerDashboard(PlayerColor.YELLOW).getMarks().size()).isEqualTo(2);
    assertThat(gameModel.getPlayerDashboard(PlayerColor.GREEN).getMarks()).containsExactly(PlayerColor.YELLOW);

    assertThat(gameModel.getRemainingSkulls()).isEqualTo(7);
    assertThat(gameModel.getKillScore().get(0)).isEqualTo(new AbstractMap.SimpleEntry<>(PlayerColor.GREEN, Boolean.TRUE));
    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW, gameModel));
    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GREEN, gameModel));
    assertThat(receivedModelEvents).haveExactly(1, isGameModelUpdatedEvent(gameModel));
  }

  @Test
  public void calculateMarksOnOtherPlayerDashboardsTest() {
    GameModel gameModel = new GameModel(8, null,
      TestUtils.generate3PlayerDashboards());

    gameModel.getPlayerDashboard(PlayerColor.YELLOW).addMarks(Collections.singletonList(PlayerColor.GREEN));
    gameModel.getPlayerDashboard(PlayerColor.GRAY).addMarks(Arrays.asList(PlayerColor.GREEN, PlayerColor.YELLOW));
    gameModel.getPlayerDashboard(PlayerColor.GREEN).addMarks(Collections.singletonList(PlayerColor.GREEN));

    assertThat(gameModel.calculateMarksOnOtherPlayerDashboards(PlayerColor.GREEN)).isEqualTo(2);
  }
}
