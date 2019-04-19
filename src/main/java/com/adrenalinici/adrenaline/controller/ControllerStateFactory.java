package com.adrenalinici.adrenaline.controller;

@FunctionalInterface
public interface ControllerStateFactory<OLD_STATE extends ControllerState, NEW_STATE extends ControllerState> {

  NEW_STATE create(OLD_STATE oldState);

}
