package com.adrenalinici.adrenaline.server.controller.guns;

import com.adrenalinici.adrenaline.common.model.AmmoColor;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.model.event.ModelEvent;
import com.adrenalinici.adrenaline.common.view.BaseGunEffectChosenEvent;
import com.adrenalinici.adrenaline.common.view.CellToHitChosenEvent;
import com.adrenalinici.adrenaline.common.view.EnemyMovementChosenEvent;
import com.adrenalinici.adrenaline.common.view.PlayerChosenEvent;
import com.adrenalinici.adrenaline.server.controller.DecoratedBaseEffectGun;
import com.adrenalinici.adrenaline.server.controller.GunLoader;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.*;
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

public class GrenadeLauncherTest extends BaseGunTest {
  @Override
  protected List<FlowNode> nodes() {
    return Arrays.asList(
      new ChooseBaseEffectForGunFlowNode(),
      new ChooseCellToHitFlowNode(),
      new ChoosePlayersToHitFlowNode(),
      new GunChooseEnemyMovementFlowNode(1)
    );
  }

  @Override
  protected String gunId() {
    return "grenade_launcher";
  }

  @Test
  public void testBaseEffect() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(0, 2)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(0, 2)).addPlayer(PlayerColor.CYAN);

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

    context.handleEvent(new EnemyMovementChosenEvent(Position.of(0, 2), PlayerColor.GRAY), viewMock);
    assertThat(model.getPlayerPosition(PlayerColor.GRAY)).isEqualTo(Position.of(0, 2));
    assertThat(receivedModelEvents)
      .haveExactly(1, isDashboardCellUpdatedEvent(0, 1))
      .haveExactly(1, isDashboardCellUpdatedEvent(0, 2));

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages())
      .containsOnly(PlayerColor.GREEN);

    assertThat(context.getKilledPlayers().isEmpty()).isTrue();

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY));

    assertThat(playerDashboard.getLoadedGuns()).isEmpty();

    assertThat(playerDashboard.getUnloadedGuns())
      .containsExactly(gunId());

    assertThat(playerDashboard.getAmmos())
      .containsExactlyInAnyOrder(AmmoColor.YELLOW, AmmoColor.BLUE, AmmoColor.RED);
  }

  @Test
  public void testBothEffectsWithFirstEnemyMovedInTheCellThenChosen() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(0, 2)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(0, 2)).addPlayer(PlayerColor.CYAN);

    PlayerDashboard playerDashboard = model.getPlayerDashboard(PlayerColor.GREEN);
    playerDashboard.addGun(gunId());

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.nextPhase(
      viewMock,
      new BaseEffectGunFlowStateImpl((DecoratedBaseEffectGun) GunLoader.INSTANCE.getDecoratedGun(gunId()))
    );
    context.handleEvent(new BaseGunEffectChosenEvent(true, false), viewMock);

    context.handleEvent(new PlayerChosenEvent(PlayerColor.GRAY), viewMock);

    context.handleEvent(new EnemyMovementChosenEvent(Position.of(0, 2), PlayerColor.GRAY), viewMock);
    assertThat(model.getPlayerPosition(PlayerColor.GRAY)).isEqualTo(Position.of(0, 2));
    assertThat(receivedModelEvents)
      .haveExactly(1, isDashboardCellUpdatedEvent(0, 1))
      .haveExactly(1, isDashboardCellUpdatedEvent(0, 2));

    context.handleEvent(new CellToHitChosenEvent(Position.of(0, 2)), viewMock);

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages())
      .isEqualTo(Collections.nCopies(2, PlayerColor.GREEN));

    assertThat(model.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .containsOnly(PlayerColor.GREEN);

    assertThat(model.getPlayerDashboard(PlayerColor.CYAN).getDamages())
      .containsOnly(PlayerColor.GREEN);

    assertThat(context.getKilledPlayers().isEmpty()).isTrue();

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.CYAN));

    assertThat(playerDashboard.getLoadedGuns()).isEmpty();

    assertThat(playerDashboard.getUnloadedGuns())
      .containsExactly(gunId());

    assertThat(playerDashboard.getAmmos())
      .containsExactlyInAnyOrder(AmmoColor.YELLOW, AmmoColor.BLUE);
  }

  @Test
  public void testBothEffectsWithFirstEnemyNotMoved() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(0, 2)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(0, 2)).addPlayer(PlayerColor.CYAN);

    PlayerDashboard playerDashboard = model.getPlayerDashboard(PlayerColor.GREEN);
    playerDashboard.addGun(gunId());

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.nextPhase(
      viewMock,
      new BaseEffectGunFlowStateImpl((DecoratedBaseEffectGun) GunLoader.INSTANCE.getDecoratedGun(gunId()))
    );
    context.handleEvent(new BaseGunEffectChosenEvent(true, false), viewMock);

    context.handleEvent(new PlayerChosenEvent(PlayerColor.GRAY), viewMock);

    context.handleEvent(new EnemyMovementChosenEvent(model.getPlayerPosition(PlayerColor.GRAY), PlayerColor.GRAY), viewMock);

    context.handleEvent(new CellToHitChosenEvent(Position.of(0, 2)), viewMock);

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages())
      .containsOnly(PlayerColor.GREEN);

    assertThat(model.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .containsOnly(PlayerColor.GREEN);

    assertThat(model.getPlayerDashboard(PlayerColor.CYAN).getDamages())
      .containsOnly(PlayerColor.GREEN);

    assertThat(context.getKilledPlayers().isEmpty()).isTrue();

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.CYAN));

    assertThat(playerDashboard.getLoadedGuns()).isEmpty();

    assertThat(playerDashboard.getUnloadedGuns())
      .containsExactly(gunId());

    assertThat(playerDashboard.getAmmos())
      .containsExactlyInAnyOrder(AmmoColor.YELLOW, AmmoColor.BLUE);
  }
}
