package com.adrenalinici.adrenaline.network.client;

import com.adrenalinici.adrenaline.network.inbox.InboxMessage;
import com.adrenalinici.adrenaline.network.outbox.*;
import com.adrenalinici.adrenaline.util.Observable;
import com.adrenalinici.adrenaline.view.BaseClientGameView;

public class ClientViewProxy extends Observable<InboxMessage> {

  private BaseClientGameView view;

  public ClientViewProxy(BaseClientGameView view) {
    this.view = view;
    this.view.registerObserver(this::notifyEvent);
  }

  public void handleNewServerMessage(OutboxMessage message) {
    message.onAvailableActionsMessage(e -> view.showAvailableActions(e.getActions()));
    message.onAvailableMovementsMessage(e -> view.showAvailableMovements(e.getPositions()));
    message.onNextTurnMessage(e -> view.showNextTurn(e.getPlayer()));
    message.onReloadableGunsMessage(e -> view.showReloadableGuns(e.getGuns()));
    message.onLoadedGunsMessage(e -> view.showLoadedGuns(e.getGuns()));
    message.onBaseGunExtraEffectsMessage(e -> view.showBaseGunExtraEffects(e.getEffects()));
    message.onAvailableRespawnLocationsMessage(e -> view.showAvailableRespawnLocations(e.getRespawnLocations()));
    message.onAvailableAlternativeEffectsGunMessage(e -> view.showAvailableAlternativeEffectsGun(e.getFirstEffect(), e.getSecondEffect()));
    message.onChoosePlayerToHitMessage(e -> view.showChoosePlayerToHit(e.getPlayers()));
    message.onChoosePlayerToMoveMessage(e -> view.showChoosePlayerToMove(e.getAvailableMovements()));
    message.onAvailableExtraEffectsMessage(e -> view.showAvailableExtraEffects(e.getFirstExtraEffect(), e.getSecondExtraEffect()));
    message.onAvailableVenomGranadesMessage(e -> view.showAvailableVenomGranades(e.getPlayer()));
    message.onAvailablePowerUpCardsMessage(e -> view.showAvailablePowerUpCards(e.getPlayer()));
    message.onAvailableEnemyMovementsMessage(e -> view.showAvailableEnemyMovements(e.getPositions()));
    message.onAvailableGunsMessage(e -> view.showAvailableGuns(e.getGuns()));
    message.onAvailableGunsToPickupMessage(e -> view.showAvailableGunsToPickup(e.getGuns()));
    message.onModelEventMessage(e -> view.onEvent(e.getModelEvent()));
    message.onChooseMyPlayerMessage(e -> view.showChooseMyPlayer(e.getPlayerColors()));
  }
}