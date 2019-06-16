package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.view.CellToHitChosenEvent;
import com.adrenalinici.adrenaline.server.controller.DecoratedAlternativeEffectGun;
import com.adrenalinici.adrenaline.server.controller.DecoratedBaseEffectGun;
import com.adrenalinici.adrenaline.server.controller.GunLoader;
import com.adrenalinici.adrenaline.server.controller.nodes.BaseNodeTest;
import com.adrenalinici.adrenaline.server.flow.FlowNode;
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
        new Position(1, 0),
        new Position(1, 1)
      );

    context.handleEvent(new CellToHitChosenEvent(Position.of(0, 1)), viewMock);

    assertThat(baseEffectGunFlowState.getChosenPlayersToHit())
      .containsExactlyInAnyOrder(PlayerColor.YELLOW, PlayerColor.GRAY);

    checkEndCalled();
  }

  @Test
  public void testShowCellsInOneDirection() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(1, 0)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(2, 0)).addPlayer(PlayerColor.GRAY);

    model.getPlayerDashboard(PlayerColor.GREEN).addGun("flamethrower");

    AlternativeEffectGunFlowState alternativeEffectGunFlowState =
      new AlternativeEffectGunFlowStateImpl((DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun("flamethrower"));
    alternativeEffectGunFlowState.setChosenEffect(alternativeEffectGunFlowState.getChosenGun().getSecondEffect(), false);

    ArgumentCaptor<Set<Position>> positionsCaptor = ArgumentCaptor.forClass(Set.class);

    context.nextPhase(
      viewMock,
      alternativeEffectGunFlowState
    );

    context.handleEvent(new CellToHitChosenEvent(Position.of(1, 0)), viewMock);
    context.handleEvent(new CellToHitChosenEvent(Position.of(2, 0)), viewMock);


    verify(viewMock, times(2)).showAvailableCellsToHit(positionsCaptor.capture());
    assertThat(positionsCaptor.getAllValues().get(0))
      .containsOnly(
        new Position(0, 1),
        new Position(1, 0)
      );
    assertThat(positionsCaptor.getAllValues().get(1))
      .containsOnly(
        new Position(2, 0)
      );

    assertThat(alternativeEffectGunFlowState.getChosenPlayersToHit())
      .containsExactlyInAnyOrder(PlayerColor.YELLOW, PlayerColor.GRAY);

    checkEndCalled();
  }
}
