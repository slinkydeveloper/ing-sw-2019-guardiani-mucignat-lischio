package com.adrenalinici.adrenaline.controller.nodes;

import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.model.*;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.view.event.GunToReloadChosenEvent;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.adrenalinici.adrenaline.testutil.MyConditions.isPlayerDashboardUpdateEvent;
import static com.adrenalinici.adrenaline.testutil.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ReloadNodeFlowTest extends BaseNodeTest {

  @Override
  public FlowNode nodeToTest() {
    return new ReloadFlowNode();
  }

  @Test
  public void calculateAvailableGunsToReloadTest() {
    Dashboard dashboard = Dashboard.newBuilder().build();
    List<PowerUpCard> powerUpCards = Arrays.asList(new PowerUpCard(AmmoColor.RED, PowerUpType.KINETIC_RAY), new PowerUpCard(AmmoColor.BLUE, PowerUpType.SCOPE));
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.YELLOW, false, powerUpCards);
    BASE_EFFECT_GUNS.forEach(playerDashboard::addUnloadedGun);
    List<PlayerDashboard> playerDashboardList = Arrays.asList(playerDashboard);
    GameModel gameModel = new GameModel(8, dashboard, playerDashboardList);
    ReloadFlowNode node = new ReloadFlowNode();
    assertThat(node.calculateReloadableGuns(gameModel, PlayerColor.YELLOW)).contains(BASE_EFFECT_GUN_SWORD);
    assertThat(node.calculateReloadableGuns(gameModel, PlayerColor.YELLOW)).contains(BASE_EFFECT_GUN_REVOLVER);
    assertThat(node.calculateReloadableGuns(gameModel, PlayerColor.YELLOW)).doesNotContain(BASE_EFFECT_GUN_RIFLE);
  }

  @Test
  public void testReloadOneGun() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    BaseEffectGun gun = new BaseEffectGun(
      "sword",
      AmmoColor.BLUE,
      Arrays.asList(AmmoColor.RED),
      "Sword", "terrible sword",
      null,
      null, Collections.emptyList(),
      null, Collections.emptyList()
    );
    model.getPlayerDashboard(PlayerColor.GREEN).addUnloadedGun(gun);

    orchestrator.startNewFlow(viewMock, context);

    ArgumentCaptor<List<Gun>> reloadableGunsCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(1)).showReloadableGuns(reloadableGunsCaptor.capture());
    assertThat(reloadableGunsCaptor.getValue()).containsOnly(gun);

    orchestrator.handleEvent(new GunToReloadChosenEvent(viewMock, gun));

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);
    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GREEN, model));

    checkEndCalled();
  }

  @Test
  public void testReloadTwoGuns() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    BaseEffectGun gun1 = new BaseEffectGun(
      "sword",
      AmmoColor.BLUE,
      Collections.emptyList(),
      "Sword", "terrible sword",
      null,
      null, Collections.emptyList(),
      null, Collections.emptyList()
    );
    BaseEffectGun gun2 = new BaseEffectGun(
      "knife",
      AmmoColor.BLUE,
      Collections.emptyList(),
      "Knige", "terrible knife",
      null,
      null, Collections.emptyList(),
      null, Collections.emptyList()
    );
    model.getPlayerDashboard(PlayerColor.GREEN).addUnloadedGun(gun1);
    model.getPlayerDashboard(PlayerColor.GREEN).addUnloadedGun(gun2);
    model.getPlayerDashboard(PlayerColor.GREEN).addAmmo(AmmoColor.BLUE);

    orchestrator.startNewFlow(viewMock, context);

    orchestrator.handleEvent(new GunToReloadChosenEvent(viewMock, gun1));
    orchestrator.handleEvent(new GunToReloadChosenEvent(viewMock, gun2));

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);
    assertThat(receivedModelEvents)
      .haveExactly(2, isPlayerDashboardUpdateEvent(PlayerColor.GREEN, model));

    // We check if showReloadableGuns was called two times with right arguments
    ArgumentCaptor<List<Gun>> reloadableGunsCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(2)).showReloadableGuns(reloadableGunsCaptor.capture());
    assertThat(reloadableGunsCaptor.getAllValues().get(0)).containsExactlyInAnyOrder(gun1, gun2);
    assertThat(reloadableGunsCaptor.getAllValues().get(1)).containsExactlyInAnyOrder(gun2);

    checkEndCalled();
  }
}
