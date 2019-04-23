package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.view.event.ViewEvent;

public class WaitingNewTurnState implements ControllerState {

  public final static WaitingNewTurnState INSTANCE = new WaitingNewTurnState();

  @Override
  public void acceptEvent(ViewEvent viewEventToAccept, GameController controller) {
    viewEventToAccept.onNewTurnEvent(
      e -> {
        controller
          .setTurnOfPlayer(e.getPlayer())
          .configureRemainingActions();
        e.getView().showAvailableActions(controller.calculateAvailableActions());
        controller.addNextStateOnHead(oldState -> WaitingChooseActionState.INSTANCE);
        controller.addNextStateOnBottom(oldState -> ReloadChosenState.INSTANCE);
        controller.endStateCallback(e);
      }
    );
  }
}
