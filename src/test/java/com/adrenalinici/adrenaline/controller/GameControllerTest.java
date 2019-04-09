package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.*;
import com.adrenalinici.adrenaline.model.event.DashboardCellUpdatedEvent;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.testutil.TestUtils;
import com.adrenalinici.adrenaline.view.event.ActionChosenEvent;
import com.adrenalinici.adrenaline.view.event.MovementChosenEvent;
import com.adrenalinici.adrenaline.view.event.NewTurnEvent;
import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

public class GameControllerTest {

  GameStatus status;
  GameViewMock gameViewMock;
  GameController controller;

  @Before
  public void setUp() {
    status = new GameStatus(8, TestUtils.build3x3Dashboard(), TestUtils.generate3PlayerDashboards());
    gameViewMock = new GameViewMock();
    controller = new GameController(status);
    gameViewMock.registerObserver(controller);
  }

  @Test
  public void testNewTurn() {
    AtomicBoolean called = new AtomicBoolean(false);

    gameViewMock.setAvailableActionsListener(actions ->
      assertThat(actions)
        .containsOnly(Action.MOVE_MOVE_MOVE, Action.MOVE_PICKUP, Action.SHOOT),
      called
    );
    gameViewMock.notifyEvent(new NewTurnEvent(gameViewMock, PlayerColor.YELLOW));
    assertThat(controller.getTurnOfPlayer()).isEqualTo(PlayerColor.YELLOW);
    assertThat(called).isTrue();
  }

  @Test
  public void testNewMovement() {
    controller.setTurnOfPlayer(PlayerColor.YELLOW);
    status.getDashboard().getDashboardCell(Position.of(1, 1)).get().addPlayer(PlayerColor.YELLOW);
    AtomicBoolean calledAvailableMovements = new AtomicBoolean(false);

    gameViewMock.setAvailableMovementsListener(movements ->
      assertThat(movements).containsOnly(
        new Position(0, 0),
        new Position(0, 1),
        new Position(0, 2),
        new Position(1, 0),
        new Position(1, 1),
        new Position(1, 2),
        new Position(2, 0),
        new Position(2, 1),
        new Position(2, 2)
      ), calledAvailableMovements
    );
    gameViewMock.notifyEvent(new ActionChosenEvent(gameViewMock, Action.MOVE_MOVE_MOVE));
    assertThat(calledAvailableMovements).isTrue();

    AtomicBoolean calledEndTurn = new AtomicBoolean(false);
    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    gameViewMock.setEndTurnListener(calledEndTurn);
    status.registerObserver(receivedModelEvents::add);
    gameViewMock.notifyEvent(new MovementChosenEvent(gameViewMock, new Position(2, 0)));
    assertThat(calledEndTurn).isTrue();
    assertThat(receivedModelEvents)
      .haveExactly(1, new Condition<>(e ->
        e instanceof DashboardCellUpdatedEvent && ((DashboardCellUpdatedEvent) e).getCell().getLine() == 1 && ((DashboardCellUpdatedEvent) e).getCell().getCell() == 1,
        "Event must be a DashboardCellUpdatedEvent with cell line 1 and row 1"
        )
      );
    assertThat(receivedModelEvents)
      .haveExactly(1, new Condition<>(e ->
          e instanceof DashboardCellUpdatedEvent && ((DashboardCellUpdatedEvent) e).getCell().getLine() == 2 && ((DashboardCellUpdatedEvent) e).getCell().getCell() == 0,
          "Event must be a DashboardCellUpdatedEvent with cell line 1 and row 1"
        )
      );
    assertThat(status.getPlayerPosition(PlayerColor.YELLOW)).isEqualTo(Position.of(2,  0));
  }

}
