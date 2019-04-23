package com.adrenalinici.adrenaline.controller.nodes;

import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.model.*;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.view.event.GunToPickupChosenEvent;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    BASE_EFFECT_GUNS.forEach(respawnDashboardCell::addAvailableGun);
    List<PowerUpCard> powerUpCards = Arrays.asList(new PowerUpCard(AmmoColor.RED, PowerUpType.KINETIC_RAY), new PowerUpCard(AmmoColor.BLUE, PowerUpType.SCOPE));
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.YELLOW, false, powerUpCards);
    List<PlayerDashboard> playerDashboardList = Collections.singletonList(playerDashboard);
    GameModel gameModel = new GameModel(8, dashboard, playerDashboardList);

    PickupFlowNode node = new PickupFlowNode();
    assertThat(node.calculateAvailableGunsToPickup(gameModel, respawnDashboardCell, PlayerColor.YELLOW)).contains(BASE_EFFECT_GUN_SWORD);
    assertThat(node.calculateAvailableGunsToPickup(gameModel, respawnDashboardCell, PlayerColor.YELLOW)).contains(BASE_EFFECT_GUN_REVOLVER);
    assertThat(node.calculateAvailableGunsToPickup(gameModel, respawnDashboardCell, PlayerColor.YELLOW)).doesNotContain(BASE_EFFECT_GUN_RIFLE);
  }

  @Override
  public FlowNode nodeToTest() {
    return new PickupFlowNode();
  }

  @Test
  public void testPickupNode() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(2, 0)).visit(
      respawnDashboardCell -> {
        respawnDashboardCell.addPlayer(PlayerColor.GREEN);
        respawnDashboardCell.addAvailableGun(BASE_EFFECT_GUN_SWORD);
      },
      null
    );

    orchestrator.startNewFlow(viewMock, context);

    ArgumentCaptor<List<Gun>> gunsCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(1)).showAvailableGunsToPickup(gunsCaptor.capture());
    assertThat(gunsCaptor.getValue())
      .containsOnly(BASE_EFFECT_GUN_SWORD);

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    orchestrator.handleEvent(new GunToPickupChosenEvent(
      viewMock,
      BASE_EFFECT_GUN_SWORD,
      (RespawnDashboardCell) model.getDashboard().getDashboardCell(Position.of(2, 0))
    ));

    ArgumentCaptor<PlayerColor> nextPlayerCaptor = ArgumentCaptor.forClass(PlayerColor.class);
    verify(viewMock, times(1)).showNextTurn(nextPlayerCaptor.capture());
    assertThat(nextPlayerCaptor.getValue())
      .isSameAs(PlayerColor.GRAY);

    assertThat(receivedModelEvents)
      .haveExactly(1, isDashboardCellUpdatedEvent(2, 0));
    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GREEN, model));

    checkEndCalled();
  }
}
