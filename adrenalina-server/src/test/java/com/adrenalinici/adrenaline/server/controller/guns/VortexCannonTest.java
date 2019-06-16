package com.adrenalinici.adrenaline.server.controller.guns;

import com.adrenalinici.adrenaline.common.model.AmmoColor;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.model.event.ModelEvent;
import com.adrenalinici.adrenaline.common.view.BaseGunEffectChosenEvent;
import com.adrenalinici.adrenaline.common.view.CellToHitChosenEvent;
import com.adrenalinici.adrenaline.common.view.PlayerChosenEvent;
import com.adrenalinici.adrenaline.server.controller.DecoratedBaseEffectGun;
import com.adrenalinici.adrenaline.server.controller.GunLoader;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.BaseEffectGunFlowStateImpl;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.ChooseBaseEffectForGunFlowNode;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.ChooseCellToHitFlowNode;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.ChoosePlayersToHitFlowNode;
import com.adrenalinici.adrenaline.server.flow.FlowNode;
import com.adrenalinici.adrenaline.server.model.PlayerDashboard;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.adrenalinici.adrenaline.server.testutil.MyConditions.isDashboardCellUpdatedEvent;
import static com.adrenalinici.adrenaline.server.testutil.MyConditions.isPlayerDashboardUpdateEvent;
import static org.assertj.core.api.Assertions.assertThat;

public class VortexCannonTest extends BaseGunTest {
  @Override
  protected List<FlowNode> nodes() {
    return Arrays.asList(
      new ChooseBaseEffectForGunFlowNode(),
      new ChoosePlayersToHitFlowNode(),
      new ChooseCellToHitFlowNode()
    );
  }

  @Override
  protected String gunId() {
    return "vortex_cannon";
  }

  @Test
  public void testBaseEffect() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 2)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(1, 1)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(2, 2)).addPlayer(PlayerColor.CYAN);

    PlayerDashboard killerPlayerDashboard = model.getPlayerDashboard(PlayerColor.GREEN);
    killerPlayerDashboard.addGun(gunId());

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.nextPhase(
      viewMock,
      new BaseEffectGunFlowStateImpl((DecoratedBaseEffectGun) GunLoader.INSTANCE.getDecoratedGun(gunId()))
    );
    context.handleEvent(new BaseGunEffectChosenEvent(false, false), viewMock);
    context.handleEvent(new CellToHitChosenEvent(Position.of(0, 1)), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.GRAY), viewMock);

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages())
      .isEqualTo(Collections.nCopies(2, PlayerColor.GREEN));

    assertThat(model.getPlayerPosition(PlayerColor.GRAY))
      .isEqualTo(Position.of(0, 1));

    assertThat(context.getKilledPlayers().isEmpty()).isTrue();

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY))
      .haveExactly(1, isDashboardCellUpdatedEvent(0, 1))
      .haveExactly(1, isDashboardCellUpdatedEvent(0, 2));

    assertThat(killerPlayerDashboard.getLoadedGuns())
      .doesNotContain(gunId());

    assertThat(killerPlayerDashboard.getUnloadedGuns())
      .contains(gunId());

    assertThat(killerPlayerDashboard.getAmmos())
      .containsExactlyInAnyOrder(AmmoColor.YELLOW, AmmoColor.BLUE, AmmoColor.RED);
  }

  @Test
  public void testWithBlackHole() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 2)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.CYAN);

    PlayerDashboard killerPlayerDashboard = model.getPlayerDashboard(PlayerColor.GREEN);
    killerPlayerDashboard.addGun(gunId());

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.nextPhase(
      viewMock,
      new BaseEffectGunFlowStateImpl((DecoratedBaseEffectGun) GunLoader.INSTANCE.getDecoratedGun(gunId()))
    );
    context.handleEvent(new BaseGunEffectChosenEvent(true, false), viewMock);
    context.handleEvent(new CellToHitChosenEvent(Position.of(0, 1)), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.GRAY), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.YELLOW), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.CYAN), viewMock);

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages())
      .isEqualTo(Collections.nCopies(2, PlayerColor.GREEN));
    assertThat(model.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .containsOnly(PlayerColor.GREEN);
    assertThat(model.getPlayerDashboard(PlayerColor.CYAN).getDamages())
      .containsOnly(PlayerColor.GREEN);

    assertThat(model.getPlayerPosition(PlayerColor.GRAY))
      .isEqualTo(Position.of(0, 1));
    assertThat(model.getPlayerPosition(PlayerColor.YELLOW))
      .isEqualTo(Position.of(0, 1));
    assertThat(model.getPlayerPosition(PlayerColor.CYAN))
      .isEqualTo(Position.of(0, 1));

    assertThat(context.getKilledPlayers().isEmpty()).isTrue();

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.CYAN))
      .haveExactly(1, isDashboardCellUpdatedEvent(0, 0))
      .haveExactly(2, isDashboardCellUpdatedEvent(0, 1))
      .haveExactly(1, isDashboardCellUpdatedEvent(0, 2));

    assertThat(killerPlayerDashboard.getLoadedGuns())
      .doesNotContain(gunId());

    assertThat(killerPlayerDashboard.getUnloadedGuns())
      .contains(gunId());

    assertThat(killerPlayerDashboard.getAmmos())
      .containsExactlyInAnyOrder(AmmoColor.YELLOW, AmmoColor.BLUE);
  }
}
