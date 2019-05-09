package com.adrenalinici.adrenaline.model;

import com.adrenalinici.adrenaline.controller.GunLoader;
import com.adrenalinici.adrenaline.model.common.*;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.model.fat.*;
import com.adrenalinici.adrenaline.testutil.TestUtils;
import org.junit.Test;

import java.util.*;

import static com.adrenalinici.adrenaline.model.fat.DashboardCellBoundType.OPEN;
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
    PowerUpCard blueKineticRay = new PowerUpCard(AmmoColor.BLUE, PowerUpType.KINETIC_RAY);
    PowerUpCard redTeleport = new PowerUpCard(AmmoColor.RED, PowerUpType.TELEPORT);
    List<PowerUpCard> powerUpCards = Arrays.asList(blueKineticRay, redTeleport);
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.GREEN, false, powerUpCards);
    playerDashboard.addAmmo(AmmoColor.RED);

    Dashboard dashboard = TestUtils.build3x3Dashboard();
    RespawnDashboardCell respawnDashboardCell = (RespawnDashboardCell) dashboard.getDashboardCell(Position.of(0, 0));

    respawnDashboardCell.addPlayer(PlayerColor.GREEN);
    respawnDashboardCell.addAvailableGun("test_sword");
    List<PlayerDashboard> playerDashboardList = Collections.singletonList(playerDashboard);
    GameModel gameModel = new GameModel(8, dashboard, playerDashboardList);

    gameModel.acquireGun(PlayerColor.GREEN, GunLoader.INSTANCE.getModelGun("test_sword"));
    assertThat(playerDashboard.getAmmos())
      .containsOnly(AmmoColor.YELLOW);
    assertThat(playerDashboard.getLoadedGuns())
      .containsOnly("test_sword");

    assertThat(playerDashboard.getPowerUpCards()).containsExactlyInAnyOrder(blueKineticRay, redTeleport);
  }

  @Test
  public void acquireGunUsingOnlyPowerupsTest() {

    PowerUpCard redTeleport = new PowerUpCard(AmmoColor.RED, PowerUpType.TELEPORT);
    PowerUpCard redKineticRay = new PowerUpCard(AmmoColor.RED, PowerUpType.KINETIC_RAY);
    PowerUpCard blueKineticRay = new PowerUpCard(AmmoColor.BLUE, PowerUpType.KINETIC_RAY);
    PowerUpCard blueTeleport = new PowerUpCard(AmmoColor.BLUE, PowerUpType.TELEPORT);
    List<PowerUpCard> powerUpCards = Arrays.asList(blueKineticRay, redKineticRay, redTeleport, blueTeleport);
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.GREEN, false, powerUpCards);
    playerDashboard.removeAmmos(Arrays.asList(AmmoColor.RED, AmmoColor.YELLOW, AmmoColor.BLUE));

    Dashboard dashboard = TestUtils.build3x3Dashboard();
    RespawnDashboardCell respawnDashboardCell = (RespawnDashboardCell) dashboard.getDashboardCell(Position.of(0, 0));

    respawnDashboardCell.addPlayer(PlayerColor.GREEN);
    respawnDashboardCell.addAvailableGun("test_sword");
    List<PlayerDashboard> playerDashboardList = Collections.singletonList(playerDashboard);
    GameModel gameModel = new GameModel(8, dashboard, playerDashboardList);
    assertThat(playerDashboard.getAmmos().isEmpty()).isTrue();

    gameModel.acquireGun(PlayerColor.GREEN, GunLoader.INSTANCE.getModelGun("test_sword"));
    assertThat(playerDashboard.getPowerUpCards())
      .containsOnly(blueTeleport);
    assertThat(playerDashboard.getLoadedGuns())
      .containsOnly("test_sword");
  }

  @Test
  public void acquireGunUsingBothAmmosAndPowerupsTest() {

    PowerUpCard blueKineticRay = new PowerUpCard(AmmoColor.BLUE, PowerUpType.KINETIC_RAY);
    PowerUpCard redTeleport = new PowerUpCard(AmmoColor.RED, PowerUpType.TELEPORT);
    List<PowerUpCard> powerUpCards = Arrays.asList(blueKineticRay, redTeleport);
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.GREEN, false, powerUpCards);
    playerDashboard.addAmmo(AmmoColor.RED);

    Dashboard dashboard = TestUtils.build3x3Dashboard();
    RespawnDashboardCell respawnDashboardCell = (RespawnDashboardCell) dashboard.getDashboardCell(Position.of(0, 0));

    respawnDashboardCell.addPlayer(PlayerColor.GREEN);
    respawnDashboardCell.addAvailableGun("test_sword");
    respawnDashboardCell.addAvailableGun("test_revolver");
    List<PlayerDashboard> playerDashboardList = Collections.singletonList(playerDashboard);

    GameModel gameModel = new GameModel(8, dashboard, playerDashboardList);
    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    gameModel.registerObserver(receivedModelEvents::add);

    gameModel.acquireGun(PlayerColor.GREEN, GunLoader.INSTANCE.getModelGun("test_sword"));
    assertThat(playerDashboard.getAmmos()).containsOnly(AmmoColor.YELLOW);
    assertThat(playerDashboard.getPowerUpCards()).containsOnly(blueKineticRay, redTeleport);

    gameModel.acquireGun(PlayerColor.GREEN, GunLoader.INSTANCE.getModelGun("test_revolver"));
    assertThat(playerDashboard.getAmmos()).isEmpty();
    assertThat(playerDashboard.getPowerUpCards()).isEmpty();

    assertThat(receivedModelEvents).haveExactly(2, isDashboardCellUpdatedEvent(0, 0));
    assertThat(receivedModelEvents).haveExactly(2, isPlayerDashboardUpdateEvent(PlayerColor.GREEN));
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
    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW));
  }

  @Test
  public void hitPlayerTest() {
    GameModel gameModel = new GameModel(8, TestUtils.build3x3Dashboard(),
      TestUtils.generate3PlayerDashboards());

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    gameModel.registerObserver(receivedModelEvents::add);
    boolean killed = gameModel.hitPlayer(PlayerColor.GREEN, PlayerColor.YELLOW, 10);

    assertThat(killed).isFalse();
    assertThat(gameModel.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .isEqualTo(Collections.nCopies(10, PlayerColor.GREEN));

    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW));
    assertThat(gameModel.getRemainingSkulls()).isEqualTo(8);
    assertThat(gameModel.getKillScore().size()).isEqualTo(0);
  }

  @Test
  public void hitPlayerWithKillDamageTest() {
    GameModel gameModel = new GameModel(8, TestUtils.build3x3Dashboard(),
      TestUtils.generate3PlayerDashboards());

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    gameModel.registerObserver(receivedModelEvents::add);
    boolean killed = gameModel.hitPlayer(PlayerColor.GREEN, PlayerColor.YELLOW, 9);

    assertThat(killed).isFalse();
    assertThat(gameModel.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .isEqualTo(Collections.nCopies(9, PlayerColor.GREEN));

    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW));

    receivedModelEvents = new ArrayList<>();
    gameModel.registerObserver(receivedModelEvents::add);
    killed = gameModel.hitPlayer(PlayerColor.GREEN, PlayerColor.YELLOW, 2);

    assertThat(killed).isTrue();
    assertThat(gameModel.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .isEqualTo(Collections.nCopies(11, PlayerColor.GREEN));

    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW));
    assertThat(receivedModelEvents).haveExactly(1, isGameModelUpdatedEvent());
    assertThat(gameModel.getRemainingSkulls()).isEqualTo(7);
    assertThat(gameModel.getKillScore().get(0)).isEqualTo(new AbstractMap.SimpleEntry<>(PlayerColor.GREEN, Boolean.FALSE));
  }

  @Test
  public void hitPlayerWithCruelDamageAndMarksTest() {
    GameModel gameModel = new GameModel(8, TestUtils.build3x3Dashboard(),
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

    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW));
    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GREEN));
    assertThat(receivedModelEvents).haveExactly(1, isGameModelUpdatedEvent());
  }

  @Test
  public void markPlayerTest() {
    GameModel gameModel = new GameModel(8, TestUtils.build3x3Dashboard(),
      TestUtils.generate3PlayerDashboards());
    gameModel.getPlayerDashboard(PlayerColor.YELLOW).addMarks(Collections.nCopies(3, PlayerColor.GREEN));

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    gameModel.registerObserver(receivedModelEvents::add);
    gameModel.markPlayer(PlayerColor.GREEN, PlayerColor.YELLOW, 1);
    gameModel.markPlayer(PlayerColor.GREEN, PlayerColor.GRAY, 2);

    assertThat(gameModel.getPlayerDashboard(PlayerColor.YELLOW).getMarks())
      .isEqualTo(Collections.nCopies(3, PlayerColor.GREEN));
    assertThat(gameModel.getPlayerDashboard(PlayerColor.GRAY).getMarks())
      .isEqualTo(Collections.nCopies(2, PlayerColor.GREEN));

    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW));
    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY));

    receivedModelEvents = new ArrayList<>();
    gameModel.registerObserver(receivedModelEvents::add);
    gameModel.markPlayer(PlayerColor.GREEN, PlayerColor.GRAY, 2);

    assertThat(gameModel.getPlayerDashboard(PlayerColor.GRAY).getMarks())
      .isEqualTo(Collections.nCopies(3, PlayerColor.GREEN));

    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY));
  }

  @Test
  public void hitAndMarkPlayerTest() {
    GameModel gameModel = new GameModel(8, TestUtils.build3x3Dashboard(),
      TestUtils.generate3PlayerDashboards());
    gameModel.getPlayerDashboard(PlayerColor.YELLOW).addMarks(Collections.singletonList(PlayerColor.GREEN));

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    gameModel.registerObserver(receivedModelEvents::add);
    boolean killed = gameModel.hitAndMarkPlayer(PlayerColor.GREEN, PlayerColor.YELLOW, 11, 3);

    assertThat(killed).isTrue();
    assertThat(gameModel.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .isEqualTo(Collections.nCopies(12, PlayerColor.GREEN));

    assertThat(gameModel.getPlayerDashboard(PlayerColor.YELLOW).getMarks().size()).isEqualTo(3);
    assertThat(gameModel.getPlayerDashboard(PlayerColor.GREEN).getMarks()).containsExactly(PlayerColor.YELLOW);

    assertThat(gameModel.getRemainingSkulls()).isEqualTo(7);
    assertThat(gameModel.getKillScore().get(0)).isEqualTo(new AbstractMap.SimpleEntry<>(PlayerColor.GREEN, Boolean.TRUE));
    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW));
    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GREEN));
    assertThat(receivedModelEvents).haveExactly(1, isGameModelUpdatedEvent());
  }

  @Test
  public void calculateMarksOnOtherPlayerDashboardsTest() {
    GameModel gameModel = new GameModel(8, TestUtils.build3x3Dashboard(),
      TestUtils.generate3PlayerDashboards());

    gameModel.getPlayerDashboard(PlayerColor.YELLOW).addMarks(Collections.singletonList(PlayerColor.GREEN));
    gameModel.getPlayerDashboard(PlayerColor.GRAY).addMarks(Arrays.asList(PlayerColor.GREEN, PlayerColor.YELLOW));
    gameModel.getPlayerDashboard(PlayerColor.GREEN).addMarks(Collections.singletonList(PlayerColor.GREEN));

    assertThat(gameModel.calculateMarksOnOtherPlayerDashboards(PlayerColor.GREEN)).isEqualTo(2);
  }

  @Test
  public void calculateKillerMarksOnVictimDashboardTest() {
    GameModel gameModel = new GameModel(8, TestUtils.build3x3Dashboard(),
      TestUtils.generate3PlayerDashboards());

    gameModel.getPlayerDashboard(PlayerColor.YELLOW)
      .addMarks(
        Arrays.asList(
          PlayerColor.GREEN,
          PlayerColor.GREEN,
          PlayerColor.YELLOW)
      );

    assertThat(gameModel.calculateKillerMarksOnVictimPlayerDashboard(PlayerColor.GREEN, PlayerColor.YELLOW))
      .isEqualTo(2);
  }
}
