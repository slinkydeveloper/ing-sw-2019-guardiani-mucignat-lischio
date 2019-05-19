package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.DecoratedAlternativeEffectGun;
import com.adrenalinici.adrenaline.controller.GunLoader;
import com.adrenalinici.adrenaline.controller.nodes.BaseNodeTest;
import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.Position;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.view.event.EnemyMovementChosenEvent;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static com.adrenalinici.adrenaline.testutil.MyConditions.isDashboardCellUpdatedEvent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class GunChooseEnemyMovementFlowNodeModeVisibleSquares extends BaseNodeTest {
  @Override
  public FlowNode nodeToTest() {
    return new GunChooseEnemyMovementFlowNode(2);
  }

  @Test
  public void testShowAvailableMovements() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(1, 2)).addPlayer(PlayerColor.YELLOW);
    model.getPlayerDashboard(PlayerColor.GREEN).addGun("tractor_beam");

    AlternativeEffectGunFlowState alternativeEffectGunFlowState =
      new AlternativeEffectGunFlowStateImpl((DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun("tractor_beam"));
    alternativeEffectGunFlowState.setChosenEffect(alternativeEffectGunFlowState.getChosenGun().getFirstEffect(), true);
    alternativeEffectGunFlowState.getChosenPlayersToHit().add(PlayerColor.YELLOW);

    context.nextPhase(
      viewMock,
      alternativeEffectGunFlowState
    );

    ArgumentCaptor<List<Position>> positionsCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(1)).showAvailableMovements(positionsCaptor.capture());
    assertThat(positionsCaptor.getValue())
      .containsOnly(
        new Position(0, 1),
        new Position(0, 2),
        new Position(1, 0)
      );

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.handleEvent(new EnemyMovementChosenEvent(Position.of(1, 0), PlayerColor.YELLOW), viewMock);

    assertThat(receivedModelEvents)
      .haveExactly(1, isDashboardCellUpdatedEvent(1, 2))
      .haveExactly(1, isDashboardCellUpdatedEvent(1, 0));

    assertThat(model.getPlayerPosition(PlayerColor.YELLOW))
      .isEqualTo(Position.of(1, 0));

    checkEndCalled();
  }
}
