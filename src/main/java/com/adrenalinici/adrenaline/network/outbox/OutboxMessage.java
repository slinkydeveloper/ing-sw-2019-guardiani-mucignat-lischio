package com.adrenalinici.adrenaline.network.outbox;

import java.io.Serializable;

import java.util.function.Consumer;

public interface OutboxMessage extends Serializable {

  default void onAvailableActionsMessage(Consumer<AvailableActionsMessage> c) {}

  default void onAvailableMovementsMessage(Consumer<AvailableMovementsMessage> c) {}

  default void onNextTurnMessage(Consumer<NextTurnMessage> c) {}

  default void onReloadableGunsMessage(Consumer<ReloadableGunsMessage> c) {}

  default void onLoadedGunsMessage(Consumer<LoadedGunsMessage> c) {}

  default void onBaseGunExtraEffectsMessage(Consumer<BaseGunExtraEffectsMessage> c) {}

  default void onAvailableRespawnLocationsMessage(Consumer<AvailableRespawnLocationsMessage> c) {}

  default void onAvailableAlternativeEffectsGunMessage(Consumer<AvailableAlternativeEffectsGunMessage> c) {}

  default void onChoosePlayerToHitMessage(Consumer<ChoosePlayerToHitMessage> c) {}

  default void onChoosePlayerToMoveMessage(Consumer<ChoosePlayerToMoveMessage> c) {}

  default void onAvailableExtraEffectsMessage(Consumer<AvailableExtraEffectsMessage> c) {}

  default void onAvailableVenomGranadesMessage(Consumer<AvailableVenomGranadesMessage> c) {}

  default void onAvailablePowerUpCardsMessage(Consumer<AvailablePowerUpCardsMessage> c) {}

  default void onAvailableEnemyMovementsMessage(Consumer<AvailableEnemyMovementsMessage> c) {}

  default void onAvailableGunsMessage(Consumer<AvailableGunsMessage> c) {}

  default void onAvailableGunsToPickupMessage(Consumer<AvailableGunsToPickupMessage> c) {}

  default void onModelEventMessage(Consumer<ModelEventMessage> c) {}

  default void onChooseMyPlayerMessage(Consumer<ChooseMyPlayerMessage> c) {}
}
