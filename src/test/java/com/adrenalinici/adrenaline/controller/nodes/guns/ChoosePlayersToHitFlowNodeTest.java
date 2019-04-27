package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.DecoratedAlternativeEffectGun;
import com.adrenalinici.adrenaline.controller.GunLoader;
import com.adrenalinici.adrenaline.controller.guns.ZX2GunFactory;
import com.adrenalinici.adrenaline.controller.nodes.BaseNodeTest;
import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.model.PlayerColor;
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
  protected GunLoader createGunLoader() {
    return new GunLoader(
      Collections.singletonList(new ZX2GunFactory())
    );
  }

  @Override
  public FlowNode nodeToTest() {
    return new ChoosePlayersToHitFlowNode();
  }

  @Test
  public void testChooseSinglePlayer() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    DecoratedAlternativeEffectGun gun = (DecoratedAlternativeEffectGun) gunLoader.getDecoratedGun("zx2");

    context.nextPhase(viewMock,
      new AlternativeEffectGunFlowState(gun).setChosenEffect(gun.getFirstEffect(), true)
    );

    ArgumentCaptor<List<PlayerColor>> playersCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(1)).showChoosePlayerToHit(playersCaptor.capture());
    assertThat(playersCaptor.getValue())
      .containsExactlyInAnyOrder(PlayerColor.GRAY, PlayerColor.YELLOW);

    context.handleEvent(new PlayerChosenEvent(viewMock, PlayerColor.GRAY));

    assertThat(((AlternativeEffectGunFlowState) context.getActualState()).getChosenPlayersToHit())
      .containsExactlyInAnyOrder(PlayerColor.GRAY);

    checkEndCalled();
  }

  @Test
  public void testChooseMultiplePlayers() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    DecoratedAlternativeEffectGun gun = (DecoratedAlternativeEffectGun) gunLoader.getDecoratedGun("zx2");

    ArgumentCaptor<List<PlayerColor>> playersCaptor = ArgumentCaptor.forClass(List.class);

    context.nextPhase(viewMock,
      new AlternativeEffectGunFlowState(gun).setChosenEffect(gun.getSecondEffect(), false)
    );

    context.handleEvent(new PlayerChosenEvent(viewMock, PlayerColor.GRAY));
    context.handleEvent(new PlayerChosenEvent(viewMock, PlayerColor.YELLOW));

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
