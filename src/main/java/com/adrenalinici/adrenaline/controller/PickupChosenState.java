package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.DashboardCell;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

public class PickupChosenState implements ControllerState {

  public static final ControllerState INSTANCE = new PickupChosenState();

  @Override
  public void acceptEvent(ViewEvent viewEventToAccept, GameController controller) {
    DashboardCell cell = controller.getGameStatus().getDashboard().getDashboardCell(controller.getGameStatus().getPlayerPosition(controller.getTurnOfPlayer()))
      .get();

    cell.visit(respawnDashboardCell -> {
      controller.addNextStateOnHead(oldState -> ExecuteGunPickupState.INSTANCE);
      viewEventToAccept.getView().showAvailableGunsToPickup(controller.getGameStatus().calculateAvailableGunsToPickup(respawnDashboardCell, controller.getTurnOfPlayer()));
    }, pickupDashboardCell -> {
      controller.getGameStatus().acquireAmmoCard(pickupDashboardCell, controller.getTurnOfPlayer());
    });

    controller.endStateCallback(viewEventToAccept);
  }
}
