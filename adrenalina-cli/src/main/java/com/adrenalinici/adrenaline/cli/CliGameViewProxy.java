package com.adrenalinici.adrenaline.cli;

import com.adrenalinici.adrenaline.client.ClientViewProxy;
import com.adrenalinici.adrenaline.common.network.inbox.InboxMessage;
import com.adrenalinici.adrenaline.common.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.common.util.ObservableImpl;

public class CliGameViewProxy extends ObservableImpl<InboxMessage> implements ClientViewProxy {

  private BaseCliGameView view;

  public CliGameViewProxy(BaseCliGameView view) {
    this.view = view;
    this.view.registerObserver(this::notifyEvent);
  }

  @Override
  public void handleNewServerMessage(OutboxMessage message) {
    message.onAvailableActionsMessage(e -> view.showAvailableActions(e.getActions()));
    message.onAvailableMovementsMessage(e -> view.showAvailableMovements(e.getPositions()));
    message.onNextTurnMessage(e -> view.showNextTurn(e.getPlayer()));
    message.onReloadableGunsMessage(e -> view.showReloadableGuns(e.getGuns()));
    message.onAvailablePowerUpCardsForRespawnMessage(e -> view.showAvailablePowerUpCardsForRespawn(e.getPlayer(), e.getPowerUpCards()));
    message.onAvailableAlternativeEffectsGunMessage(e -> view.showAvailableAlternativeEffectsGun(e.getFirstEffect(), e.getSecondEffect()));
    message.onChoosePlayerToHitMessage(e -> view.showChoosePlayerToHit(e.getPlayers()));
    message.onAvailableExtraEffectsMessage(e -> view.showAvailableExtraEffects(e.getFirstExtraEffect(), e.getSecondExtraEffect()));
    message.onAvailableGunsMessage(e -> view.showAvailableGuns(e.getGuns()));
    message.onAvailableGunsToPickupMessage(e -> view.showAvailableGunsToPickup(e.getGuns()));
    message.onAvailableTagbackGrenadeMessage(e -> view.showAvailableTagbackGrenade(e.getPlayer(), e.getPowerUpCards()));
    message.onAvailableRoomsMessage(e -> view.showAvailableRooms(e.getRooms()));
    message.onAvailableCellsToHitMessage(e -> view.showAvailableCellsToHit(e.getCells()));
    message.onModelEventMessage(e -> view.onEvent(e.getModelEvent()));
    message.onAvailableMatchesMessage(e -> view.showAvailableMatchesAndPlayers(e.getMatchesRemainingPlayers()));
    message.onInfoMessage(e -> view.showInfoMessage(e.getInformation(), e.getInfoType()));
  }
}
