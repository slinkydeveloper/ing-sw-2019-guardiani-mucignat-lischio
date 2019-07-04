package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.model.event.ModelEvent;
import com.adrenalinici.adrenaline.common.view.MovementChosenEvent;
import com.adrenalinici.adrenaline.server.controller.DecoratedAlternativeEffectGun;
import com.adrenalinici.adrenaline.server.controller.GunLoader;
import com.adrenalinici.adrenaline.server.controller.nodes.BaseNodeTest;
import com.adrenalinici.adrenaline.server.flow.FlowNode;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static com.adrenalinici.adrenaline.server.testutil.MyConditions.isDashboardCellUpdatedEvent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AlternativeGunChooseMovementFlowNodeTest extends BaseNodeTest {
  @Override
  public void setUp() {
    super.setUp();
  }

  @Override
  public FlowNode nodeToTest() {
    return new AlternativeGunChooseMovementFlowNode();
  }

  @Test
  public void testShowAvailableMovements() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.YELLOW);
    model.getPlayerDashboard(PlayerColor.GREEN).addGun("power_glove");

    AlternativeEffectGunFlowState alternativeEffectGunFlowState =
      new AlternativeEffectGunFlowStateImpl((DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun("power_glove"));
    alternativeEffectGunFlowState.setChosenEffect(alternativeEffectGunFlowState.getChosenGun().getSecondEffect(), false);

    alternativeEffectGunFlowState.getChosenPlayersToHit().add(PlayerColor.YELLOW);
    alternativeEffectGunFlowState.setKillerStartingPosition(Position.of(0, 0));
    alternativeEffectGunFlowState.setFirstVictimPosition(Position.of(0, 1));

    context.nextPhase(
      viewMock,
      alternativeEffectGunFlowState
    );

    ArgumentCaptor<List<Position>> positionsCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(1)).showAvailableMovements(positionsCaptor.capture());
    assertThat(positionsCaptor.getValue())
      .containsOnly(
        new Position(0, 1),
        new Position(0, 2)
      );

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.handleEvent(new MovementChosenEvent(Position.of(0, 2)), viewMock);

    assertThat(receivedModelEvents)
      .haveExactly(1, isDashboardCellUpdatedEvent(0, 0))
      .haveExactly(1, isDashboardCellUpdatedEvent(0, 2));

    assertThat(model.getPlayerPosition(PlayerColor.GREEN))
      .isEqualTo(Position.of(0, 2));

    checkEndCalled();
  }
}
