package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.nodes.BaseNodeTest;
import com.adrenalinici.adrenaline.controller.nodes.ChooseGunFlowNode;
import com.adrenalinici.adrenaline.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.view.event.GunChosenEvent;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class ChooseGunFlowNodeTest extends BaseNodeTest {


  @Override
  public void setUp() {
    super.setUp();
  }

  @Override
  public FlowNode nodeToTest() {
    return new ChooseGunFlowNode();
  }

  @Test
  public void testShowAvailableGuns() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getPlayerDashboard(PlayerColor.GREEN).addGun("zx2");

    orchestrator.startNewFlow(viewMock, context);

    ArgumentCaptor<Set<String>> gunsCaptor = ArgumentCaptor.forClass(Set.class);
    verify(viewMock, times(1)).showAvailableGuns(gunsCaptor.capture());
    assertThat(gunsCaptor.getValue())
      .containsOnly("zx2");
  }

  @Test
  public void testChooseAlternativeEffectGun() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getPlayerDashboard(PlayerColor.GREEN).addGun("zx2");

    orchestrator.startNewFlow(viewMock, context);

    orchestrator.handleEvent(new GunChosenEvent("zx2"), viewMock);

    assertThat(context.getPhasesQueue().get(0))
      .isEqualTo(ControllerNodes.ALTERNATIVE_GUN_START.name());

    assertThat(context.getActualState())
      .isInstanceOf(AlternativeEffectGunFlowStateImpl.class);

  }

  @Test
  public void testChooseBaseEffectGun() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getPlayerDashboard(PlayerColor.GREEN).addGun("machine_gun");

    orchestrator.startNewFlow(viewMock, context);

    orchestrator.handleEvent(new GunChosenEvent("machine_gun"), viewMock);

    assertThat(context.getPhasesQueue().get(0))
      .isEqualTo(ControllerNodes.BASE_GUN_START.name());

    assertThat(context.getActualState())
      .isInstanceOf(BaseEffectGunFlowStateImpl.class);

  }

  //TODO P2 choose base gun test

}