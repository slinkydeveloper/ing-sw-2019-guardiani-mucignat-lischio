package com.adrenalinici.adrenaline.network.server;

import com.adrenalinici.adrenaline.model.*;
import com.adrenalinici.adrenaline.network.outbox.*;
import com.adrenalinici.adrenaline.network.inbox.InboxEntry;

import java.util.*;
import java.util.concurrent.BlockingQueue;

public class GameViewServer extends BaseGameViewServer {

  public GameViewServer(BlockingQueue<InboxEntry> inbox, BlockingQueue<OutboxMessage> outbox, Set<PlayerColor> availablePlayers) {
    super(inbox, outbox, availablePlayers);
  }
  @Override
  public void showAvailableActions(List<Action> actions) {
    broadcast(new AvailableActionsMessage(actions));
  }

  @Override
  public void showAvailableMovements(List<Position> positions) {
    broadcast(new AvailableMovementsMessage(positions));
  }

  @Override
  public void showNextTurn(PlayerColor player) {
    broadcast(new NextTurnMessage(player));
  }

  @Override
  public void showReloadableGuns(List<Gun> guns) {
    broadcast(new ReloadableGunsMessage(guns));
  }

  @Override
  public void showLoadedGuns(List<Gun> guns) {
    broadcast(new LoadedGunsMessage(guns));
  }

  @Override
  public void showBaseGunExtraEffects(List<Effect> effects) {
    broadcast(new BaseGunExtraEffectsMessage(effects));
  }

  @Override
  public void showAvailableRespawnLocations(List<AmmoColor> respawnLocations) {
    broadcast(new AvailableRespawnLocationsMessage(respawnLocations));
  }

  @Override
  public void showAvailableAlternativeEffectsGun(Effect firstEffect, Effect secondEffect) {
    broadcast(new AvailableAlternativeEffectsGunMessage(firstEffect, secondEffect));
  }

  @Override
  public void showChoosePlayerToHit(List<PlayerColor> players) {
    broadcast(new ChoosePlayerToHitMessage(players));
  }

  @Override
  public void showChoosePlayerToMove(Map<PlayerColor, List<Position>> availableMovements) {
    broadcast(new ChoosePlayerToMoveMessage(availableMovements));
  }

  @Override
  public void showAvailableExtraEffects(Effect firstExtraEffect, Effect secondExtraEffect) {
    broadcast(new AvailableExtraEffectsMessage(firstExtraEffect, secondExtraEffect));
  }

  @Override
  public void showAvailableVenomGranades(PlayerColor player) {
    broadcast(new AvailableVenomGranadesMessage(player));
  }

  @Override
  public void showAvailablePowerUpCards(PlayerColor player) {
    broadcast(new AvailablePowerUpCardsMessage(player));
  }

  @Override
  public void showAvailableEnemyMovements(List<Position> positions) {
    broadcast(new AvailableEnemyMovementsMessage(positions));
  }

  @Override
  public void showAvailableGuns(List<Gun> guns) {
    broadcast(new AvailableGunsMessage(guns));
  }

  @Override
  public void showAvailableGunsToPickup(List<Gun> guns) {
    broadcast(new AvailableGunsToPickupMessage(guns));
  }

}
