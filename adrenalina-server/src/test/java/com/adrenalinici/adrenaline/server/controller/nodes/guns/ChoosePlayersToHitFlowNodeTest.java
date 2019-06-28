package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.common.model.AmmoColor;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.view.PlayerChosenEvent;
import com.adrenalinici.adrenaline.server.controller.DecoratedAlternativeEffectGun;
import com.adrenalinici.adrenaline.server.controller.DecoratedBaseEffectGun;
import com.adrenalinici.adrenaline.server.controller.GunLoader;
import com.adrenalinici.adrenaline.server.controller.nodes.BaseNodeTest;
import com.adrenalinici.adrenaline.server.flow.FlowNode;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class ChoosePlayersToHitFlowNodeTest extends BaseNodeTest { //TODO test for flag ON_CHOSEN_CELL after visible fixing

  @Override
  public void setUp() {
    super.setUp();
  }

  @Override
  public FlowNode nodeToTest() {
    return new ChoosePlayersToHitFlowNode();
  }

  @Test
  public void testChooseSinglePlayer() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(1, 1)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(1, 2)).addPlayer(PlayerColor.CYAN);

    DecoratedAlternativeEffectGun gun = (DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun("zx2");
    context.nextPhase(viewMock,
      new AlternativeEffectGunFlowStateImpl(gun).setChosenEffect(gun.getFirstEffect(), true)
    );

    ArgumentCaptor<List<PlayerColor>> playersCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(1)).showChoosePlayerToHit(playersCaptor.capture());
    assertThat(playersCaptor.getValue())
      .containsExactlyInAnyOrder(PlayerColor.GRAY, PlayerColor.YELLOW);

    context.handleEvent(new PlayerChosenEvent(PlayerColor.GRAY), viewMock);

    assertThat(((AlternativeEffectGunFlowState) context.getActualState()).getChosenPlayersToHit())
      .containsExactlyInAnyOrder(PlayerColor.GRAY);

    checkEndCalled();
  }

  @Test
  public void testChooseMultiplePlayers() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(1, 1)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(2, 1)).addPlayer(PlayerColor.CYAN);

    DecoratedAlternativeEffectGun gun = (DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun("zx2");

    ArgumentCaptor<List<PlayerColor>> playersCaptor = ArgumentCaptor.forClass(List.class);

    context.nextPhase(viewMock,
      new AlternativeEffectGunFlowStateImpl(gun).setChosenEffect(gun.getSecondEffect(), false)
    );

    context.handleEvent(new PlayerChosenEvent(PlayerColor.GRAY), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.YELLOW), viewMock);

    verify(viewMock, times(2)).showChoosePlayerToHit(playersCaptor.capture());
    assertThat(playersCaptor.getAllValues().get(0))
      .containsExactlyInAnyOrder(PlayerColor.GRAY, PlayerColor.YELLOW);
    assertThat(playersCaptor.getAllValues().get(1))
      .containsExactlyInAnyOrder(PlayerColor.YELLOW);

    assertThat(((AlternativeEffectGunFlowState) context.getActualState()).getChosenPlayersToHit())
      .containsExactlyInAnyOrder(PlayerColor.GRAY, PlayerColor.YELLOW);

    checkEndCalled();
  }

  @Test
  public void testForThorRestrictions() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(1, 2)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(2, 1)).addPlayer(PlayerColor.CYAN);
    model.getPlayerDashboard(PlayerColor.GREEN).addAmmo(AmmoColor.BLUE);
    assertThat(model.getDashboard().calculateIfVisible(Position.of(2, 2), Position.of(2, 1)))
      .isTrue();

    DecoratedBaseEffectGun gun = (DecoratedBaseEffectGun) GunLoader.INSTANCE.getDecoratedGun("thor");
    BaseEffectGunFlowStateImpl baseEffectGunFlowState = new BaseEffectGunFlowStateImpl(gun);
    baseEffectGunFlowState.setActivatedFirstExtraEffect(true).setActivatedSecondExtraEffect(true);

    ArgumentCaptor<List<PlayerColor>> playersCaptor = ArgumentCaptor.forClass(List.class);

    context.nextPhase(
      viewMock,
      baseEffectGunFlowState
    );

    context.handleEvent(new PlayerChosenEvent(PlayerColor.GRAY), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.YELLOW), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.CYAN), viewMock);

    verify(viewMock, times(3)).showChoosePlayerToHit(playersCaptor.capture());
    assertThat(playersCaptor.getAllValues().get(0))
      .containsExactlyInAnyOrder(PlayerColor.GRAY);
    assertThat(playersCaptor.getAllValues().get(1))
      .containsExactlyInAnyOrder(PlayerColor.YELLOW);
    assertThat(playersCaptor.getAllValues().get(2))
      .containsExactlyInAnyOrder(PlayerColor.CYAN);

    assertThat(((BaseEffectGunFlowState) context.getActualState()).getChosenPlayersToHit())
      .containsExactlyInAnyOrder(PlayerColor.GRAY, PlayerColor.YELLOW, PlayerColor.CYAN);

    checkEndCalled();
  }

  @Test
  public void testForRailgunRestrictionsWithFirstEnemyOnKillerCell() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(0, 2)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(2, 1)).addPlayer(PlayerColor.CYAN);

    DecoratedAlternativeEffectGun gun = (DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun("railgun");
    AlternativeEffectGunFlowStateImpl alternativeEffectGunFlowState = new AlternativeEffectGunFlowStateImpl(gun);
    alternativeEffectGunFlowState.setChosenEffect(gun.getSecondEffect(), false);

    ArgumentCaptor<List<PlayerColor>> playersCaptor = ArgumentCaptor.forClass(List.class);

    context.nextPhase(
      viewMock,
      alternativeEffectGunFlowState
    );

    context.handleEvent(new PlayerChosenEvent(PlayerColor.GRAY), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.YELLOW), viewMock);

    verify(viewMock, times(2)).showChoosePlayerToHit(playersCaptor.capture());
    assertThat(playersCaptor.getAllValues().get(0))
      .containsExactlyInAnyOrder(PlayerColor.GRAY, PlayerColor.YELLOW);
    assertThat(playersCaptor.getAllValues().get(1))
      .containsExactlyInAnyOrder(PlayerColor.YELLOW);

    assertThat(((AlternativeEffectGunFlowState) context.getActualState()).getChosenPlayersToHit())
      .containsExactlyInAnyOrder(PlayerColor.GRAY, PlayerColor.YELLOW);

    checkEndCalled();
  }

  @Test
  public void testForRailgunRestrictions() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(2, 2)).addPlayer(PlayerColor.CYAN);

    DecoratedAlternativeEffectGun gun = (DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun("railgun");
    AlternativeEffectGunFlowStateImpl alternativeEffectGunFlowState = new AlternativeEffectGunFlowStateImpl(gun);
    alternativeEffectGunFlowState.setChosenEffect(gun.getSecondEffect(), false);

    ArgumentCaptor<List<PlayerColor>> playersCaptor = ArgumentCaptor.forClass(List.class);

    context.nextPhase(
      viewMock,
      alternativeEffectGunFlowState
    );

    context.handleEvent(new PlayerChosenEvent(PlayerColor.GRAY), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.YELLOW), viewMock);

    verify(viewMock, times(2)).showChoosePlayerToHit(playersCaptor.capture());
    assertThat(playersCaptor.getAllValues().get(0))
      .containsExactlyInAnyOrder(PlayerColor.GRAY, PlayerColor.YELLOW);
    assertThat(playersCaptor.getAllValues().get(1))
      .containsExactlyInAnyOrder(PlayerColor.YELLOW);

    assertThat(((AlternativeEffectGunFlowState) context.getActualState()).getChosenPlayersToHit())
      .containsExactlyInAnyOrder(PlayerColor.GRAY, PlayerColor.YELLOW);

    checkEndCalled();
  }

  @Test
  public void testForShockwaveRestrictions() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(1, 0)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.CYAN);

    DecoratedAlternativeEffectGun gun = (DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun("shockwave");
    AlternativeEffectGunFlowStateImpl alternativeEffectGunFlowState = new AlternativeEffectGunFlowStateImpl(gun);
    alternativeEffectGunFlowState.setChosenEffect(gun.getFirstEffect(), true);

    ArgumentCaptor<List<PlayerColor>> playersCaptor = ArgumentCaptor.forClass(List.class);

    context.nextPhase(
      viewMock,
      alternativeEffectGunFlowState
    );

    context.handleEvent(new PlayerChosenEvent(PlayerColor.GRAY), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.YELLOW), viewMock);

    verify(viewMock, times(2)).showChoosePlayerToHit(playersCaptor.capture());
    assertThat(playersCaptor.getAllValues().get(0))
      .containsExactlyInAnyOrder(PlayerColor.GRAY, PlayerColor.YELLOW);
    assertThat(playersCaptor.getAllValues().get(1))
      .containsExactlyInAnyOrder(PlayerColor.YELLOW);

    assertThat(((AlternativeEffectGunFlowState) context.getActualState()).getChosenPlayersToHit())
      .containsExactlyInAnyOrder(PlayerColor.GRAY, PlayerColor.YELLOW);
  }

  @Test
  public void testOnChosenCellRestriction() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(1, 2)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(2, 1)).addPlayer(PlayerColor.CYAN);

    DecoratedBaseEffectGun gun = (DecoratedBaseEffectGun) GunLoader.INSTANCE.getDecoratedGun("vortex_cannon");
    BaseEffectGunFlowStateImpl baseEffectGunFlowState = new BaseEffectGunFlowStateImpl(gun);
    baseEffectGunFlowState.setActivatedFirstExtraEffect(false).setActivatedSecondExtraEffect(false);
    baseEffectGunFlowState.getChosenCellsToHit().add(Position.of(0, 1));

    ArgumentCaptor<List<PlayerColor>> playersCaptor = ArgumentCaptor.forClass(List.class);

    context.nextPhase(
      viewMock,
      baseEffectGunFlowState
    );

    verify(viewMock, times(1)).showChoosePlayerToHit(playersCaptor.capture());
    assertThat(playersCaptor.getValue())
      .containsOnly(PlayerColor.GRAY);
  }
}
