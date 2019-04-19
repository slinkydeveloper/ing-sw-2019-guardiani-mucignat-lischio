package com.adrenalinici.adrenaline.controller.state;

import com.adrenalinici.adrenaline.controller.ControllerStateFactory;
import com.adrenalinici.adrenaline.model.GameStatus;
import com.adrenalinici.adrenaline.model.Gun;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.List;
import java.util.function.Consumer;

public class ExecuteReloadState implements ControllerState {

  public static final ControllerState INSTANCE = new ExecuteReloadState();

  @Override
  public void acceptEvent(ViewEvent viewEventToAccept, GameStatus gameStatus, PlayerColor turnOfPlayer, List<ControllerStateFactory> nextStates, Consumer<ViewEvent> endStateCallback) {
    viewEventToAccept.onGunToReloadChosenEvent(gunToReloadChosenEvent -> {
      Gun chosenGun = gunToReloadChosenEvent.getChosenGunToReload();
      //chiamo metodo di gameStatus che reloada l'arma e scala le ammo (e le powerUp) dalla playerDashboard
    });
  }
}
