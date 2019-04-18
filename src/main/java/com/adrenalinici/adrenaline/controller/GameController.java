package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.Action;
import com.adrenalinici.adrenaline.model.GameStatus;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.util.Observer;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class GameController implements Observer<ViewEvent> {

  private GameStatus gameStatus;

  private int remainingActions;
  private PlayerColor turnOfPlayer;
  private ControllerState state;
  private List<ControllerStateFactory> nextStates;

  public GameController(GameStatus status) {
    this.gameStatus = status;
    this.state = WaitingNewTurnState.INSTANCE;
    nextStates = new ArrayList<>();
  }

  @Override
  public void onEvent(ViewEvent event) {
    state.acceptEvent(event, this);
  }

  private PlayerColor nextTurnPlayer() {
    List<PlayerColor> players = gameStatus.getPlayers();
    return players.get(players.indexOf(turnOfPlayer) + 1 % players.size());
  }

  @SuppressWarnings("unchecked")
  void endStateCallback(ViewEvent event) {
    if (!nextStates.isEmpty()) {
      state = nextStates.remove(0).create(state);
      state.acceptEvent(event, this);
    } else {
      if (remainingActions != 0) {
        event.getView().showAvailableActions(calculateAvailableActions());
      } else {
        //TODO refill dashboard
        event.getView().showNextTurn(nextTurnPlayer());
        //TODO somewhere maybe here should go the check if match finished
        state = WaitingNewTurnState.INSTANCE;
      }
    }
  }

  List<Action> calculateAvailableActions() {
    //TODO based on turn of player
    return Arrays.asList(Action.MOVE_MOVE_MOVE, Action.MOVE_PICKUP, Action.SHOOT);
  }

  GameController configureRemainingActions() {
    remainingActions = 2; //TODO based on turn of player
    return this;
  }

  List<ControllerStateFactory> getNextStates() {
    return nextStates;
  }

  void addNextStateOnHead(ControllerStateFactory factory) {
    nextStates.add(0, factory);
  }

  void addNextStatesOnHead(Collection<ControllerStateFactory> factories) {
    nextStates.addAll(0, factories);
  }

  PlayerColor getTurnOfPlayer() {
    return turnOfPlayer;
  }

  GameController setTurnOfPlayer(PlayerColor turnOfPlayer) {
    this.turnOfPlayer = turnOfPlayer;
    return this;
  }

  int getRemainingActions() {
    return remainingActions;
  }

  GameController setRemainingActions(int remainingActions) {
    this.remainingActions = remainingActions;
    return this;
  }

  GameController decrementRemainingActions() {
    this.remainingActions--;
    return this;
  }

  void setControllerState(ControllerState controllerState) {
    this.state = controllerState;
  }

  GameStatus getGameStatus() {
    return gameStatus;
  }

}
