package com.adrenalinici.adrenaline.controller.guns;

import com.adrenalinici.adrenaline.controller.GunFactory;
import com.adrenalinici.adrenaline.controller.nodes.guns.ChooseAlternativeEffectForGunFlowNode;
import com.adrenalinici.adrenaline.controller.nodes.guns.ChoosePlayersToHitFlowNode;
import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.view.event.AlternativeGunEffectChosenEvent;
import com.adrenalinici.adrenaline.view.event.PlayerChosenEvent;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ZX2Test extends BaseGunTest {
  @Override
  protected GunFactory gunFactory() {
    return new ZX2GunFactory();
  }

  @Override
  protected List<FlowNode> nodes() {
    return Arrays.asList(
      new ChooseAlternativeEffectForGunFlowNode(),
      new ChoosePlayersToHitFlowNode()
    );
  }

  @Override
  protected String gunId() {
    return "zx2";
  }

  @Test
  public void testBaseEffect() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getPlayerDashboard(PlayerColor.GREEN).addLoadedGun(gunLoader.getModelGun("zx2"));

    orchestrator.startNewFlow(viewMock, context);
    orchestrator.handleEvent(new AlternativeGunEffectChosenEvent(viewMock, false));
    orchestrator.handleEvent(new PlayerChosenEvent(viewMock, PlayerColor.GRAY));

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages())
      .containsExactly(PlayerColor.GREEN);

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getMarks())
      .containsExactly(PlayerColor.GREEN, PlayerColor.GREEN);
  }

  @Test
  public void testScannerEffect() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    model.getPlayerDashboard(PlayerColor.GREEN).addLoadedGun(gunLoader.getModelGun("zx2"));

    orchestrator.startNewFlow(viewMock, context);
    orchestrator.handleEvent(new AlternativeGunEffectChosenEvent(viewMock, true));
    orchestrator.handleEvent(new PlayerChosenEvent(viewMock, PlayerColor.GRAY));
    orchestrator.handleEvent(new PlayerChosenEvent(viewMock, PlayerColor.YELLOW));

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getMarks())
      .containsExactly(PlayerColor.GREEN);

    assertThat(model.getPlayerDashboard(PlayerColor.YELLOW).getMarks())
      .containsExactly(PlayerColor.GREEN);
  }

}
