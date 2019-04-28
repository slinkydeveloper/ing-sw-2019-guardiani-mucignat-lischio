package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.view.GameView;

import java.util.function.Consumer;

public interface ViewEvent {

  GameView getView();

  default void onActionChosenEvent(Consumer<ActionChosenEvent> consumer) {
  }

  default void onMovementChosenEvent(Consumer<MovementChosenEvent> consumer) {
  }

  default void onNewTurnEvent(Consumer<NewTurnEvent> consumer) {
  }

  default void onGunToPickupChosenEvent(Consumer<GunToPickupChosenEvent> consumer) {
  }

  default void onAlternativeGunEffectChosenEvent(Consumer<AlternativeGunEffectChosenEvent> consumer) {
  }

  default void onGunChosenEvent(Consumer<GunChosenEvent> consumer) {
  }

  default void onPlayerChosenEvent(Consumer<PlayerChosenEvent> consumer) {
  }

  default void onGunToReloadChosenEvent(Consumer<GunToReloadChosenEvent> consumer) {
  }

  default void onBaseGunEffectChosenEvent(Consumer<BaseGunEffectChosenEvent> consumer) {
  }
}
