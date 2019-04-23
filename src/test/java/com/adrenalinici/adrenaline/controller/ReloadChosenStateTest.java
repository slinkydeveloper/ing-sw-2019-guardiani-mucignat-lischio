package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.*;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.testutil.TestUtils;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.GunToReloadChosenEvent;
import com.adrenalinici.adrenaline.view.event.ViewEvent;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.adrenalinici.adrenaline.testutil.MyConditions.isPlayerDashboardUpdateEvent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class ReloadChosenStateTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  GameStatus status;
  @Mock
  GameView viewMock;
  GameController controller;

  @Before
  public void setUp() {
    status = TestUtils.generatePlayerStatus();
    controller = new GameController(status);
  }

  @Test
  public void reloadChosenStateTest() {
    controller.setTurnOfPlayer(PlayerColor.GREEN);
    BaseEffectGun gun = new BaseEffectGun(AmmoColor.BLUE,
      Arrays.asList(AmmoColor.RED),
      "Sword", "terrible sword", null, null, Collections.emptyList(),
      null, Collections.emptyList());
    status.getPlayerDashboard(PlayerColor.GREEN).addUnloadedGun(gun);
    ViewEvent event = new GunToReloadChosenEvent(viewMock, gun);
    ControllerState state = ReloadChosenState.INSTANCE;
    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    status.registerObserver(receivedModelEvents::add);
    state.acceptEvent(event, controller);
    ArgumentCaptor<List<Gun>> gunsCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(1)).showReloadableGuns(gunsCaptor.capture());
    assertThat(gunsCaptor.getValue()).containsOnly(gun);
    assertThat(receivedModelEvents).haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GREEN, status));
  }
}
