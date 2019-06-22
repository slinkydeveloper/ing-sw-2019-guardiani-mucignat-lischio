package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.model.event.ModelEvent;
import com.adrenalinici.adrenaline.common.view.MovementChosenEvent;
import com.adrenalinici.adrenaline.server.controller.DecoratedBaseEffectGun;
import com.adrenalinici.adrenaline.server.controller.GunLoader;
import com.adrenalinici.adrenaline.server.controller.nodes.BaseNodeTest;
import com.adrenalinici.adrenaline.server.flow.FlowNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static com.adrenalinici.adrenaline.server.testutil.MyConditions.isDashboardCellUpdatedEvent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class BaseGunChooseMovementFlowNodeTest extends BaseNodeTest {
  @Override
  @Before
  public void setUp() {
    super.setUp();
  }

  @Override
  public FlowNode nodeToTest() {
    return new BaseGunChooseMovementFlowNode(2);
  }

  @Test
  public void testShowAvailableMovements() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(2, 1)).addPlayer(PlayerColor.GREEN);
    model.getPlayerDashboard(PlayerColor.GREEN).addGun("plasma_gun");

    BaseEffectGunFlowState baseEffectGunFlowState =
      new BaseEffectGunFlowStateImpl((DecoratedBaseEffectGun) GunLoader.INSTANCE.getDecoratedGun("plasma_gun"));
    baseEffectGunFlowState.setActivatedFirstExtraEffect(true);

    context.nextPhase(
      viewMock,
      new BaseGunChooseMovementFlowNode.GunChooseMovementFlowState(baseEffectGunFlowState)
    );

    ArgumentCaptor<List<Position>> positionsCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(1)).showAvailableMovements(positionsCaptor.capture());
    assertThat(positionsCaptor.getValue())
      .containsOnly(
        new Position(1, 0),
        new Position(1, 2),
        new Position(2, 0),
        new Position(2, 1),
        new Position(2, 2)
      );

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.handleEvent(new MovementChosenEvent(Position.of(2, 0)), viewMock);

    assertThat(receivedModelEvents)
      .haveExactly(1, isDashboardCellUpdatedEvent(2, 1))
      .haveExactly(1, isDashboardCellUpdatedEvent(2, 0));

    assertThat(model.getPlayerPosition(PlayerColor.GREEN))
      .isEqualTo(Position.of(2, 0));

    checkEndCalled();
  }
}
