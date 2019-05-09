package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.DecoratedAlternativeEffectGun;
import com.adrenalinici.adrenaline.controller.GunLoader;
import com.adrenalinici.adrenaline.controller.guns.ZX2GunFactory;
import com.adrenalinici.adrenaline.controller.nodes.BaseNodeTest;
import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.Position;
import com.adrenalinici.adrenaline.view.event.PlayerChosenEvent;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class ChoosePlayersToHitFlowNodeTest extends BaseNodeTest {

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
    model.getDashboard().getDashboardCell(Position.of(0, 2)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(2, 0)).addPlayer(PlayerColor.YELLOW);

    DecoratedAlternativeEffectGun gun = (DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun("zx2");
    context.nextPhase(viewMock,
      new AlternativeEffectGunFlowState(gun).setChosenEffect(gun.getFirstEffect(), true)
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
    model.getDashboard().getDashboardCell(Position.of(0, 2)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(2, 0)).addPlayer(PlayerColor.YELLOW);

    DecoratedAlternativeEffectGun gun = (DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun("zx2");

    ArgumentCaptor<List<PlayerColor>> playersCaptor = ArgumentCaptor.forClass(List.class);

    context.nextPhase(viewMock,
      new AlternativeEffectGunFlowState(gun).setChosenEffect(gun.getSecondEffect(), false)
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

}
