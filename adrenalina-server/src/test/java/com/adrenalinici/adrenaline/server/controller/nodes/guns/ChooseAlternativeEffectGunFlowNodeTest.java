package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.common.model.AmmoColor;
import com.adrenalinici.adrenaline.common.model.Effect;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.view.AlternativeGunEffectChosenEvent;
import com.adrenalinici.adrenaline.server.controller.DecoratedAlternativeEffectGun;
import com.adrenalinici.adrenaline.server.controller.GunLoader;
import com.adrenalinici.adrenaline.server.controller.nodes.BaseNodeTest;
import com.adrenalinici.adrenaline.server.flow.FlowNode;
import org.assertj.core.data.Index;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class ChooseAlternativeEffectGunFlowNodeTest extends BaseNodeTest {

  @Override
  public void setUp() {
    super.setUp();
  }

  @Override
  public FlowNode nodeToTest() {
    return new ChooseAlternativeEffectForGunFlowNode();
  }

  @Test
  public void testShowAvailableEffects() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getPlayerDashboard(PlayerColor.GREEN).addGun("zx2");

    context.nextPhase(
      viewMock,
      new AlternativeEffectGunFlowStateImpl((DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun("zx2"))
    );

    ArgumentCaptor<Effect> effectOneCaptor = ArgumentCaptor.forClass(Effect.class);
    ArgumentCaptor<Effect> effectTwoCaptor = ArgumentCaptor.forClass(Effect.class);
    verify(viewMock, times(1))
      .showAvailableAlternativeEffectsGun(effectOneCaptor.capture(), effectTwoCaptor.capture());
    assertThat(effectOneCaptor.getValue())
      .hasFieldOrPropertyWithValue("id", "base");
    assertThat(effectTwoCaptor.getValue())
      .hasFieldOrPropertyWithValue("id", "scanner");
  }

  @Test
  public void testChooseEffectOne() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getPlayerDashboard(PlayerColor.GREEN).addGun("zx2");

    context.nextPhase(
      viewMock,
      new AlternativeEffectGunFlowStateImpl((DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun("zx2"))
    );
    context.handleEvent(new AlternativeGunEffectChosenEvent(false), viewMock);

    assertThat(context.getActualState())
      .extracting("chosenEffect")
      .satisfies(effect ->
          assertThat(effect)
            .hasFieldOrPropertyWithValue("id", "base"),
        Index.atIndex(0)
      );
  }

  @Test
  public void testChooseEffectTwo() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getPlayerDashboard(PlayerColor.GREEN).addGun("zx2");

    context.nextPhase(
      viewMock,
      new AlternativeEffectGunFlowStateImpl((DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun("zx2"))
    );
    context.handleEvent(new AlternativeGunEffectChosenEvent(true), viewMock);

    assertThat(context.getActualState())
      .extracting("chosenEffect")
      .satisfies(effect ->
          assertThat(effect)
            .hasFieldOrPropertyWithValue("id", "scanner"),
        Index.atIndex(0)
      );
  }

  @Test
  public void testOnlyBaseEffectAvailable() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getPlayerDashboard(PlayerColor.GREEN).removeAmmos(Arrays.asList(AmmoColor.YELLOW, AmmoColor.RED, AmmoColor.BLUE));
    model.getPlayerDashboard(PlayerColor.GREEN).addGun("tractor_beam");

    context.nextPhase(
      viewMock,
      new AlternativeEffectGunFlowStateImpl((DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun("tractor_beam"))
    );

    checkEndCalled();

    assertThat(context.getPhasesQueue())
      .containsExactly(
        "gun_enemy_movement_2",
        "apply_gun_effect_tractor_beam_base"
      );

    assertThat(context.getActualState())
      .extracting("chosenEffect")
      .satisfies(effect ->
          assertThat(effect)
            .hasFieldOrPropertyWithValue("id", "base"),
        Index.atIndex(0)
      );
  }

}
