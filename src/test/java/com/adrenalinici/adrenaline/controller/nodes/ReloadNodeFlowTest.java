package com.adrenalinici.adrenaline.controller.nodes;

import com.adrenalinici.adrenaline.controller.GunLoader;
import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.model.*;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.view.event.GunChosenEvent;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.adrenalinici.adrenaline.testutil.MyConditions.isPlayerDashboardUpdateEvent;
import static com.adrenalinici.adrenaline.testutil.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
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
    Stream
      .of("test_revolver", "test_rifle", "test_sword")
      .map(s -> GunLoader.INSTANCE.getModelGun(s))
      .forEach(playerDashboard::addUnloadedGun);
    List<PlayerDashboard> playerDashboardList = Collections.singletonList(playerDashboard);
    GameModel gameModel = new GameModel(8, dashboard, playerDashboardList);
    ReloadFlowNode node = new ReloadFlowNode();
    assertThat(node.calculateReloadableGuns(gameModel, PlayerColor.YELLOW))
      .extracting(Gun::getId)
      .contains("test_sword", "test_revolver")
      .doesNotContain("test_rifle");
  }

  @Test
  public void testReloadOneGun() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);
    PlayerDashboard d = model.getPlayerDashboard(PlayerColor.GREEN);
    d.addAmmo(AmmoColor.RED);
    d.addAmmo(AmmoColor.BLUE);

    d.addUnloadedGun(GunLoader.INSTANCE.getModelGun("test_sword"));

    orchestrator.startNewFlow(viewMock, context);

    ArgumentCaptor<List<Gun>> reloadableGunsCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(1)).showReloadableGuns(reloadableGunsCaptor.capture());
    assertThat(reloadableGunsCaptor.getValue())
      .extracting(Gun::getId)
      .containsOnly("test_sword");

    orchestrator.handleEvent(new GunChosenEvent("test_sword"), viewMock);

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GREEN, model));

    assertThat(d.getLoadedGuns())
      .extracting(Gun::getId)
      .containsOnly("test_sword");
    assertThat(d.getUnloadedGuns()).isEmpty();

    checkEndCalled();
  }

  @Test
  public void testReloadTwoGuns() {
    context.setTurnOfPlayer(PlayerColor.GREEN);
    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    PlayerDashboard d = model.getPlayerDashboard(PlayerColor.GREEN);
    d.addAmmo(AmmoColor.RED);
    d.addAmmo(AmmoColor.YELLOW);
    d.addAmmo(AmmoColor.YELLOW);
    d.addAmmo(AmmoColor.BLUE);
    d.addAmmo(AmmoColor.BLUE);

    d.addUnloadedGun(GunLoader.INSTANCE.getModelGun("test_revolver"));
    d.addUnloadedGun(GunLoader.INSTANCE.getModelGun("test_rifle"));

    orchestrator.startNewFlow(viewMock, context);

    orchestrator.handleEvent(new GunChosenEvent("test_revolver"), viewMock);
    orchestrator.handleEvent(new GunChosenEvent("test_rifle"), viewMock);

    assertThat(receivedModelEvents)
      .haveExactly(2, isPlayerDashboardUpdateEvent(PlayerColor.GREEN, model));

    // We check if showReloadableGuns was called two times with right arguments
    ArgumentCaptor<List<Gun>> reloadableGunsCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(2)).showReloadableGuns(reloadableGunsCaptor.capture());
    assertThat(reloadableGunsCaptor.getAllValues().get(0))
      .extracting(Gun::getId)
      .containsExactlyInAnyOrder("test_rifle", "test_revolver");
    assertThat(reloadableGunsCaptor.getAllValues().get(1))
      .extracting(Gun::getId)
      .containsExactlyInAnyOrder("test_rifle");

    assertThat(d.getLoadedGuns())
      .extracting(Gun::getId)
      .containsExactlyInAnyOrder("test_rifle", "test_revolver");
    assertThat(d.getUnloadedGuns()).isEmpty();

    checkEndCalled();
  }
}
