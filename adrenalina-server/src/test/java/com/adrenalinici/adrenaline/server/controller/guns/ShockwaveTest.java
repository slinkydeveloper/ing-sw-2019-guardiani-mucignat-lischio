package com.adrenalinici.adrenaline.server.controller.guns;

import com.adrenalinici.adrenaline.common.model.AmmoColor;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.model.event.ModelEvent;
import com.adrenalinici.adrenaline.common.view.AlternativeGunEffectChosenEvent;
import com.adrenalinici.adrenaline.common.view.PlayerChosenEvent;
import com.adrenalinici.adrenaline.server.controller.DecoratedAlternativeEffectGun;
import com.adrenalinici.adrenaline.server.controller.GunLoader;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.AlternativeEffectGunFlowStateImpl;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.ChooseAlternativeEffectForGunFlowNode;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.ChoosePlayersToHitFlowNode;
import com.adrenalinici.adrenaline.server.flow.FlowNode;
import com.adrenalinici.adrenaline.server.model.PlayerDashboard;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.adrenalinici.adrenaline.server.testutil.MyConditions.isPlayerDashboardUpdateEvent;
import static org.assertj.core.api.Assertions.assertThat;

public class ShockwaveTest extends BaseGunTest {
  @Override
  protected List<FlowNode> nodes() {
    return Arrays.asList(
      new ChooseAlternativeEffectForGunFlowNode(),
      new ChoosePlayersToHitFlowNode()
    );
  }

  @Override
  protected String gunId() {
    return "shockwave";
  }

  @Test
  public void testBaseEffect() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(1, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(1, 1)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(2, 0)).addPlayer(PlayerColor.CYAN);

    PlayerDashboard killerPlayerDashboard = model.getPlayerDashboard(PlayerColor.GREEN);
    killerPlayerDashboard.addGun(gunId());

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.nextPhase(
      viewMock,
      new AlternativeEffectGunFlowStateImpl((DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun(gunId()))
    );
    context.handleEvent(new AlternativeGunEffectChosenEvent(false), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.GRAY), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.YELLOW), viewMock);
    context.handleEvent(new PlayerChosenEvent(null), viewMock);

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages())
      .containsExactly(PlayerColor.GREEN);

    assertThat(model.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .containsExactly(PlayerColor.GREEN);

    assertThat(context.getKilledPlayers().isEmpty()).isTrue();

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY));
    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW));

    assertThat(killerPlayerDashboard.getLoadedGuns())
      .doesNotContain(gunId());

    assertThat(killerPlayerDashboard.getUnloadedGuns())
      .contains(gunId());
  }

  @Test
  public void testTsunamiEffect() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(1, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(1, 1)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(2, 0)).addPlayer(PlayerColor.CYAN);

    PlayerDashboard killerPlayerDashboard = model.getPlayerDashboard(PlayerColor.GREEN);
    killerPlayerDashboard.addGun(gunId());

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.nextPhase(
      viewMock,
      new AlternativeEffectGunFlowStateImpl((DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun(gunId()))
    );
    context.handleEvent(new AlternativeGunEffectChosenEvent(true), viewMock);

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages())
      .containsExactly(PlayerColor.GREEN);

    assertThat(model.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .containsExactly(PlayerColor.GREEN);

    assertThat(model.getPlayerDashboard(PlayerColor.CYAN).getDamages())
      .containsExactly(PlayerColor.GREEN);

    assertThat(context.getKilledPlayers().isEmpty()).isTrue();

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.CYAN));

    assertThat(killerPlayerDashboard.getLoadedGuns())
      .doesNotContain(gunId());

    assertThat(killerPlayerDashboard.getUnloadedGuns())
      .contains(gunId());

    assertThat(killerPlayerDashboard.getAmmos())
      .containsExactlyInAnyOrder(AmmoColor.BLUE, AmmoColor.RED);
  }
}
