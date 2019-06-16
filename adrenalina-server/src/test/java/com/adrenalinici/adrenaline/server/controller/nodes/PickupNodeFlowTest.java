package com.adrenalinici.adrenaline.server.controller.nodes;

import com.adrenalinici.adrenaline.common.model.*;
import com.adrenalinici.adrenaline.common.model.event.ModelEvent;
import com.adrenalinici.adrenaline.common.view.GunChosenEvent;
import com.adrenalinici.adrenaline.server.flow.FlowNode;
import com.adrenalinici.adrenaline.server.model.Dashboard;
import com.adrenalinici.adrenaline.server.model.GameModel;
import com.adrenalinici.adrenaline.server.model.PlayerDashboard;
import com.adrenalinici.adrenaline.server.model.RespawnDashboardCell;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.*;
import java.util.stream.Stream;

import static com.adrenalinici.adrenaline.common.model.DashboardCellBoundType.OPEN;
import static com.adrenalinici.adrenaline.server.testutil.MyConditions.isDashboardCellUpdatedEvent;
import static com.adrenalinici.adrenaline.server.testutil.MyConditions.isPlayerDashboardUpdateEvent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class PickupNodeFlowTest extends BaseNodeTest {

  @Test
  public void calculateAvailableGunsToPickupTest() {
    Dashboard dashboard = Dashboard.newBuilder().build();
    RespawnDashboardCell respawnDashboardCell = new RespawnDashboardCell(OPEN, OPEN, OPEN, OPEN, CellColor.CYAN, 0, 0, dashboard);
    Stream
      .of("test_revolver", "test_rifle", "test_sword")
      .forEach(respawnDashboardCell::addAvailableGun);
    List<PowerUpCard> powerUpCards = Arrays.asList(new PowerUpCard(AmmoColor.RED, PowerUpType.NEWTON), new PowerUpCard(AmmoColor.BLUE, PowerUpType.SCOPE));
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.YELLOW, powerUpCards);
    List<PlayerDashboard> playerDashboardList = Collections.singletonList(playerDashboard);
    GameModel gameModel = new GameModel(8, dashboard, playerDashboardList, true);

    PickupFlowNode node = new PickupFlowNode();
    assertThat(node.calculateAvailableGunsToPickup(gameModel, respawnDashboardCell, PlayerColor.YELLOW))
      .contains("test_sword");
    assertThat(node.calculateAvailableGunsToPickup(gameModel, respawnDashboardCell, PlayerColor.YELLOW))
      .contains("test_revolver");
    assertThat(node.calculateAvailableGunsToPickup(gameModel, respawnDashboardCell, PlayerColor.YELLOW))
      .doesNotContain("test_rifle");
  }

  @Override
  public FlowNode nodeToTest() {
    return new PickupFlowNode();
  }

  @Test
  public void testPickupNode() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getPlayerDashboard(PlayerColor.GREEN).addAmmo(AmmoColor.RED);
    model.getDashboard().getDashboardCell(Position.of(2, 0)).visit(
      respawnDashboardCell -> {
        respawnDashboardCell.addPlayer(PlayerColor.GREEN);
        respawnDashboardCell.addAvailableGun("test_sword");
      },
      null
    );

    orchestrator.startNewFlow(viewMock, context);

    ArgumentCaptor<Set<String>> gunsCaptor = ArgumentCaptor.forClass(Set.class);
    verify(viewMock, times(1)).showAvailableGunsToPickup(gunsCaptor.capture());
    assertThat(gunsCaptor.getValue())
      .containsOnly("test_sword");

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    orchestrator.handleEvent(new GunChosenEvent(
      "test_sword"
    ), viewMock);

    assertThat(receivedModelEvents)
      .haveExactly(1, isDashboardCellUpdatedEvent(2, 0));
    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GREEN));

    assertThat(model.getPlayerDashboard(PlayerColor.GREEN).getLoadedGuns())
      .containsOnly("test_sword");

    checkEndCalled();
  }

  @Test
  public void testPickupNodeOnAmmoCard() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getPlayerDashboard(PlayerColor.GREEN)
      .removeAmmos(Arrays.asList(AmmoColor.BLUE, AmmoColor.YELLOW, AmmoColor.RED));

    assertThat(model.getPlayerDashboard(PlayerColor.GREEN).getAmmos())
      .isEmpty();
    assertThat(model.getPlayerDashboard(PlayerColor.GREEN).getPowerUpCards())
      .isEmpty();

    AmmoCard ammoCard = new AmmoCard(Arrays.asList(AmmoColor.RED, AmmoColor.YELLOW), true);
    model.getDashboard().getDashboardCell(Position.of(0, 1))
      .visit(
        null,
        pickupDashboardCell -> {
          pickupDashboardCell.addPlayer(PlayerColor.GREEN);
          pickupDashboardCell.setAmmoCard(ammoCard);
        }
      );

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    orchestrator.startNewFlow(viewMock, context);

    assertThat(receivedModelEvents)
      .haveExactly(1, isDashboardCellUpdatedEvent(0, 1));
    assertThat(receivedModelEvents)
      .haveExactly(2, isPlayerDashboardUpdateEvent(PlayerColor.GREEN));

    assertThat(model.getPlayerDashboard(PlayerColor.GREEN).getAmmos())
      .containsExactlyInAnyOrder(AmmoColor.YELLOW, AmmoColor.RED);
    assertThat(model.getPlayerDashboard(PlayerColor.GREEN).getPowerUpCards())
      .isNotEmpty();

    checkEndCalled();
  }
}
