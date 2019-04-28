package com.adrenalinici.adrenaline.controller.guns;

import com.adrenalinici.adrenaline.controller.DecoratedAlternativeEffectGun;
import com.adrenalinici.adrenaline.controller.GunFactory;
import com.adrenalinici.adrenaline.controller.nodes.guns.AlternativeEffectGunFlowState;
import com.adrenalinici.adrenaline.controller.nodes.guns.ChooseAlternativeEffectForGunFlowNode;
import com.adrenalinici.adrenaline.controller.nodes.guns.ChoosePlayersToHitFlowNode;
import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.model.PlayerDashboard;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.view.event.AlternativeGunEffectChosenEvent;
import com.adrenalinici.adrenaline.view.event.PlayerChosenEvent;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.adrenalinici.adrenaline.testutil.MyConditions.gunWithId;
import static com.adrenalinici.adrenaline.testutil.MyConditions.isPlayerDashboardUpdateEvent;
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
    PlayerDashboard playerDashboard = model.getPlayerDashboard(PlayerColor.GREEN);
    playerDashboard.addLoadedGun(gunLoader.getModelGun("zx2"));

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.nextPhase(viewMock, new AlternativeEffectGunFlowState((DecoratedAlternativeEffectGun) gunLoader.getDecoratedGun("zx2")));
    context.handleEvent(new AlternativeGunEffectChosenEvent(viewMock, false));
    context.handleEvent(new PlayerChosenEvent(viewMock, PlayerColor.GRAY));

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages())
      .containsExactly(PlayerColor.GREEN);

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getMarks())
      .containsExactly(PlayerColor.GREEN, PlayerColor.GREEN);

    assertThat(context.getKilledPlayers()).isEmpty();

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY, model));

    assertThat(playerDashboard.getLoadedGuns())
      .doNotHave(gunWithId("zx2"));

    assertThat(playerDashboard.getUnloadedGuns())
      .haveExactly(1, gunWithId("zx2"));
  }

  @Test
  public void testScannerEffect() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    PlayerDashboard playerDashboard = model.getPlayerDashboard(PlayerColor.GREEN);
    playerDashboard.addLoadedGun(gunLoader.getModelGun("zx2"));

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.nextPhase(viewMock, new AlternativeEffectGunFlowState((DecoratedAlternativeEffectGun) gunLoader.getDecoratedGun("zx2")));
    context.handleEvent(new AlternativeGunEffectChosenEvent(viewMock, true));
    context.handleEvent(new PlayerChosenEvent(viewMock, PlayerColor.GRAY));
    context.handleEvent(new PlayerChosenEvent(viewMock, PlayerColor.YELLOW));

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getMarks())
      .containsExactly(PlayerColor.GREEN);

    assertThat(model.getPlayerDashboard(PlayerColor.YELLOW).getMarks())
      .containsExactly(PlayerColor.GREEN);

    assertThat(context.getKilledPlayers()).isEmpty();

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY, model))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW, model));

    assertThat(playerDashboard.getLoadedGuns())
      .doNotHave(gunWithId("zx2"));

    assertThat(playerDashboard.getUnloadedGuns())
      .haveExactly(1, gunWithId("zx2"));

  }

}
