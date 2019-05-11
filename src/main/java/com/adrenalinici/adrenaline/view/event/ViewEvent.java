package com.adrenalinici.adrenaline.view.event;

import java.io.Serializable;
import java.util.function.Consumer;

public interface ViewEvent extends Serializable {

  default void onActionChosenEvent(Consumer<ActionChosenEvent> consumer) {
  }

  default void onMovementChosenEvent(Consumer<MovementChosenEvent> consumer) {
  }

  default void onNewTurnEvent(Consumer<NewTurnEvent> consumer) {
  }

  default void onAlternativeGunEffectChosenEvent(Consumer<AlternativeGunEffectChosenEvent> consumer) {
  }

  default void onGunChosenEvent(Consumer<GunChosenEvent> consumer) {
  }

  default void onPlayerChosenEvent(Consumer<PlayerChosenEvent> consumer) {
  }

  default void onBaseGunEffectChosenEvent(Consumer<BaseGunEffectChosenEvent> consumer) {
  }

  default void onPowerUpChosenEvent(Consumer<PowerUpCardChosenEvent> consumer) {
  }
}
