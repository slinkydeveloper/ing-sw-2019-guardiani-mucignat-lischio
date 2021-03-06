package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.common.model.Effect;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.view.BaseGunEffectChosenEvent;
import com.adrenalinici.adrenaline.server.controller.DecoratedBaseEffectGun;
import com.adrenalinici.adrenaline.server.controller.GunLoader;
import com.adrenalinici.adrenaline.server.controller.nodes.BaseNodeTest;
import com.adrenalinici.adrenaline.server.flow.FlowNode;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class ChooseBaseEffectGunFlowNodeTest extends BaseNodeTest {

  @Override
  public void setUp() {
    super.setUp();
  }

  @Override
  public FlowNode nodeToTest() {
    return new ChooseBaseEffectForGunFlowNode();
  }

  @Test
  public void testShowAvailableEffects() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getPlayerDashboard(PlayerColor.GREEN).addGun("machine_gun");

    context.nextPhase(
      viewMock,
      new BaseEffectGunFlowStateImpl((DecoratedBaseEffectGun) GunLoader.INSTANCE.getDecoratedGun("machine_gun"))
    );

    ArgumentCaptor<Effect> extraEffectOneCaptor = ArgumentCaptor.forClass(Effect.class);
    ArgumentCaptor<Effect> extraEffectTwoCaptor = ArgumentCaptor.forClass(Effect.class);
    verify(viewMock, times(1))
      .showAvailableExtraEffects(extraEffectOneCaptor.capture(), extraEffectTwoCaptor.capture());
    assertThat(extraEffectOneCaptor.getValue())
      .hasFieldOrPropertyWithValue("id", "focus");
    assertThat(extraEffectTwoCaptor.getValue())
      .hasFieldOrPropertyWithValue("id", "tripod");
  }

  @Test
  public void testNoExtraEffect() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getPlayerDashboard(PlayerColor.GREEN).addGun("machine_gun");
    BaseEffectGunFlowState state = new BaseEffectGunFlowStateImpl((DecoratedBaseEffectGun) GunLoader.INSTANCE.getDecoratedGun("machine_gun"));

    context.nextPhase(viewMock, state);
    context.handleEvent(new BaseGunEffectChosenEvent(false, false), viewMock);

    assertThat(state.isActivatedFirstExtraEffect()).isFalse();
    assertThat(state.isActivatedSecondExtraEffect()).isFalse();
  }

  @Test
  public void testOnlyExtraEffectOne() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getPlayerDashboard(PlayerColor.GREEN).addGun("machine_gun");
    BaseEffectGunFlowState state = new BaseEffectGunFlowStateImpl((DecoratedBaseEffectGun) GunLoader.INSTANCE.getDecoratedGun("machine_gun"));

    context.nextPhase(viewMock, state);
    context.handleEvent(new BaseGunEffectChosenEvent(true, false), viewMock);

    assertThat(state.isActivatedFirstExtraEffect()).isTrue();
    assertThat(state.isActivatedSecondExtraEffect()).isFalse();
  }

  @Test
  public void testAllExtraEffects() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getPlayerDashboard(PlayerColor.GREEN).addGun("machine_gun");
    BaseEffectGunFlowState state = new BaseEffectGunFlowStateImpl((DecoratedBaseEffectGun) GunLoader.INSTANCE.getDecoratedGun("machine_gun"));

    context.nextPhase(viewMock, state);
    context.handleEvent(new BaseGunEffectChosenEvent(true, true), viewMock);

    assertThat(state.isActivatedFirstExtraEffect()).isTrue();
    assertThat(state.isActivatedSecondExtraEffect()).isTrue();
  }

}
