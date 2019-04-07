package com.adrenalinici.adrenaline.controller.state;

import com.adrenalinici.adrenaline.view.event.ViewEvent;

public interface ControllerState {

  void acceptEvent(ViewEvent viewEventToAccept);

}
