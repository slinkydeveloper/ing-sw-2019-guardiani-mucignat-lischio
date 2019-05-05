package com.adrenalinici.adrenaline.controller.nodes;

import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.model.*;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.view.event.GunChosenEvent;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.adrenalinici.adrenaline.model.DashboardCellBoundType.OPEN;
import static com.adrenalinici.adrenaline.testutil.MyConditions.isDashboardCellUpdatedEvent;
import static com.adrenalinici.adrenaline.testutil.MyConditions.isPlayerDashboardUpdateEvent;
import static com.adrenalinici.adrenaline.testutil.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class PickupNodeFlowTest extends BaseNodeTest {

  @Test
  public void calculateAvailableGunsToPickupTest() {
    Dashboard dashboard = Dashboard.newBuilder().build();
    RespawnDashboardCell respawnDashboardCell = new RespawnDashboardCell(OPEN, OPEN, OPEN, OPEN, 0, 0, dashboard);
    Stream
      .of("test_revolver", "test_rifle", "test_sword")
      .map(s -> gunLoader.getModelGun(s))
      .forEach(respawnDashboardCell::addAvailableGun);
    List<PowerUpCard> powerUpCards = Arrays.asList(new PowerUpCard(AmmoColor.RED, PowerUpType.KINETIC_RAY), new PowerUpCard(AmmoColor.BLUE, PowerUpType.SCOPE));
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.YELLOW, false, powerUpCards);
    List<PlayerDashboard> playerDashboardList = Collections.singletonList(playerDashboard);
    GameModel gameModel = new GameModel(8, dashboard, playerDashboardList);

    PickupFlowNode node = new PickupFlowNode();
    assertThat(node.calculateAvailableGunsToPickup(gameModel, respawnDashboardCell, PlayerColor.YELLOW))
      .extracting(Gun::getId).contains("test_sword");
    assertThat(node.calculateAvailableGunsToPickup(gameModel, respawnDashboardCell, PlayerColor.YELLOW))
      .extracting(Gun::getId).contains("test_revolver");
    assertThat(node.calculateAvailableGunsToPickup(gameModel, respawnDashboardCell, PlayerColor.YELLOW))
      .extracting(Gun::getId).doesNotContain("test_rifle");
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
        respawnDashboardCell.addAvailableGun(gunLoader.getModelGun("test_sword"));
      },
      null
    );

    orchestrator.startNewFlow(viewMock, context);

    ArgumentCaptor<List<Gun>> gunsCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(1)).showAvailableGunsToPickup(gunsCaptor.capture());
    assertThat(gunsCaptor.getValue())
      .extracting(Gun::getId)
      .containsOnly("test_sword");

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    orchestrator.handleEvent(new GunChosenEvent(
      "test_sword"
    ), viewMock);

    assertThat(receivedModelEvents)
      .haveExactly(1, isDashboardCellUpdatedEvent(2, 0));
    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GREEN, model));

    assertThat(model.getPlayerDashboard(PlayerColor.GREEN).getLoadedGuns())
      .extracting(Gun::getId)
      .containsOnly("test_sword");

    checkEndCalled();
  }
}
