package com.adrenalinici.adrenaline.server.controller.nodes;

import com.adrenalinici.adrenaline.common.model.Action;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.view.ActionChosenEvent;
import com.adrenalinici.adrenaline.server.flow.FlowNode;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes.PICKUP;
import static com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes.movement;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class ChooseActionNodeFlowTest extends BaseNodeTest {

  @Override
  public FlowNode nodeToTest() {
    return new ChooseActionFlowNode();
  }

  @Test
  public void testShowAvailableActions() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    context.setRemainingActions(1);

    orchestrator.startNewFlow(viewMock, context);

    ArgumentCaptor<List<Action>> actionsCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(1)).showAvailableActions(actionsCaptor.capture());
    assertThat(actionsCaptor.getValue())
      .containsOnly(Action.MOVE_MOVE_MOVE, Action.MOVE_PICKUP);
  }

  @Test
  public void testShowAvailableActionsWithSevenDamagesAndLoadedGun() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    context.setRemainingActions(1);
    model.getPlayerDashboard(context.getTurnOfPlayer()).addGun("railgun");
    model.getPlayerDashboard(context.getTurnOfPlayer()).addDamages(Collections.nCopies(7, PlayerColor.PURPLE));

    orchestrator.startNewFlow(viewMock, context);

    ArgumentCaptor<List<Action>> actionsCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(1)).showAvailableActions(actionsCaptor.capture());
    assertThat(actionsCaptor.getValue())
      .containsOnly(Action.MOVE_MOVE_MOVE, Action.MOVE_PICKUP, Action.MOVE_MOVE_PICKUP, Action.SHOOT, Action.MOVE_SHOOT);
  }


  @Test
  public void testChooseMoveMoveMove() {
    testChooseSpecificAction(
      Action.MOVE_MOVE_MOVE,
      phases -> assertThat(phases.get(0)).isEqualTo(movement(3))
    );
  }

  @Test
  public void testNullAction() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    context.setRemainingActions(1);
    model.getDashboard().getDashboardCell(Position.of(1, 1)).addPlayer(PlayerColor.GREEN);

    orchestrator.startNewFlow(viewMock, context);

    orchestrator.handleEvent(new ActionChosenEvent(null), viewMock);

    checkEndCalled();
  }

  @Test
  public void testChooseMovePickup() {
    testChooseSpecificAction(Action.MOVE_PICKUP, phases -> {
      assertThat(phases.get(0)).isEqualTo(movement(1));
      assertThat(phases.get(1)).isEqualTo(PICKUP.name());
    });
  }

  private void testChooseSpecificAction(Action action, Consumer<List<String>> remainingPhases) {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    context.setRemainingActions(1);
    model.getDashboard().getDashboardCell(Position.of(1, 1)).addPlayer(PlayerColor.GREEN);

    orchestrator.startNewFlow(viewMock, context);

    orchestrator.handleEvent(new ActionChosenEvent(action), viewMock);

    remainingPhases.accept(context.getPhasesQueue());

    checkEndCalled();
  }
}
