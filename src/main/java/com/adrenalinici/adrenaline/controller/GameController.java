package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.controller.state.ControllerState;
import com.adrenalinici.adrenaline.model.Action;
import com.adrenalinici.adrenaline.model.GameStatus;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.model.Position;
import com.adrenalinici.adrenaline.util.Observer;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class GameController implements Observer<ViewEvent> {

  private GameStatus status;

  private int remainingActions;
  private PlayerColor turnOfPlayer;
  private ControllerState state;
  private List<Function<ControllerState, ControllerState>> nextStates;

  public GameController(GameStatus status) {
    this.status = status;
    nextStates = new ArrayList<>();
  }

  @Override
  public void onEvent(ViewEvent event) {
    event.onNewTurnEvent(
      e -> {
        turnOfPlayer = e.getPlayer();
        remainingActions = calculateRemainingActions(e.getPlayer());
        e.getView().showAvailableActions(calculateAvailableActions(e.getPlayer()));
      }
    );
    event.onActionChosenEvent(
      e -> {
        switch (e.getAction()) {
          case MOVE_MOVE_MOVE:
            Position actualPlayerPosition = status.getDashboard().getPlayersPositions().get(turnOfPlayer);
            e.getView().showAvailableMovements(
              status.getDashboard().calculateMovements(actualPlayerPosition, 3)
            );
            break;
        }
      }
    );
    event.onMovementChosenEvent(
      e -> {
        status.movePlayerInDashboard(e.getCoordinates(), turnOfPlayer);
        handlePartialActionEnd(e);
      }
    );
  }

  private List<Action> calculateAvailableActions(PlayerColor player) {
    //TODO
    return Arrays.asList(Action.MOVE_MOVE_MOVE, Action.MOVE_PICKUP, Action.SHOOT);
  }

  private int calculateRemainingActions(PlayerColor player) {
    return 2; //TODO
  }

  private void handlePartialActionEnd(ViewEvent event) {
    if (state != null || !nextStates.isEmpty()) {
      state = nextStates.remove(0).apply(state);
      state.acceptEvent(event);
    } else {
      if (remainingActions != 0) {
        remainingActions--;
        event.getView().showAvailableActions(calculateAvailableActions(turnOfPlayer));
      } else {
        event.getView().showEndTurn();
      }
    }
  }

  PlayerColor getTurnOfPlayer() {
    return turnOfPlayer;
  }

  void setTurnOfPlayer(PlayerColor turnOfPlayer) {
    this.turnOfPlayer = turnOfPlayer;
  }

  int getRemainingActions() {
    return remainingActions;
  }

}