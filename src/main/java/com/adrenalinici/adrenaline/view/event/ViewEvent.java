package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.view.GameView;

import java.util.function.Consumer;

public interface ViewEvent {

  GameView getView();

  void onActionChosenEvent(Consumer<ActionChosenEvent> consumer);

  void onMovementChosenEvent(Consumer<MovementChosenEvent> consumer);

  void onNewTurnEvent(Consumer<NewTurnEvent> consumer);

  void onGunToPickupChosenEvent(Consumer<GunToPickupChosenEvent> consumer);
}
