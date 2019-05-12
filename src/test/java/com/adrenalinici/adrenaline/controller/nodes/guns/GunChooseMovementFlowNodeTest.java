package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.DecoratedBaseEffectGun;
import com.adrenalinici.adrenaline.controller.GunLoader;
import com.adrenalinici.adrenaline.controller.nodes.BaseNodeTest;
import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.Position;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.view.event.BaseGunEffectChosenEvent;
import com.adrenalinici.adrenaline.view.event.MovementChosenEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static com.adrenalinici.adrenaline.testutil.MyConditions.isDashboardCellUpdatedEvent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class GunChooseMovementFlowNodeTest extends BaseNodeTest {
  @Override
  @Before
  public void setUp() {
    super.setUp();
  }

  @Override
  public FlowNode nodeToTest() {
    return new GunChooseMovementFlowNode(2);
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
      new GunChooseMovementFlowNode.GunChooseMovementFlowState(baseEffectGunFlowState)
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
