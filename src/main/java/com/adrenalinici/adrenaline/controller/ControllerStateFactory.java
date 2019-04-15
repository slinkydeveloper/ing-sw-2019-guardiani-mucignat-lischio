package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.controller.state.ControllerState;

@FunctionalInterface
public interface ControllerStateFactory<OLD_STATE extends ControllerState, NEW_STATE extends ControllerState> {

  NEW_STATE create(OLD_STATE oldState);

}
