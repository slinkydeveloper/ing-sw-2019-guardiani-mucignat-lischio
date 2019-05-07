package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.DecoratedAlternativeEffectGun;
import com.adrenalinici.adrenaline.controller.GunLoader;
import com.adrenalinici.adrenaline.controller.guns.ZX2GunFactory;
import com.adrenalinici.adrenaline.controller.nodes.BaseNodeTest;
import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.model.Effect;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.view.event.AlternativeGunEffectChosenEvent;
import org.assertj.core.data.Index;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class ChooseAlternativeEffectGunNodeFlowTest extends BaseNodeTest {


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
      new AlternativeEffectGunFlowState((DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun("zx2"))
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
      new AlternativeEffectGunFlowState((DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun("zx2"))
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
      new AlternativeEffectGunFlowState((DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun("zx2"))
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

}
