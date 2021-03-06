package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.common.model.*;
import com.adrenalinici.adrenaline.common.model.event.ModelEvent;
import com.adrenalinici.adrenaline.common.view.UseTagbackGrenadeEvent;
import com.adrenalinici.adrenaline.server.controller.DecoratedBaseEffectGun;
import com.adrenalinici.adrenaline.server.controller.GunLoader;
import com.adrenalinici.adrenaline.server.controller.nodes.BaseNodeTest;
import com.adrenalinici.adrenaline.server.flow.FlowNode;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static com.adrenalinici.adrenaline.server.testutil.MyConditions.isPlayerDashboardUpdateEvent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class TagbackGrenadeFlowNodeTest extends BaseNodeTest {

  @Override
  public void setUp() {
    super.setUp();
  }

  @Override
  public FlowNode nodeToTest() {
    return new TagbackGrenadeFlowNode();
  }

  @Test
  public void testShowAvailableTagbackGrenades() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    PowerUpCard powerUpCardGray = new PowerUpCard(AmmoColor.BLUE, PowerUpType.TAGBACK_GRENADE);
    PowerUpCard powerUpCardYellow = new PowerUpCard(AmmoColor.RED, PowerUpType.TAGBACK_GRENADE);
    model.getPlayerDashboard(PlayerColor.GRAY).addPowerUpCard(powerUpCardGray);
    model.getPlayerDashboard(PlayerColor.YELLOW).addPowerUpCard(powerUpCardYellow);
    model.getDashboard().getDashboardCell(Position.of(1, 1)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(1, 1)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.YELLOW);

    GunFlowState gunState = new BaseEffectGunFlowStateImpl((DecoratedBaseEffectGun) GunLoader.INSTANCE.getDecoratedGun("machine_gun"));
    gunState.hitPlayer(PlayerColor.GRAY, 1);
    gunState.hitPlayer(PlayerColor.YELLOW, 1);

    context.nextPhase(viewMock, gunState);

    ArgumentCaptor<PlayerColor> playerCaptor = ArgumentCaptor.forClass(PlayerColor.class);
    ArgumentCaptor<List<PowerUpCard>> cardsCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(1)).showAvailableTagbackGrenade(playerCaptor.capture(), cardsCaptor.capture());
    assertThat(playerCaptor.getValue())
      .isEqualTo(PlayerColor.GRAY);
    assertThat(cardsCaptor.getValue())
      .containsExactly(powerUpCardGray);
  }

  @Test
  public void testOnePlayerChooseOnePlayerNoChoose() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    PowerUpCard powerUpCardGray = new PowerUpCard(AmmoColor.BLUE, PowerUpType.TAGBACK_GRENADE);
    PowerUpCard powerUpCardYellow = new PowerUpCard(AmmoColor.RED, PowerUpType.TAGBACK_GRENADE);
    model.getPlayerDashboard(PlayerColor.GRAY).addPowerUpCard(powerUpCardGray);
    model.getPlayerDashboard(PlayerColor.YELLOW).addPowerUpCard(powerUpCardYellow);
    model.getDashboard().getDashboardCell(Position.of(1, 1)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(1, 1)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(1, 1)).addPlayer(PlayerColor.YELLOW);

    GunFlowState gunState = new BaseEffectGunFlowStateImpl((DecoratedBaseEffectGun) GunLoader.INSTANCE.getDecoratedGun("machine_gun"));
    gunState.hitPlayer(PlayerColor.GRAY, 1);
    gunState.hitPlayer(PlayerColor.YELLOW, 1);

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.nextPhase(
      viewMock,
      gunState
    );
    context.handleEvent(new UseTagbackGrenadeEvent(PlayerColor.GRAY, powerUpCardGray), viewMock);
    context.handleEvent(new UseTagbackGrenadeEvent(PlayerColor.YELLOW, null), viewMock);

    assertThat(model.getPlayerDashboard(PlayerColor.GREEN).getMarks())
      .containsExactly(PlayerColor.GRAY);

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GREEN));

    checkEndCalled();

  }

  @Test
  public void calculatePlayersThatCanUseVenomGrenadeTest() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    PowerUpCard powerUpCardGray = new PowerUpCard(AmmoColor.BLUE, PowerUpType.TAGBACK_GRENADE);
    PowerUpCard powerUpCardYellow = new PowerUpCard(AmmoColor.RED, PowerUpType.TAGBACK_GRENADE);
    model.getPlayerDashboard(PlayerColor.GRAY).addPowerUpCard(powerUpCardGray);
    model.getPlayerDashboard(PlayerColor.YELLOW).addPowerUpCard(powerUpCardYellow);
    model.getDashboard().getDashboardCell(Position.of(1, 1)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(1, 1)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(1, 1)).addPlayer(PlayerColor.YELLOW);

    GunFlowState gunState = new BaseEffectGunFlowStateImpl((DecoratedBaseEffectGun) GunLoader.INSTANCE.getDecoratedGun("machine_gun"));
    gunState.hitPlayer(PlayerColor.GRAY, 1);
    gunState.hitPlayer(PlayerColor.YELLOW, 1);

    context.getKilledPlayers().add(PlayerColor.GRAY);

    assertThat(
      ((TagbackGrenadeFlowNode) nodeToTest()).calculatePlayersThatCanUseVenomGrenade(gunState, model, context)
    ).containsOnly(PlayerColor.YELLOW);
  }
}
