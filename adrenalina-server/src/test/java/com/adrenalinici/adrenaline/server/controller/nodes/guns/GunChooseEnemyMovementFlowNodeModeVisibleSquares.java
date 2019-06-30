package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.model.event.ModelEvent;
import com.adrenalinici.adrenaline.common.view.EnemyMovementChosenEvent;
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
    verify(viewMock, times(1)).showAvailableEnemyMovements(positionsCaptor.capture());
    assertThat(positionsCaptor.getValue())
      .containsOnly(
        new Position(0, 1),
        new Position(0, 2),
        new Position(1, 0)
      );

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.handleEvent(new EnemyMovementChosenEvent(Position.of(1, 0)), viewMock);

    assertThat(receivedModelEvents)
      .haveExactly(1, isDashboardCellUpdatedEvent(1, 2))
      .haveExactly(1, isDashboardCellUpdatedEvent(1, 0));

    assertThat(model.getPlayerPosition(PlayerColor.YELLOW))
      .isEqualTo(Position.of(1, 0));

    checkEndCalled();
  }
}
