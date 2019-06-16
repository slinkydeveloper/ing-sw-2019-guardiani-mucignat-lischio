package com.adrenalinici.adrenaline.server.controller.nodes;

import com.adrenalinici.adrenaline.common.model.*;
import com.adrenalinici.adrenaline.common.model.event.ModelEvent;
import com.adrenalinici.adrenaline.common.view.UseNewtonEvent;
import com.adrenalinici.adrenaline.server.flow.FlowNode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.adrenalinici.adrenaline.server.testutil.MyConditions.isDashboardCellUpdatedEvent;
import static com.adrenalinici.adrenaline.server.testutil.MyConditions.isPlayerDashboardUpdateEvent;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unchecked")
public class NewtonNodeFlowTest extends BaseNodeTest {

  @Override
  public FlowNode nodeToTest() {
    return new ApplyNewtonFlowNode();
  }

  @Test
  public void testWrongPowerup() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    PowerUpCard card = new PowerUpCard(AmmoColor.BLUE, PowerUpType.TELEPORTER);
    model.getPlayerDashboard(PlayerColor.GREEN).addPowerUpCard(card);

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    orchestrator.startNewFlow(viewMock, context);
    context.handleEvent(new UseNewtonEvent(card, Position.of(2, 2), PlayerColor.CYAN), viewMock);

    assertThat(receivedModelEvents).isEmpty();
  }

  @Test
  public void testNotAvailablePowerup() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    orchestrator.startNewFlow(viewMock, context);
    context.handleEvent(new UseNewtonEvent(new PowerUpCard(AmmoColor.BLUE, PowerUpType.NEWTON), Position.of(2, 2), PlayerColor.GRAY), viewMock);

    assertThat(receivedModelEvents).isEmpty();
  }

  @Test
  public void testNotAvailablePlayer() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    PowerUpCard card = new PowerUpCard(AmmoColor.BLUE, PowerUpType.NEWTON);
    model.getPlayerDashboard(PlayerColor.GREEN).addPowerUpCard(card);

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    orchestrator.startNewFlow(viewMock, context);
    context.handleEvent(new UseNewtonEvent(card, Position.of(2, 2), PlayerColor.PURPLE), viewMock);

    assertThat(receivedModelEvents).isEmpty();
  }

  @Test
  public void testNewtonUsage() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(1, 1)).addPlayer(PlayerColor.GRAY);

    PowerUpCard card = new PowerUpCard(AmmoColor.BLUE, PowerUpType.NEWTON);
    model.getPlayerDashboard(PlayerColor.GREEN).addPowerUpCard(card);

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    orchestrator.startNewFlow(viewMock, context);

    context.handleEvent(new UseNewtonEvent(card, Position.of(1, 0), PlayerColor.GRAY), viewMock);

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GREEN))
      .haveExactly(1, isDashboardCellUpdatedEvent(1, 1))
      .haveExactly(1, isDashboardCellUpdatedEvent(1, 0));

    receivedModelEvents.clear();
  }
}
