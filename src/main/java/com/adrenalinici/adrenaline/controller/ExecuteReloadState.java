package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.Gun;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

public class ExecuteReloadState implements ControllerState {

  public static final ControllerState INSTANCE = new ExecuteReloadState();

  @Override
  public void acceptEvent(ViewEvent viewEventToAccept, GameController controller) {
    viewEventToAccept.onGunToReloadChosenEvent(gunToReloadChosenEvent -> {
      Gun chosenGun = gunToReloadChosenEvent.getChosenGunToReload();
      controller.getGameStatus().reloadGun(controller.getTurnOfPlayer(), chosenGun);
      controller.endStateCallback(viewEventToAccept);
    });
  }
}
