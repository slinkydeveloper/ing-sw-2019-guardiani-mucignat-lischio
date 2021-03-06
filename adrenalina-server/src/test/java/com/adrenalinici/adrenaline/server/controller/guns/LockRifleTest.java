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

public class LockRifleTest extends BaseGunTest {
  @Override
  protected List<FlowNode> nodes() {
    return Arrays.asList(
      new ChooseBaseEffectForGunFlowNode(),
      new ChoosePlayersToHitFlowNode()
    );
  }

  @Override
  protected String gunId() {
    return "lock_rifle";
  }

  @Test
  public void testOnlyBaseEffect() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(1, 1)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(0, 2)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(0, 2)).addPlayer(PlayerColor.CYAN);


    PlayerDashboard killerPlayerDashboard = model.getPlayerDashboard(PlayerColor.GREEN);
    killerPlayerDashboard.addGun(gunId());

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.nextPhase(
      viewMock,
      new BaseEffectGunFlowStateImpl((DecoratedBaseEffectGun) GunLoader.INSTANCE.getDecoratedGun(gunId()))
    );
    context.handleEvent(new BaseGunEffectChosenEvent(false, false), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.GRAY), viewMock);

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages())
      .isEqualTo(Collections.nCopies(2, PlayerColor.GREEN));

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getMarks())
      .containsExactly(PlayerColor.GREEN);

    assertThat(context.getKilledPlayers()).isEmpty();

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY));

    assertThat(killerPlayerDashboard.getLoadedGuns()).isEmpty();

    assertThat(killerPlayerDashboard.getUnloadedGuns())
      .containsExactly(gunId());

    assertThat(killerPlayerDashboard.getAmmos())
      .containsExactlyInAnyOrder(AmmoColor.YELLOW, AmmoColor.RED, AmmoColor.BLUE);
  }

  @Test
  public void testWithSecondLockEffect() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(1, 0)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(1, 1)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(0, 2)).addPlayer(PlayerColor.CYAN);


    PlayerDashboard killerPlayerDashboard = model.getPlayerDashboard(PlayerColor.GREEN);
    killerPlayerDashboard.addGun(gunId());

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.nextPhase(
      viewMock,
      new BaseEffectGunFlowStateImpl((DecoratedBaseEffectGun) GunLoader.INSTANCE.getDecoratedGun(gunId()))
    );
    context.handleEvent(new BaseGunEffectChosenEvent(true, false), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.GRAY), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.YELLOW), viewMock);

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages())
      .isEqualTo(Collections.nCopies(2, PlayerColor.GREEN));

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getMarks())
      .containsExactly(PlayerColor.GREEN);

    assertThat(context.getKilledPlayers()).isEmpty();

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY));

    assertThat(model.getPlayerDashboard(PlayerColor.YELLOW).getDamages().isEmpty()).isTrue();
    assertThat(model.getPlayerDashboard(PlayerColor.YELLOW).getMarks())
      .containsExactly(PlayerColor.GREEN);

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW));

    assertThat(killerPlayerDashboard.getLoadedGuns()).isEmpty();

    assertThat(killerPlayerDashboard.getUnloadedGuns())
      .containsExactly(gunId());

    assertThat(killerPlayerDashboard.getAmmos())
      .containsExactlyInAnyOrder(AmmoColor.YELLOW, AmmoColor.BLUE);
  }
}
