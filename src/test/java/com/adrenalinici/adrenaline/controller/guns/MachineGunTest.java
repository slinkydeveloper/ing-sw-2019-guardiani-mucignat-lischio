package com.adrenalinici.adrenaline.controller.guns;

import com.adrenalinici.adrenaline.controller.DecoratedBaseEffectGun;
import com.adrenalinici.adrenaline.controller.GunFactory;
import com.adrenalinici.adrenaline.controller.nodes.guns.BaseEffectGunFlowState;
import com.adrenalinici.adrenaline.controller.nodes.guns.ChooseBaseEffectForGunFlowNode;
import com.adrenalinici.adrenaline.controller.nodes.guns.ChoosePlayersToHitFlowNode;
import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.model.AmmoColor;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.model.PlayerDashboard;
import com.adrenalinici.adrenaline.model.Position;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.view.event.BaseGunEffectChosenEvent;
import com.adrenalinici.adrenaline.view.event.PlayerChosenEvent;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.adrenalinici.adrenaline.testutil.MyConditions.gunWithId;
import static com.adrenalinici.adrenaline.testutil.MyConditions.isPlayerDashboardUpdateEvent;
import static org.assertj.core.api.Assertions.assertThat;

public class MachineGunTest extends BaseGunTest {
  @Override
  protected GunFactory gunFactory() {
    return new MachineGunGunFactory();
  }

  @Override
  protected List<FlowNode> nodes() {
    return Arrays.asList(
      new ChooseBaseEffectForGunFlowNode(),
      new ChoosePlayersToHitFlowNode()
    );
  }

  @Override
  protected String gunId() {
    return "machine_gun";
  }

  @Test
  public void testOnlyBaseEffect() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 2)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(2, 0)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(2, 0)).addPlayer(PlayerColor.CYAN);

    PlayerDashboard playerDashboard = model.getPlayerDashboard(PlayerColor.GREEN);
    playerDashboard.addLoadedGun(gunLoader.getModelGun(gunId()));

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.nextPhase(viewMock, new BaseEffectGunFlowState((DecoratedBaseEffectGun) gunLoader.getDecoratedGun(gunId())));
    context.handleEvent(new BaseGunEffectChosenEvent(false, false), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.GRAY), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.YELLOW), viewMock);

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages())
      .containsExactly(PlayerColor.GREEN);

    assertThat(model.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .containsExactly(PlayerColor.GREEN);

    assertThat(context.getKilledPlayers()).isEmpty();

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY, model))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW, model));

    assertThat(playerDashboard.getLoadedGuns())
      .doNotHave(gunWithId(gunId()));

    assertThat(playerDashboard.getUnloadedGuns())
      .haveExactly(1, gunWithId(gunId()));

    assertThat(playerDashboard.getAmmos())
      .containsExactlyInAnyOrder(AmmoColor.YELLOW, AmmoColor.RED, AmmoColor.BLUE);
  }

  @Test
  public void testBaseEffectAndFocus() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 2)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(2, 0)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(2, 0)).addPlayer(PlayerColor.CYAN);

    PlayerDashboard playerDashboard = model.getPlayerDashboard(PlayerColor.GREEN);
    playerDashboard.addLoadedGun(gunLoader.getModelGun(gunId()));

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.nextPhase(viewMock, new BaseEffectGunFlowState((DecoratedBaseEffectGun) gunLoader.getDecoratedGun(gunId())));
    context.handleEvent(new BaseGunEffectChosenEvent(true, false), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.GRAY), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.YELLOW), viewMock);

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages())
      .containsExactly(PlayerColor.GREEN, PlayerColor.GREEN);

    assertThat(model.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .containsExactly(PlayerColor.GREEN);

    assertThat(context.getKilledPlayers()).isEmpty();

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY, model))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW, model));

    assertThat(playerDashboard.getLoadedGuns())
      .doNotHave(gunWithId(gunId()));

    assertThat(playerDashboard.getUnloadedGuns())
      .haveExactly(1, gunWithId(gunId()));

    assertThat(playerDashboard.getAmmos())
      .containsExactlyInAnyOrder(AmmoColor.RED, AmmoColor.BLUE);
  }

  @Test
  public void testBaseEffectAndTripod() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 2)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(2, 0)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(2, 0)).addPlayer(PlayerColor.CYAN);

    PlayerDashboard playerDashboard = model.getPlayerDashboard(PlayerColor.GREEN);
    playerDashboard.addLoadedGun(gunLoader.getModelGun(gunId()));

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.nextPhase(viewMock, new BaseEffectGunFlowState((DecoratedBaseEffectGun) gunLoader.getDecoratedGun(gunId())));
    context.handleEvent(new BaseGunEffectChosenEvent(false, true), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.GRAY), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.YELLOW), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.CYAN), viewMock);

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages())
      .containsExactly(PlayerColor.GREEN);

    assertThat(model.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .containsExactly(PlayerColor.GREEN, PlayerColor.GREEN);

    assertThat(model.getPlayerDashboard(PlayerColor.CYAN).getDamages())
      .containsExactly(PlayerColor.GREEN);

    assertThat(context.getKilledPlayers()).isEmpty();

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY, model))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW, model))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.CYAN, model));

    assertThat(playerDashboard.getLoadedGuns())
      .doNotHave(gunWithId(gunId()));

    assertThat(playerDashboard.getUnloadedGuns())
      .haveExactly(1, gunWithId(gunId()));

    assertThat(playerDashboard.getAmmos())
      .containsExactlyInAnyOrder(AmmoColor.RED, AmmoColor.YELLOW);
  }

  @Test
  public void testBaseEffectAndTripodWithoutThirdPlayerChosen() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 2)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(2, 0)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(2, 0)).addPlayer(PlayerColor.CYAN);

    PlayerDashboard playerDashboard = model.getPlayerDashboard(PlayerColor.GREEN);
    playerDashboard.addLoadedGun(gunLoader.getModelGun(gunId()));

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.nextPhase(viewMock, new BaseEffectGunFlowState((DecoratedBaseEffectGun) gunLoader.getDecoratedGun(gunId())));
    context.handleEvent(new BaseGunEffectChosenEvent(false, true), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.GRAY), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.YELLOW), viewMock);
    context.handleEvent(new PlayerChosenEvent(null), viewMock);

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages())
      .containsExactly(PlayerColor.GREEN);

    assertThat(model.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .containsExactly(PlayerColor.GREEN, PlayerColor.GREEN);

    assertThat(context.getKilledPlayers()).isEmpty();

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY, model))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW, model));

    assertThat(playerDashboard.getLoadedGuns())
      .doNotHave(gunWithId(gunId()));

    assertThat(playerDashboard.getUnloadedGuns())
      .haveExactly(1, gunWithId(gunId()));

    assertThat(playerDashboard.getAmmos())
      .containsExactlyInAnyOrder(AmmoColor.RED, AmmoColor.YELLOW);
  }

  @Test
  public void testBaseEffectAndFocusAndTripod() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 2)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(2, 0)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(2, 0)).addPlayer(PlayerColor.CYAN);

    PlayerDashboard playerDashboard = model.getPlayerDashboard(PlayerColor.GREEN);
    playerDashboard.addLoadedGun(gunLoader.getModelGun(gunId()));

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.nextPhase(viewMock, new BaseEffectGunFlowState((DecoratedBaseEffectGun) gunLoader.getDecoratedGun(gunId())));
    context.handleEvent(new BaseGunEffectChosenEvent(true, true), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.GRAY), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.YELLOW), viewMock);
    context.handleEvent(new PlayerChosenEvent(PlayerColor.CYAN), viewMock);

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages())
      .containsExactly(PlayerColor.GREEN, PlayerColor.GREEN);

    assertThat(model.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .containsExactly(PlayerColor.GREEN, PlayerColor.GREEN);

    assertThat(model.getPlayerDashboard(PlayerColor.CYAN).getDamages())
      .containsExactly(PlayerColor.GREEN);

    assertThat(context.getKilledPlayers()).isEmpty();

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY, model))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW, model))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.CYAN, model));

    assertThat(playerDashboard.getLoadedGuns())
      .doNotHave(gunWithId(gunId()));

    assertThat(playerDashboard.getUnloadedGuns())
      .haveExactly(1, gunWithId(gunId()));

    assertThat(playerDashboard.getAmmos())
      .containsExactlyInAnyOrder(AmmoColor.RED);
  }

}
