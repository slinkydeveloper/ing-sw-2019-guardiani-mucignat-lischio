package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.GunLoader;
import com.adrenalinici.adrenaline.controller.guns.MachineGunGunFactory;
import com.adrenalinici.adrenaline.controller.guns.ZX2GunFactory;
import com.adrenalinici.adrenaline.controller.nodes.BaseNodeTest;
import com.adrenalinici.adrenaline.controller.nodes.ChooseGunFlowNode;
import com.adrenalinici.adrenaline.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.model.AlternativeEffectGun;
import com.adrenalinici.adrenaline.model.Gun;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.view.event.GunChosenEvent;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class ChooseGunNodeFlowTest extends BaseNodeTest {


  @Override
  public void setUp() {
    super.setUp();
  }

  @Override
  protected GunLoader createGunLoader() {
    return new GunLoader(
      Arrays.asList(new ZX2GunFactory(), new MachineGunGunFactory())
    );
  }

  @Override
  public FlowNode nodeToTest() {
    return new ChooseGunFlowNode();
  }

  @Test
  public void testShowAvailableGuns() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getPlayerDashboard(PlayerColor.GREEN).addLoadedGun(gunLoader.getModelGun("zx2"));

    orchestrator.startNewFlow(viewMock, context);

    ArgumentCaptor<List<Gun>> gunsCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(1)).showAvailableGuns(gunsCaptor.capture());
    assertThat(gunsCaptor.getValue())
      .hasOnlyOneElementSatisfying(g ->
        assertThat(g)
          .isInstanceOf(AlternativeEffectGun.class)
          .hasFieldOrPropertyWithValue("id", "zx2")
      );
  }

  @Test
  public void testChooseAlternativeEffectGun() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getPlayerDashboard(PlayerColor.GREEN).addLoadedGun(gunLoader.getModelGun("zx2"));

    orchestrator.startNewFlow(viewMock, context);

    orchestrator.handleEvent(new GunChosenEvent(viewMock, "zx2"));

    assertThat(context.getPhasesQueue().get(0))
      .isEqualTo(ControllerNodes.ALTERNATIVE_GUN_START.name());

    assertThat(context.getActualState())
      .isInstanceOf(AlternativeEffectGunFlowState.class);

  }

  @Test
  public void testChooseBaseEffectGun() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getPlayerDashboard(PlayerColor.GREEN).addLoadedGun(gunLoader.getModelGun("machine_gun"));

    orchestrator.startNewFlow(viewMock, context);

    orchestrator.handleEvent(new GunChosenEvent(viewMock, "machine_gun"));

    assertThat(context.getPhasesQueue().get(0))
      .isEqualTo(ControllerNodes.BASE_GUN_START.name());

    assertThat(context.getActualState())
      .isInstanceOf(BaseEffectGunFlowState.class);

  }

  //TODO P2 choose base gun test

}
