package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.view.event.ViewEvent;

public class WaitingChooseMovementState implements ControllerState {

  public static final WaitingChooseMovementState INSTANCE = new WaitingChooseMovementState();

  @Override
  public void acceptEvent(ViewEvent event, GameController controller) {
    event.onMovementChosenEvent(
      e -> {
        controller.getGameStatus().movePlayerInDashboard(e.getCoordinates(), controller.getTurnOfPlayer());
        controller.endStateCallback(e);
      }
    );
  }
}
