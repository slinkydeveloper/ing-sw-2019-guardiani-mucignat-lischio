package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.GameStatus;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.List;
import java.util.function.Consumer;

public class ReloadChosenState implements ControllerState {

  public static final ControllerState INSTANCE = new ReloadChosenState();

  @Override
  public void acceptEvent(ViewEvent viewEventToAccept, GameController controller) {
    controller.addNextStateOnHead(oldState -> ExecuteReloadState.INSTANCE);
    viewEventToAccept.getView().showReloadableGuns(
      controller.getGameStatus().calculateReloadableGuns(controller.getTurnOfPlayer())
    );
    controller.endStateCallback(viewEventToAccept);
  }
}
