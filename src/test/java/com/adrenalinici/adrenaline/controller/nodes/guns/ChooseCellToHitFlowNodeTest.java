package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.DecoratedAlternativeEffectGun;
import com.adrenalinici.adrenaline.controller.DecoratedBaseEffectGun;
import com.adrenalinici.adrenaline.controller.GunLoader;
import com.adrenalinici.adrenaline.controller.nodes.BaseNodeTest;
import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.Position;
import com.adrenalinici.adrenaline.view.event.CellToHitChosenEvent;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class ChooseCellToHitFlowNodeTest extends BaseNodeTest {
  @Override
  public void setUp() {
    super.setUp();
  }

  @Override
  public FlowNode nodeToTest() {
    return new ChooseCellToHitFlowNode();
  }

  @Test
  public void testShowAvailableCells() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.GRAY);

    model.getPlayerDashboard(PlayerColor.GREEN).addGun("furnace");

    AlternativeEffectGunFlowState alternativeEffectGunFlowState =
      new AlternativeEffectGunFlowStateImpl((DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun("furnace"));
    alternativeEffectGunFlowState.setChosenEffect(alternativeEffectGunFlowState.getChosenGun().getSecondEffect(), false);

    context.nextPhase(
      viewMock,
      alternativeEffectGunFlowState
    );

    ArgumentCaptor<Set<Position>> positionsCaptor = ArgumentCaptor.forClass(Set.class);
    verify(viewMock, times(1)).showAvailableCellsToHit(positionsCaptor.capture());
    assertThat(positionsCaptor.getValue())
      .containsOnly(
        new Position(0, 1),
        new Position(1, 0)
      );

    context.handleEvent(new CellToHitChosenEvent(Position.of(0, 1)), viewMock);

    assertThat(alternativeEffectGunFlowState.getChosenPlayersToHit())
      .containsExactlyInAnyOrder(PlayerColor.YELLOW, PlayerColor.GRAY);

    checkEndCalled();
  }

  @Test
  public void testShowVisibleCells() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.GRAY);

    model.getPlayerDashboard(PlayerColor.GREEN).addGun("grenade_launcher");

    BaseEffectGunFlowState baseEffectGunFlowState =
      new BaseEffectGunFlowStateImpl((DecoratedBaseEffectGun) GunLoader.INSTANCE.getDecoratedGun("grenade_launcher"));
    baseEffectGunFlowState.setActivatedFirstExtraEffect(true);

    context.nextPhase(
      viewMock,
      baseEffectGunFlowState
    );

    ArgumentCaptor<Set<Position>> positionsCaptor = ArgumentCaptor.forClass(Set.class);
    verify(viewMock, times(1)).showAvailableCellsToHit(positionsCaptor.capture());
    assertThat(positionsCaptor.getValue())
      .containsOnly(
        new Position(0, 0),
        new Position(0, 1),
        new Position(0, 2),
        new Position(1, 0),
        new Position(2, 0)

      );

    context.handleEvent(new CellToHitChosenEvent(Position.of(0, 1)), viewMock);

    assertThat(baseEffectGunFlowState.getChosenPlayersToHit())
      .containsExactlyInAnyOrder(PlayerColor.YELLOW, PlayerColor.GRAY);

    checkEndCalled();
  }
}