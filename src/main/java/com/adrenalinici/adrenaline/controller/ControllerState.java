package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.view.event.ViewEvent;


public interface ControllerState {

  void acceptEvent(ViewEvent viewEventToAccept, GameController controller);

}
