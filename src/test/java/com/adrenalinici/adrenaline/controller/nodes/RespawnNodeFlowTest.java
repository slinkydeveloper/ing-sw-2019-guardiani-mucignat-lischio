package com.adrenalinici.adrenaline.controller.nodes;

import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.model.common.*;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.model.fat.Dashboard;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.model.fat.PlayerDashboard;
import com.adrenalinici.adrenaline.model.fat.RespawnDashboardCell;
import com.adrenalinici.adrenaline.view.event.GunChosenEvent;
import com.adrenalinici.adrenaline.view.event.PowerUpCardChosenEvent;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.*;
import java.util.stream.Stream;

import static com.adrenalinici.adrenaline.model.fat.DashboardCellBoundType.OPEN;
import static com.adrenalinici.adrenaline.testutil.MyConditions.isDashboardCellUpdatedEvent;
import static com.adrenalinici.adrenaline.testutil.MyConditions.isPlayerDashboardUpdateEvent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class RespawnNodeFlowTest extends BaseNodeTest {

  @Override
  public FlowNode nodeToTest() {
    return new RespawnFlowNode();
  }

  @Test
  public void testNoKilledPlayer() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    orchestrator.startNewFlow(viewMock, context);

    assertThat(receivedModelEvents).isEmpty();

    checkEndCalled();
  }

  @Test
  public void testOneKilledPlayer() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    context.getKilledPlayers().add(PlayerColor.GRAY);

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    orchestrator.startNewFlow(viewMock, context);

    ArgumentCaptor<PlayerColor> playerColorCaptor = ArgumentCaptor.forClass(PlayerColor.class);
    ArgumentCaptor<List<PowerUpCard>> powerUpCardCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(1)).showAvailablePowerUpCardsForRespawn(playerColorCaptor.capture(), powerUpCardCaptor.capture());
    assertThat(playerColorCaptor.getValue())
      .isEqualTo(PlayerColor.GRAY);
    assertThat(powerUpCardCaptor.getValue())
      .hasSize(1);

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getPowerUpCards())
      .hasSize(1);

    PowerUpCard extracted = powerUpCardCaptor.getValue().get(0);
    Position cellPositionWherePlayerShouldRespawn = model
      .getDashboard()
      .stream()
      .filter(c -> c.getCellColor().matchesAmmoColor(extracted.getAmmoColor()))
      .findFirst()
      .get()
      .getPosition();

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY));
    receivedModelEvents.clear();
    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getPowerUpCards()).containsExactly(extracted);

    orchestrator.handleEvent(new PowerUpCardChosenEvent(PlayerColor.GRAY, extracted), viewMock);

    assertThat(receivedModelEvents)
      .haveExactly(1, isDashboardCellUpdatedEvent(cellPositionWherePlayerShouldRespawn.line(), cellPositionWherePlayerShouldRespawn.cell()));
    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY));

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getPowerUpCards()).isEmpty();
    assertThat(model.getDashboard().getPlayersPositions().get(PlayerColor.GRAY)).isEqualTo(cellPositionWherePlayerShouldRespawn);

    checkEndCalled();
  }

  @Test
  public void testTwoKilledPlayer() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    context.getKilledPlayers().add(PlayerColor.GRAY);
    context.getKilledPlayers().add(PlayerColor.YELLOW);

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    orchestrator.startNewFlow(viewMock, context);

    ArgumentCaptor<PlayerColor> playerColorCaptor = ArgumentCaptor.forClass(PlayerColor.class);
    ArgumentCaptor<List<PowerUpCard>> powerUpCardCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(2)).showAvailablePowerUpCardsForRespawn(playerColorCaptor.capture(), powerUpCardCaptor.capture());
    assertThat(playerColorCaptor.getAllValues())
      .containsExactly(PlayerColor.GRAY, PlayerColor.YELLOW);
    assertThat(powerUpCardCaptor.getAllValues().stream().mapToInt(List::size).sum())
      .isEqualTo(2);

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getPowerUpCards())
      .hasSize(1);
    assertThat(model.getPlayerDashboard(PlayerColor.YELLOW).getPowerUpCards())
      .hasSize(1);

    PowerUpCard extractedPowerUpCardForGray = model.getPlayerDashboard(PlayerColor.GRAY).getPowerUpCards().get(0);
    Position cellPositionWhereGrayShouldRespawn = model
      .getDashboard()
      .stream()
      .filter(c -> c.getCellColor().matchesAmmoColor(extractedPowerUpCardForGray.getAmmoColor()))
      .findFirst()
      .get()
      .getPosition();

    PowerUpCard extractedPowerUpCardForYellow = model.getPlayerDashboard(PlayerColor.YELLOW).getPowerUpCards().get(0);
    Position cellPositionWhereYellowShouldRespawn = model
      .getDashboard()
      .stream()
      .filter(c -> c.getCellColor().matchesAmmoColor(extractedPowerUpCardForYellow.getAmmoColor()))
      .findFirst()
      .get()
      .getPosition();

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW));
    receivedModelEvents.clear();

    orchestrator.handleEvent(new PowerUpCardChosenEvent(PlayerColor.GRAY, extractedPowerUpCardForGray), viewMock);

    assertThat(receivedModelEvents)
      .haveExactly(1, isDashboardCellUpdatedEvent(cellPositionWhereGrayShouldRespawn.line(), cellPositionWhereGrayShouldRespawn.cell()))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY));

    receivedModelEvents.clear();

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getPowerUpCards()).isEmpty();
    assertThat(model.getDashboard().getPlayersPositions().get(PlayerColor.GRAY)).isEqualTo(cellPositionWhereGrayShouldRespawn);

    orchestrator.handleEvent(new PowerUpCardChosenEvent(PlayerColor.YELLOW, extractedPowerUpCardForYellow), viewMock);

    assertThat(receivedModelEvents)
      .haveExactly(1, isDashboardCellUpdatedEvent(cellPositionWhereYellowShouldRespawn.line(), cellPositionWhereYellowShouldRespawn.cell()))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW));

    assertThat(model.getPlayerDashboard(PlayerColor.YELLOW).getPowerUpCards()).isEmpty();
    assertThat(model.getDashboard().getPlayersPositions().get(PlayerColor.YELLOW)).isEqualTo(cellPositionWhereYellowShouldRespawn);

    checkEndCalled();
  }
}
