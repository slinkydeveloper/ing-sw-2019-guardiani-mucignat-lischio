package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.Gun;
import com.adrenalinici.adrenaline.model.RespawnDashboardCell;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

public class ExecuteGunPickupState implements ControllerState {

  public static final ExecuteGunPickupState INSTANCE = new ExecuteGunPickupState();

  @Override
  public void acceptEvent(ViewEvent viewEventToAccept, GameController controller) {
    viewEventToAccept.onGunToPickupChosenEvent(gunToPickupChosenEvent -> {
      //TODO ci sarebbe anche da gestire il caso in cui si supera il limite delle tre armi
      Gun chosenGun = gunToPickupChosenEvent.getChosenGunToPickup();
      RespawnDashboardCell cell = gunToPickupChosenEvent.getCell();
      controller.getGameStatus().acquireGun(cell, controller.getTurnOfPlayer(), chosenGun);
      controller.endStateCallback(viewEventToAccept);
    });
  }
}
