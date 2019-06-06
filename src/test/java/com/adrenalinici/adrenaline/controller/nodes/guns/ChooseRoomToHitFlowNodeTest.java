package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.DecoratedAlternativeEffectGun;
import com.adrenalinici.adrenaline.controller.GunLoader;
import com.adrenalinici.adrenaline.controller.nodes.BaseNodeTest;
import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.model.common.CellColor;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.Position;
import com.adrenalinici.adrenaline.view.event.RoomChosenEvent;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class ChooseRoomToHitFlowNodeTest extends BaseNodeTest {
  @Override
  public void setUp() {
    super.setUp();
  }

  @Override
  public FlowNode nodeToTest() {
    return new ChooseRoomToHitFlowNode();
  }

  @Test
  public void testShowAvailableCells() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 2)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(1, 2)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(1, 1)).addPlayer(PlayerColor.CYAN);

    model.getPlayerDashboard(PlayerColor.GREEN).addGun("furnace");

    AlternativeEffectGunFlowState alternativeEffectGunFlowState =
      new AlternativeEffectGunFlowStateImpl((DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun("furnace"));
    alternativeEffectGunFlowState.setChosenEffect(alternativeEffectGunFlowState.getChosenGun().getFirstEffect(), true);

    context.nextPhase(
      viewMock,
      alternativeEffectGunFlowState
    );

    ArgumentCaptor<Set<CellColor>> roomsCaptor = ArgumentCaptor.forClass(Set.class);
    verify(viewMock, times(1)).showAvailableRooms(roomsCaptor.capture());
    assertThat(roomsCaptor.getValue())
      .containsOnly(
        CellColor.YELLOW,
        CellColor.GRAY
      );

    context.handleEvent(new RoomChosenEvent(CellColor.YELLOW), viewMock);

    assertThat(alternativeEffectGunFlowState.getChosenPlayersToHit())
      .containsExactlyInAnyOrder(PlayerColor.YELLOW, PlayerColor.GRAY);

    checkEndCalled();
  }
}
