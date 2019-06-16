package com.adrenalinici.adrenaline.server.controller.guns;

import com.adrenalinici.adrenaline.common.model.AmmoColor;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.model.event.ModelEvent;
import com.adrenalinici.adrenaline.common.view.BaseGunEffectChosenEvent;
import com.adrenalinici.adrenaline.common.view.MovementChosenEvent;
import com.adrenalinici.adrenaline.common.view.PlayerChosenEvent;
import com.adrenalinici.adrenaline.server.controller.DecoratedBaseEffectGun;
import com.adrenalinici.adrenaline.server.controller.GunLoader;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.BaseEffectGunFlowStateImpl;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.ChooseBaseEffectForGunFlowNode;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.ChoosePlayersToHitFlowNode;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.GunChooseMovementFlowNode;
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

public class CyberbladeTest extends BaseGunTest {
  @Override
  protected List<FlowNode> nodes() {
    return Arrays.asList(
      new ChooseBaseEffectForGunFlowNode(),
      new ChoosePlayersToHitFlowNode(),
      new GunChooseMovementFlowNode(1)
    );
  }

  @Override
  protected String gunId() {
    return "cyberblade";
  }

  @Test
  public void testOnlyBaseEffect() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(1, 0)).addPlayer(PlayerColor.YELLOW);
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
      .isEqualTo(Collections.nCopies(2, PlayerColor.GREEN));

    assertThat(context.getKilledPlayers().isEmpty()).isTrue();

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY));

    assertThat(playerDashboard.getLoadedGuns()).isEmpty();

    assertThat(playerDashboard.getUnloadedGuns())
      .containsExactly(gunId());

    assertThat(playerDashboard.getAmmos())
      .containsExactlyInAnyOrder(AmmoColor.YELLOW, AmmoColor.RED, AmmoColor.BLUE);
  }

  @Test
  public void testWithOnlyBaseEffectAndMoveAppliedBefore() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(1, 0)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(2, 0)).addPlayer(PlayerColor.CYAN);

    PlayerDashboard playerDashboard = model.getPlayerDashboard(PlayerColor.GREEN);
    playerDashboard.addGun(gunId());

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.nextPhase(
      viewMock,
      new BaseEffectGunFlowStateImpl((DecoratedBaseEffectGun) GunLoader.INSTANCE.getDecoratedGun(gunId()))
    );
    context.handleEvent(new BaseGunEffectChosenEvent(true, false), viewMock);

    context.handleEvent(new MovementChosenEvent(Position.of(0, 1)), viewMock);
    assertThat(model.getPlayerPosition(PlayerColor.GREEN)).isEqualTo(Position.of(0, 1));
    assertThat(receivedModelEvents)
      .haveExactly(1, isDashboardCellUpdatedEvent(0, 0))
      .haveExactly(1, isDashboardCellUpdatedEvent(0, 1));

    context.handleEvent(new PlayerChosenEvent(PlayerColor.GRAY), viewMock);

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages())
      .isEqualTo(Collections.nCopies(2, PlayerColor.GREEN));

    assertThat(context.getKilledPlayers().isEmpty()).isTrue();

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY));

    assertThat(playerDashboard.getLoadedGuns()).isEmpty();

    assertThat(playerDashboard.getUnloadedGuns())
      .containsExactly(gunId());

    assertThat(playerDashboard.getAmmos())
      .containsExactlyInAnyOrder(AmmoColor.YELLOW, AmmoColor.RED, AmmoColor.BLUE);
  }

  @Test
  public void testWithAllEffectsAndMoveAppliedInTheMiddle() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(2, 0)).addPlayer(PlayerColor.CYAN);

    PlayerDashboard playerDashboard = model.getPlayerDashboard(PlayerColor.GREEN);
    playerDashboard.addGun(gunId());

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.nextPhase(
      viewMock,
      new BaseEffectGunFlowStateImpl((DecoratedBaseEffectGun) GunLoader.INSTANCE.getDecoratedGun(gunId()))
    );
    context.handleEvent(new BaseGunEffectChosenEvent(true, true), viewMock);
    context.handleEvent(new MovementChosenEvent(model.getPlayerPosition(PlayerColor.GREEN)), viewMock);
    assertThat(receivedModelEvents)
      .haveExactly(0, isDashboardCellUpdatedEvent(0, 0));

    //choose first victim
    context.handleEvent(new PlayerChosenEvent(PlayerColor.GRAY), viewMock);

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages().isEmpty()).isTrue();

    //move
    context.handleEvent(new MovementChosenEvent(Position.of(0, 1)), viewMock);
    assertThat(model.getPlayerPosition(PlayerColor.GREEN)).isEqualTo(Position.of(0, 1));
    assertThat(receivedModelEvents)
      .haveExactly(1, isDashboardCellUpdatedEvent(0, 0))
      .haveExactly(1, isDashboardCellUpdatedEvent(0, 1));

    //choose second victim
    context.handleEvent(new PlayerChosenEvent(PlayerColor.YELLOW), viewMock);

    assertThat(model.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .isEqualTo(Collections.nCopies(2, PlayerColor.GREEN));
    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages())
      .isEqualTo(Collections.nCopies(2, PlayerColor.GREEN));

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY));

    assertThat(context.getKilledPlayers().isEmpty()).isTrue();

    assertThat(playerDashboard.getLoadedGuns()).isEmpty();

    assertThat(playerDashboard.getUnloadedGuns())
      .containsExactly(gunId());

    assertThat(playerDashboard.getAmmos())
      .containsExactlyInAnyOrder(AmmoColor.RED, AmmoColor.BLUE);
  }

  @Test
  public void testWithAllEffectsAndMoveAppliedOnEnd() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(2, 0)).addPlayer(PlayerColor.CYAN);

    PlayerDashboard playerDashboard = model.getPlayerDashboard(PlayerColor.GREEN);
    playerDashboard.addGun(gunId());

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.nextPhase(
      viewMock,
      new BaseEffectGunFlowStateImpl((DecoratedBaseEffectGun) GunLoader.INSTANCE.getDecoratedGun(gunId()))
    );
    context.handleEvent(new BaseGunEffectChosenEvent(true, true), viewMock);

    context.handleEvent(new MovementChosenEvent(Position.of(0, 1)), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.GRAY), viewMock);

    context.handleEvent(new PlayerChosenEvent(PlayerColor.YELLOW), viewMock);

    context.handleEvent(new MovementChosenEvent(Position.of(0, 1)), viewMock);

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages())
      .isEqualTo(Collections.nCopies(2, PlayerColor.GREEN));

    assertThat(model.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .isEqualTo(Collections.nCopies(2, PlayerColor.GREEN));

    context.handleEvent(new MovementChosenEvent(Position.of(1, 1)), viewMock);
    assertThat(model.getPlayerPosition(PlayerColor.GREEN)).isEqualTo(Position.of(1, 1));
    assertThat(receivedModelEvents)
      .haveExactly(1, isDashboardCellUpdatedEvent(0, 1))
      .haveExactly(1, isDashboardCellUpdatedEvent(1, 1));

    assertThat(context.getKilledPlayers().isEmpty()).isTrue();

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW));

    assertThat(playerDashboard.getLoadedGuns()).isEmpty();

    assertThat(playerDashboard.getUnloadedGuns())
      .containsExactly(gunId());

    assertThat(playerDashboard.getAmmos())
      .containsExactlyInAnyOrder(AmmoColor.RED, AmmoColor.BLUE);
  }

}
