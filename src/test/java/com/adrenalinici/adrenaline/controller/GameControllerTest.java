package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.*;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.testutil.TestUtils;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ActionChosenEvent;
import com.adrenalinici.adrenaline.view.event.GunToPickupChosenEvent;
import com.adrenalinici.adrenaline.view.event.MovementChosenEvent;
import com.adrenalinici.adrenaline.view.event.NewTurnEvent;
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

import static com.adrenalinici.adrenaline.testutil.MyConditions.isDashboardCellUpdatedEvent;
import static com.adrenalinici.adrenaline.testutil.MyConditions.isPlayerDashboardUpdateEvent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class GameControllerTest {

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
  public void testNewTurnFlow() {
    controller.onEvent(new NewTurnEvent(viewMock, PlayerColor.YELLOW));
    assertThat(controller.getTurnOfPlayer()).isEqualTo(PlayerColor.YELLOW);

    ArgumentCaptor<List<Action>> actionsCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(1)).showAvailableActions(actionsCaptor.capture());
    assertThat(actionsCaptor.getValue())
      .containsOnly(Action.MOVE_MOVE_MOVE, Action.MOVE_PICKUP, Action.SHOOT);
  }

  @Test
  public void testMoveMoveMoveFlow() {
    controller.setTurnOfPlayer(PlayerColor.GREEN);
    status.getDashboard().getDashboardCell(Position.of(1, 1)).get().addPlayer(PlayerColor.GREEN);
    controller.setControllerState(WaitingChooseActionState.INSTANCE);
    controller.setRemainingActions(1);

    controller.onEvent(new ActionChosenEvent(viewMock, Action.MOVE_MOVE_MOVE));
    ArgumentCaptor<List<Position>> positionsCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(1)).showAvailableMovements(positionsCaptor.capture());
    assertThat(positionsCaptor.getValue())
      .containsOnly(
        new Position(0, 0),
        new Position(0, 1),
        new Position(0, 2),
        new Position(1, 0),
        new Position(1, 1),
        new Position(1, 2),
        new Position(2, 0),
        new Position(2, 1),
        new Position(2, 2)
      );

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    status.registerObserver(receivedModelEvents::add);

    controller.onEvent(new MovementChosenEvent(viewMock, new Position(2, 0)));

    ArgumentCaptor<PlayerColor> nextPlayerCaptor = ArgumentCaptor.forClass(PlayerColor.class);
    verify(viewMock, times(1)).showNextTurn(nextPlayerCaptor.capture());
    assertThat(nextPlayerCaptor.getValue())
      .isSameAs(PlayerColor.GRAY);

    assertThat(receivedModelEvents)
      .haveExactly(1, isDashboardCellUpdatedEvent(1, 1))
      .haveExactly(1, isDashboardCellUpdatedEvent(2, 0));

    assertThat(status.getPlayerPosition(PlayerColor.GREEN))
      .isEqualTo(Position.of(2, 0));
  }

  @Test
  public void testMovePickupFlow() {
    controller.setTurnOfPlayer(PlayerColor.GREEN);
    status.getDashboard().getDashboardCell(Position.of(2, 1)).get().addPlayer(PlayerColor.GREEN);
    controller.setControllerState(WaitingChooseActionState.INSTANCE);
    controller.setRemainingActions(1);

    BaseEffectGun gun = new BaseEffectGun(AmmoColor.BLUE,
      Arrays.asList(AmmoColor.RED, AmmoColor.BLUE),
      "Sword", "terrible sword", null, null, Collections.emptyList(),
      null, Collections.emptyList());
    status.getDashboard().getDashboardCell(Position.of(2, 0)).get().visit(
      respawnDashboardCell -> respawnDashboardCell.addAvailableGun(gun),
      pickupDashboardCell -> {
      }
    );

    controller.onEvent(new ActionChosenEvent(viewMock, Action.MOVE_PICKUP));
    ArgumentCaptor<List<Position>> positionsCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(1)).showAvailableMovements(positionsCaptor.capture());
    assertThat(positionsCaptor.getValue())
      .containsOnly(
        new Position(2, 0),
        new Position(2, 1),
        new Position(2, 2)
      );

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    status.registerObserver(receivedModelEvents::add);

    controller.onEvent(new MovementChosenEvent(viewMock, new Position(2, 0)));

    ArgumentCaptor<List<Gun>> gunsCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(1)).showAvailableGunsToPickup(gunsCaptor.capture());
    assertThat(gunsCaptor.getValue())
      .containsOnly(gun);

    assertThat(receivedModelEvents)
      .haveExactly(1, isDashboardCellUpdatedEvent(2, 1));
    assertThat(receivedModelEvents)
      .haveExactly(1, isDashboardCellUpdatedEvent(2, 0));
    assertThat(status.getPlayerPosition(PlayerColor.GREEN))
      .isEqualTo(Position.of(2, 0));

    receivedModelEvents = new ArrayList<>();
    status.registerObserver(receivedModelEvents::add);
    status
      .getDashboard()
      .getDashboardCell(Position.of(2, 0)).get()
      .visit(
        respawnDashboardCell -> controller.onEvent(new GunToPickupChosenEvent(viewMock, gun, respawnDashboardCell)),
        pickupDashboardCell -> {
        }
      );

    ArgumentCaptor<PlayerColor> nextPlayerCaptor = ArgumentCaptor.forClass(PlayerColor.class);
    verify(viewMock, times(1)).showNextTurn(nextPlayerCaptor.capture());
    assertThat(nextPlayerCaptor.getValue())
      .isSameAs(PlayerColor.GRAY);

    assertThat(receivedModelEvents)
      .haveExactly(1, isDashboardCellUpdatedEvent(2, 0));
    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GREEN, status));


  }
}
