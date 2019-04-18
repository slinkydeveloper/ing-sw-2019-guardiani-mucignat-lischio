package com.adrenalinici.adrenaline.controller.state;

import com.adrenalinici.adrenaline.controller.ControllerStateFactory;
import com.adrenalinici.adrenaline.model.*;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ExecuteGunPickupState implements ControllerState {

  public static final ExecuteGunPickupState INSTANCE = new ExecuteGunPickupState();

  @Override
  public void acceptEvent(ViewEvent viewEventToAccept, GameStatus gameStatus, PlayerColor turnOfPlayer, List<ControllerStateFactory> nextStates, Consumer<ViewEvent> endStateCallback) {
    viewEventToAccept.onGunToPickupChosenEvent(gunToPickupChosenEvent -> {
      //TODO ci sarebbe anche da gestire il caso in cui si supera il limite delle tre armi
      Gun chosenGun = gunToPickupChosenEvent.getChosenGunToPickup();
      RespawnDashboardCell cell = gunToPickupChosenEvent.getCell();
      gameStatus.acquireGun(cell, turnOfPlayer, chosenGun);
      endStateCallback.accept(viewEventToAccept);
    });
  }
}
