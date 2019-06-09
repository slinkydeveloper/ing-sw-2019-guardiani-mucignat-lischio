package com.adrenalinici.adrenaline.controller.nodes;

import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.model.common.*;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.view.event.UseTeleporterEvent;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.adrenalinici.adrenaline.testutil.MyConditions.isDashboardCellUpdatedEvent;
import static com.adrenalinici.adrenaline.testutil.MyConditions.isPlayerDashboardUpdateEvent;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unchecked")
public class TeleporterNodeFlowTest extends BaseNodeTest {

  @Override
  public FlowNode nodeToTest() {
    return new ApplyTeleporterFlowNode();
  }

  @Test
  public void testWrongPowerup() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    PowerUpCard card = new PowerUpCard(AmmoColor.BLUE, PowerUpType.NEWTON);
    model.getPlayerDashboard(PlayerColor.GREEN).addPowerUpCard(card);

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    orchestrator.startNewFlow(viewMock, context);
    context.handleEvent(new UseTeleporterEvent(Position.of(2, 2), card), viewMock);

    assertThat(receivedModelEvents).isEmpty();
  }

  @Test
  public void testNotAvailablePowerup() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    orchestrator.startNewFlow(viewMock, context);
    context.handleEvent(new UseTeleporterEvent(Position.of(2, 2), new PowerUpCard(AmmoColor.BLUE, PowerUpType.TELEPORTER)), viewMock);

    assertThat(receivedModelEvents).isEmpty();
  }

  @Test
  public void testTeleporterUsage() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);

    PowerUpCard card = new PowerUpCard(AmmoColor.BLUE, PowerUpType.TELEPORTER);

    model.getPlayerDashboard(PlayerColor.GREEN).addPowerUpCard(card);

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    orchestrator.startNewFlow(viewMock, context);

    context.handleEvent(new UseTeleporterEvent(Position.of(2, 2), card), viewMock);

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GREEN))
      .haveExactly(1, isDashboardCellUpdatedEvent(2, 2))
      .haveExactly(1, isDashboardCellUpdatedEvent(0, 0));

    receivedModelEvents.clear();
  }
}
