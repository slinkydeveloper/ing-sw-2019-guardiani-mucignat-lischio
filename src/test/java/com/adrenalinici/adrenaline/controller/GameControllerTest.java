package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.*;
import com.adrenalinici.adrenaline.model.event.DashboardCellUpdatedEvent;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.testutil.TestUtils;
import com.adrenalinici.adrenaline.view.event.ActionChosenEvent;
import com.adrenalinici.adrenaline.view.event.MovementChosenEvent;
import com.adrenalinici.adrenaline.view.event.NewTurnEvent;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.adrenalinici.adrenaline.testutil.MyAssertions.assertContainsOneSatisfying;
import static com.adrenalinici.adrenaline.testutil.MyAssertions.assertListEqualsWithoutOrdering;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
      assertListEqualsWithoutOrdering(Arrays.asList(
        Action.MOVE_MOVE_MOVE, Action.MOVE_PICKUP, Action.SHOOT
      ), actions), called
    );
    gameViewMock.notifyEvent(new NewTurnEvent(gameViewMock, PlayerColor.YELLOW));
    assertEquals(PlayerColor.YELLOW, controller.getTurnOfPlayer());
    assertTrue(called.get());
  }

  @Test
  public void testNewMovement() {
    controller.setTurnOfPlayer(PlayerColor.YELLOW);
    status.getDashboard().getDashboardCell(Position.of(1, 1)).get().addPlayer(PlayerColor.YELLOW);
    AtomicBoolean calledAvailableMovements = new AtomicBoolean(false);

    gameViewMock.setAvailableMovementsListener(movements ->
      assertListEqualsWithoutOrdering(Arrays.asList(
        new Position(0, 0),
        new Position(0, 1),
        new Position(0, 2),
        new Position(1, 0),
        new Position(1, 1),
        new Position(1, 2),
        new Position(2, 0),
        new Position(2, 1),
        new Position(2, 2)
      ), movements), calledAvailableMovements
    );
    gameViewMock.notifyEvent(new ActionChosenEvent(gameViewMock, Action.MOVE_MOVE_MOVE));
    assertTrue(calledAvailableMovements.get());

    AtomicBoolean calledEndTurn = new AtomicBoolean(false);
    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    gameViewMock.setEndTurnListener(calledEndTurn);
    status.registerObserver(receivedModelEvents::add);
    gameViewMock.notifyEvent(new MovementChosenEvent(gameViewMock, new Position(2, 0)));
    assertTrue(calledEndTurn.get());
    assertContainsOneSatisfying(e -> {
      DashboardCell c = ((DashboardCellUpdatedEvent) e).getCell();
      return c.getLine() == 1 && c.getCell() == 1;
    }, receivedModelEvents);
    assertContainsOneSatisfying(e -> {
      DashboardCell c = ((DashboardCellUpdatedEvent) e).getCell();
      return c.getLine() == 2 && c.getCell() == 0;
    }, receivedModelEvents);
    assertEquals(Position.of(2, 0), status.getPlayerPosition(PlayerColor.YELLOW));
  }

}
