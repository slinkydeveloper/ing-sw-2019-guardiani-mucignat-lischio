package com.adrenalinici.adrenaline.controller.state;

import com.adrenalinici.adrenaline.controller.ControllerStateFactory;
import com.adrenalinici.adrenaline.model.GameStatus;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.List;
import java.util.function.Consumer;

public class ReloadChosenState implements ControllerState {

  public static final ControllerState INSTANCE = new ReloadChosenState();

  @Override
  public void acceptEvent(ViewEvent viewEventToAccept, GameStatus gameStatus, PlayerColor turnOfPlayer, List<ControllerStateFactory> nextStates, Consumer<ViewEvent> endStateCallback) {
    nextStates.add(0, oldState -> ExecuteGunPickupState.INSTANCE);
    viewEventToAccept.getView().showReloadableGuns(gameStatus.calculateReloadableGuns(turnOfPlayer));
    endStateCallback.accept(viewEventToAccept);
  }
}
