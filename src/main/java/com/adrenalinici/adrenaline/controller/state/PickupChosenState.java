package com.adrenalinici.adrenaline.controller.state;

import com.adrenalinici.adrenaline.controller.ControllerStateFactory;
import com.adrenalinici.adrenaline.model.DashboardCell;
import com.adrenalinici.adrenaline.model.GameStatus;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.List;
import java.util.function.Consumer;

public class PickupChosenState implements ControllerState {

  public static final ControllerState INSTANCE = new PickupChosenState();

  @Override
  public void acceptEvent(ViewEvent viewEventToAccept, GameStatus gameStatus, PlayerColor turnOfPlayer, List<ControllerStateFactory> nextStates, Consumer<ViewEvent> endStateCallback) {
    DashboardCell cell = gameStatus.getDashboard().getDashboardCell(gameStatus.getPlayerPosition(turnOfPlayer))
      .get();

    cell.visit(respawnDashboardCell -> {
      nextStates.add(0, oldState -> ExecuteGunPickupState.INSTANCE);
      viewEventToAccept.getView().showAvailableGunsToPickup(gameStatus.calculateAvailableGunsToPickup(respawnDashboardCell, turnOfPlayer));
    }, pickupDashboardCell -> {
      gameStatus.acquireAmmoCard(pickupDashboardCell, turnOfPlayer);
    });

    endStateCallback.accept(viewEventToAccept);
  }
}
