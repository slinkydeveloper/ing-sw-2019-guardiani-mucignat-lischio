package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.Position;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.Arrays;

public class WaitingChooseActionState implements ControllerState {

  public static final WaitingChooseActionState INSTANCE = new WaitingChooseActionState();

  @Override
  public void acceptEvent(ViewEvent event, GameController controller) {
    event.onActionChosenEvent(
      e -> {
        controller.decrementRemainingActions();
        switch (e.getAction()) {
          case MOVE_MOVE_MOVE:
            Position actualPlayerPosition = controller.getGameStatus().getDashboard().getPlayersPositions().get(controller.getTurnOfPlayer());
            controller.addNextStateOnHead(oldState -> WaitingChooseMovementState.INSTANCE);
            e.getView().showAvailableMovements(
              controller.getGameStatus().getDashboard().calculateMovements(actualPlayerPosition, 3)
            );
            controller.endStateCallback(e);
            break;
          case MOVE_PICKUP:
            actualPlayerPosition = controller.getGameStatus().getDashboard().getPlayersPositions().get(controller.getTurnOfPlayer());
            controller.addNextStatesOnHead(Arrays.asList(
              oldState -> WaitingChooseMovementState.INSTANCE,
              oldState -> PickupChosenState.INSTANCE
            ));
            e.getView().showAvailableMovements(
              controller.getGameStatus().getDashboard().calculateMovements(actualPlayerPosition, 1)
            );
            controller.endStateCallback(e);
            break;
        }
      }
    );
  }
}
