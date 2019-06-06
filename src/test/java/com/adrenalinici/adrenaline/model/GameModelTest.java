package com.adrenalinici.adrenaline.model;

import com.adrenalinici.adrenaline.controller.GunLoader;
import com.adrenalinici.adrenaline.model.common.*;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.model.fat.*;
import com.adrenalinici.adrenaline.model.light.LightDashboardCell;
import com.adrenalinici.adrenaline.testutil.TestUtils;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static com.adrenalinici.adrenaline.model.fat.DashboardCellBoundType.OPEN;
import static com.adrenalinici.adrenaline.testutil.MyConditions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class GameModelTest {
  @Test
  public void addKillScoreTest() {
    GameModel gameModel = new GameModel(8, null, null, false);
    gameModel.addKillScore(PlayerColor.CYAN, false);
    assertThat(gameModel.getKillScore()).hasSize(1);
    assertThat(gameModel.getKillScore().get(0).getKey()).isEqualTo(PlayerColor.CYAN);
    assertThat(gameModel.getKillScore().get(0).getValue()).isEqualTo(false);
  }

  @Test
  public void inizializationTest() {
    GameModel gameModel = new GameModel(8, null, null, false);
    assertThat(gameModel.getRemainingSkulls()).isEqualTo(8);
    assertThat(gameModel.getKillScore().isEmpty()).isTrue();
    assertThat(gameModel.getDoubleKillScore().isEmpty()).isTrue();
  }

  @Test
  public void acquireGunUsingOnlyAmmosTest() {
    PowerUpCard blueKineticRay = new PowerUpCard(AmmoColor.BLUE, PowerUpType.KINETIC_RAY);
    PowerUpCard redTeleport = new PowerUpCard(AmmoColor.RED, PowerUpType.TELEPORT);
    List<PowerUpCard> powerUpCards = Arrays.asList(blueKineticRay, redTeleport);
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.GREEN, powerUpCards);
    playerDashboard.addAmmo(AmmoColor.RED);

    Dashboard dashboard = TestUtils.build3x3Dashboard();
    RespawnDashboardCell respawnDashboardCell = (RespawnDashboardCell) dashboard.getDashboardCell(Position.of(0, 0));

    respawnDashboardCell.addPlayer(PlayerColor.GREEN);
    respawnDashboardCell.addAvailableGun("test_sword");
    List<PlayerDashboard> playerDashboardList = Collections.singletonList(playerDashboard);
    GameModel gameModel = new GameModel(8, dashboard, playerDashboardList, true);

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
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.GREEN, powerUpCards);
    playerDashboard.removeAmmos(Arrays.asList(AmmoColor.RED, AmmoColor.YELLOW, AmmoColor.BLUE));

    Dashboard dashboard = TestUtils.build3x3Dashboard();
    RespawnDashboardCell respawnDashboardCell = (RespawnDashboardCell) dashboard.getDashboardCell(Position.of(0, 0));

    respawnDashboardCell.addPlayer(PlayerColor.GREEN);
    respawnDashboardCell.addAvailableGun("test_sword");
    List<PlayerDashboard> playerDashboardList = Collections.singletonList(playerDashboard);
    GameModel gameModel = new GameModel(8, dashboard, playerDashboardList, true);
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
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.GREEN, powerUpCards);
    playerDashboard.addAmmo(AmmoColor.RED);

    Dashboard dashboard = TestUtils.build3x3Dashboard();
    RespawnDashboardCell respawnDashboardCell = (RespawnDashboardCell) dashboard.getDashboardCell(Position.of(0, 0));

    respawnDashboardCell.addPlayer(PlayerColor.GREEN);
    respawnDashboardCell.addAvailableGun("test_sword");
    respawnDashboardCell.addAvailableGun("test_revolver");
    List<PlayerDashboard> playerDashboardList = Collections.singletonList(playerDashboard);

    GameModel gameModel = new GameModel(8, dashboard, playerDashboardList, true);
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

    AmmoCard ammoCard = new AmmoCard(Arrays.asList(AmmoColor.RED, AmmoColor.YELLOW), true);
    PickupDashboardCell pickupDashboardCell = new PickupDashboardCell(OPEN, OPEN, OPEN, OPEN, CellColor.CYAN,0, 0, dashboard);
    pickupDashboardCell.setAmmoCard(ammoCard);

    List<PowerUpCard> powerUpCards = Arrays.asList(new PowerUpCard(AmmoColor.RED, PowerUpType.KINETIC_RAY), new PowerUpCard(AmmoColor.BLUE, PowerUpType.SCOPE));
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.YELLOW, powerUpCards);
    List<PlayerDashboard> playerDashboardList = Arrays.asList(playerDashboard);
    GameModel gameModel = new GameModel(8, dashboard, playerDashboardList, true);
    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    gameModel.registerObserver(receivedModelEvents::add);
    gameModel.acquireAmmoCard(pickupDashboardCell, PlayerColor.YELLOW);
    assertThat(pickupDashboardCell.getAmmoCard()).isNotPresent();
    assertThat(playerDashboard.getAmmos())
      .containsExactlyInAnyOrderElementsOf(Arrays.asList(AmmoColor.BLUE, AmmoColor.RED, AmmoColor.RED, AmmoColor.YELLOW, AmmoColor.YELLOW));

    assertThat(playerDashboard.getPowerUpCards())
    .contains(
      new PowerUpCard(AmmoColor.RED, PowerUpType.KINETIC_RAY),
      new PowerUpCard(AmmoColor.BLUE, PowerUpType.SCOPE)
    )
    .hasSize(3);

    assertThat(receivedModelEvents).haveExactly(1, isDashboardCellUpdatedEvent(0, 0));
    assertThat(receivedModelEvents).haveExactly(2, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW));
  }

  @Test
  public void hitPlayerTest() {
    GameModel gameModel = new GameModel(8, TestUtils.build3x3Dashboard(),
      TestUtils.generate3PlayerDashboards(), true);

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
      TestUtils.generate3PlayerDashboards(), true);

    gameModel.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.YELLOW);
    gameModel.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GRAY);
    gameModel.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    gameModel.registerObserver(receivedModelEvents::add);
    boolean killed = gameModel.hitPlayer(PlayerColor.GREEN, PlayerColor.YELLOW, 9);

    assertThat(killed).isFalse();
    assertThat(gameModel.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .isEqualTo(Collections.nCopies(9, PlayerColor.GREEN));

    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW));

    receivedModelEvents = new ArrayList<>();
    gameModel.registerObserver(receivedModelEvents::add);
    killed = gameModel.hitPlayer(PlayerColor.GRAY, PlayerColor.YELLOW, 2);

    assertThat(killed).isTrue();

    assertThat(gameModel.getPlayerDashboard(PlayerColor.YELLOW).getDamages()).isEmpty();

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GREEN))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW))
      .haveExactly(1, isGameModelUpdatedEvent())
      .haveExactly(1, isDashboardCellUpdatedEvent(0, 0));

    assertThat(gameModel.getRemainingSkulls()).isEqualTo(7);
    assertThat(gameModel.getKillScore().get(0))
      .isEqualTo(new AbstractMap.SimpleEntry<>(PlayerColor.GRAY, Boolean.FALSE));
    assertThat(gameModel.getDashboard().getDashboardCell(Position.of(0, 0)).getPlayersInCell())
      .doesNotContain(PlayerColor.YELLOW);

    // Assert points
    assertThat(gameModel.getPlayerDashboard(PlayerColor.YELLOW).getPoints()).isEqualTo(0);
    assertThat(gameModel.getPlayerDashboard(PlayerColor.GRAY).getPoints()).isEqualTo(6);
    assertThat(gameModel.getPlayerDashboard(PlayerColor.GREEN).getPoints()).isEqualTo(8 + 1); // first blood!
  }

  @Test
  public void hitPlayerWithCruelDamageAndMarksTest() {
    GameModel gameModel = new GameModel(8, TestUtils.build3x3Dashboard(), TestUtils.generate3PlayerDashboards(), true);
    gameModel.getPlayerDashboard(PlayerColor.YELLOW).addMarks(Arrays.asList(PlayerColor.GREEN, PlayerColor.GREEN));
    gameModel.getPlayerDashboard(PlayerColor.YELLOW).setFlipped(true);

    gameModel.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.YELLOW);

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    gameModel.registerObserver(receivedModelEvents::add);
    boolean killed = gameModel.hitPlayer(PlayerColor.GREEN, PlayerColor.YELLOW, 11);

    assertThat(killed).isTrue();

    assertThat(gameModel.getPlayerDashboard(PlayerColor.YELLOW).getMarks().size()).isEqualTo(0);
    assertThat(gameModel.getPlayerDashboard(PlayerColor.GREEN).getMarks()).containsExactly(PlayerColor.YELLOW);

    assertThat(gameModel.getRemainingSkulls()).isEqualTo(7);
    assertThat(gameModel.getKillScore().get(0)).isEqualTo(new AbstractMap.SimpleEntry<>(PlayerColor.GREEN, Boolean.TRUE));

    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW));
    assertThat(receivedModelEvents).haveExactly(2, isPlayerDashboardUpdateEvent(PlayerColor.GREEN));
    assertThat(receivedModelEvents).haveExactly(1, isGameModelUpdatedEvent());
    assertThat(receivedModelEvents).haveExactly(1, isDashboardCellUpdatedEvent(0, 0));

    assertThat(gameModel.getDashboard().getDashboardCell(Position.of(0, 0)).getPlayersInCell()).doesNotContain(PlayerColor.YELLOW);

    assertThat(gameModel.getPlayerDashboard(PlayerColor.YELLOW).getPoints()).isEqualTo(0);
    assertThat(gameModel.getPlayerDashboard(PlayerColor.GRAY).getPoints()).isEqualTo(0);
    assertThat(gameModel.getPlayerDashboard(PlayerColor.GREEN).getPoints()).isEqualTo(2 + 1); // flipped + first blood!
  }

  @Test
  public void markPlayerTest() {
    GameModel gameModel = new GameModel(8, TestUtils.build3x3Dashboard(),
      TestUtils.generate3PlayerDashboards(), true);
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
      TestUtils.generate3PlayerDashboards(), true);
    gameModel.getPlayerDashboard(PlayerColor.YELLOW).addMarks(Collections.singletonList(PlayerColor.GREEN));
    gameModel.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.YELLOW);

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    gameModel.registerObserver(receivedModelEvents::add);
    boolean killed = gameModel.hitAndMarkPlayer(PlayerColor.GREEN, PlayerColor.YELLOW, 11, 3);

    assertThat(killed).isTrue();

    assertThat(gameModel.getPlayerDashboard(PlayerColor.YELLOW).getMarks())
      .containsAll(Collections.nCopies(3, PlayerColor.GREEN))
      .hasSize(3);
    assertThat(gameModel.getPlayerDashboard(PlayerColor.GREEN).getMarks())
      .containsExactly(PlayerColor.YELLOW);

    assertThat(gameModel.getRemainingSkulls()).isEqualTo(7);
    assertThat(gameModel.getKillScore().get(0)).isEqualTo(new AbstractMap.SimpleEntry<>(PlayerColor.GREEN, Boolean.TRUE));

    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW));
    assertThat(receivedModelEvents).haveExactly(2, isPlayerDashboardUpdateEvent(PlayerColor.GREEN));
    assertThat(receivedModelEvents).haveExactly(1, isGameModelUpdatedEvent());
    assertThat(receivedModelEvents).haveExactly(1, isDashboardCellUpdatedEvent(0, 0));

    assertThat(gameModel.getDashboard().getDashboardCell(Position.of(0, 0)).getPlayersInCell())
      .doesNotContain(PlayerColor.YELLOW);

    assertThat(gameModel.getPlayerDashboard(PlayerColor.GREEN).getPoints()).isEqualTo(8 + 1);
  }

  @Test
  public void calculateMarksOnOtherPlayerDashboardsTest() {
    GameModel gameModel = new GameModel(8, TestUtils.build3x3Dashboard(),
      TestUtils.generate3PlayerDashboards(), true);

    gameModel.getPlayerDashboard(PlayerColor.YELLOW).addMarks(Collections.singletonList(PlayerColor.GREEN));
    gameModel.getPlayerDashboard(PlayerColor.GRAY).addMarks(Arrays.asList(PlayerColor.GREEN, PlayerColor.YELLOW));
    gameModel.getPlayerDashboard(PlayerColor.GREEN).addMarks(Collections.singletonList(PlayerColor.GREEN));

    assertThat(gameModel.calculateMarksOnOtherPlayerDashboards(PlayerColor.GREEN)).isEqualTo(2);
  }

  @Test
  public void calculateKillerMarksOnVictimDashboardTest() {
    GameModel gameModel = new GameModel(8, TestUtils.build3x3Dashboard(),
      TestUtils.generate3PlayerDashboards(), true);

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

  @Test
  public void refillDashboard() {
    GameModel gameModel = new GameModel(8, TestUtils.build3x3Dashboard(),
      TestUtils.generate3PlayerDashboards(), true);

    // Refill a new dashboard
    gameModel.refillDashboard();

    assertThat(gameModel.getDashboard().stream())
      .noneMatch(EMPTY_CELL_PREDICATE); // All cells must have something

    // I generate the list of all cells excluding the central one that we are going to mutate
    List<LightDashboardCell> cellsBeforeMutation = gameModel.getDashboard().light().stream().collect(Collectors.toList());
    cellsBeforeMutation.remove(4); // Cell at position (1, 1)

    // Remove some stuff
    PickupDashboardCell cell = (PickupDashboardCell) gameModel.getDashboard().getDashboardCell(Position.of(1, 1));
    cell.removeAmmoCard();

    // Refill again
    gameModel.refillDashboard();

    assertThat(gameModel.getDashboard().stream())
      .noneMatch(EMPTY_CELL_PREDICATE); // All cells must have something

    List<LightDashboardCell> cellsAfterMutation = gameModel.getDashboard().light().stream().collect(Collectors.toList());
    cellsAfterMutation.remove(4); // Cell at position (1, 1)

    assertThat(cellsBeforeMutation)
      .containsAll(cellsAfterMutation);

  }

  @Test
  public void activateFrenzyMode() {
    GameModel gameModel = new GameModel(8, TestUtils.build3x3Dashboard(),
      TestUtils.generate4PlayerDashboards(), true);

    gameModel.getPlayerDashboard(PlayerColor.YELLOW).addDamages(Arrays.asList(PlayerColor.GREEN));

    gameModel.activateFrenzyMode(PlayerColor.YELLOW);

    assertThat(gameModel.isFrenzyModeFinished(PlayerColor.YELLOW)).isTrue();

    assertThat(gameModel.isFirstPlayerOrAfterFirstPlayerInFrenzyMode(PlayerColor.YELLOW)).isFalse();
    assertThat(gameModel.isFirstPlayerOrAfterFirstPlayerInFrenzyMode(PlayerColor.CYAN)).isFalse();
    assertThat(gameModel.isFirstPlayerOrAfterFirstPlayerInFrenzyMode(PlayerColor.GREEN)).isTrue();
    assertThat(gameModel.isFirstPlayerOrAfterFirstPlayerInFrenzyMode(PlayerColor.GRAY)).isTrue();
  }
}
