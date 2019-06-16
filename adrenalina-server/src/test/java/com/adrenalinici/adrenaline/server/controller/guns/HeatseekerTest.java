package com.adrenalinici.adrenaline.server.controller.guns;

import com.adrenalinici.adrenaline.common.model.AmmoColor;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.model.event.ModelEvent;
import com.adrenalinici.adrenaline.common.view.BaseGunEffectChosenEvent;
import com.adrenalinici.adrenaline.common.view.PlayerChosenEvent;
import com.adrenalinici.adrenaline.server.controller.DecoratedBaseEffectGun;
import com.adrenalinici.adrenaline.server.controller.GunLoader;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.BaseEffectGunFlowStateImpl;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.ChooseBaseEffectForGunFlowNode;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.ChoosePlayersToHitFlowNode;
import com.adrenalinici.adrenaline.server.flow.FlowNode;
import com.adrenalinici.adrenaline.server.model.PlayerDashboard;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.adrenalinici.adrenaline.server.testutil.MyConditions.isPlayerDashboardUpdateEvent;
import static org.assertj.core.api.Assertions.assertThat;

public class HeatseekerTest extends BaseGunTest {
  @Override
  protected List<FlowNode> nodes() {
    return Arrays.asList(
      new ChooseBaseEffectForGunFlowNode(),
      new ChoosePlayersToHitFlowNode()
    );
  }

  @Override
  protected String gunId() {
    return "heatseeker";
  }

  @Test
  public void testBaseEffect() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(2, 2)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(2, 0)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(2, 0)).addPlayer(PlayerColor.CYAN);

    PlayerDashboard playerDashboard = model.getPlayerDashboard(PlayerColor.GREEN);
    playerDashboard.addGun(gunId());

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.nextPhase(
      viewMock,
      new BaseEffectGunFlowStateImpl((DecoratedBaseEffectGun) GunLoader.INSTANCE.getDecoratedGun(gunId()))
    );
    context.handleEvent(new BaseGunEffectChosenEvent(false, false), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.GRAY), viewMock);

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages())
      .isEqualTo(Collections.nCopies(3, PlayerColor.GREEN));
    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getMarks().isEmpty()).isTrue();

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY));

    assertThat(playerDashboard.getLoadedGuns()).isEmpty();

    assertThat(playerDashboard.getUnloadedGuns())
      .containsExactly(gunId());

    assertThat(playerDashboard.getAmmos())
      .containsExactlyInAnyOrder(AmmoColor.YELLOW, AmmoColor.RED, AmmoColor.BLUE);
  }
}
