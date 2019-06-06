package com.adrenalinici.adrenaline.cli;

import com.adrenalinici.adrenaline.network.client.ClientViewProxy;
import com.adrenalinici.adrenaline.network.inbox.InboxMessage;
import com.adrenalinici.adrenaline.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.util.ObservableImpl;

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
    message.onLoadedGunsMessage(e -> view.showLoadedGuns(e.getGuns()));
    message.onBaseGunExtraEffectsMessage(e -> view.showBaseGunExtraEffects(e.getEffects()));
    message.onAvailablePowerUpCardsForRespawnMessage(e -> view.showAvailablePowerUpCardsForRespawn(e.getPlayer(), e.getPowerUpCards()));
    message.onAvailableAlternativeEffectsGunMessage(e -> view.showAvailableAlternativeEffectsGun(e.getFirstEffect(), e.getSecondEffect()));
    message.onChoosePlayerToHitMessage(e -> view.showChoosePlayerToHit(e.getPlayers()));
    message.onChoosePlayerToMoveMessage(e -> view.showChoosePlayerToMove(e.getAvailableMovements()));
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
