package com.adrenalinici.adrenaline.network.server;

import com.adrenalinici.adrenaline.model.common.*;
import com.adrenalinici.adrenaline.network.outbox.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RemoteView extends BaseRemoteView {

  public RemoteView(String matchId, ServerContext context, Set<PlayerColor> availablePlayers) {
    super(matchId, context, availablePlayers);
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
  public void showReloadableGuns(Set<String> guns) {
    broadcast(new ReloadableGunsMessage(guns));
  }

  @Override
  public void showAvailablePowerUpCardsForRespawn(PlayerColor player, List<PowerUpCard> powerUpCards) {
    broadcast(new AvailablePowerUpCardsForRespawnMessage(player, powerUpCards));
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
  public void showAvailableExtraEffects(Effect firstExtraEffect, Effect secondExtraEffect) {
    broadcast(new AvailableExtraEffectsMessage(firstExtraEffect, secondExtraEffect));
  }

  @Override
  public void showAvailableGuns(Set<String> guns) {
    broadcast(new AvailableGunsMessage(guns));
  }

  @Override
  public void showAvailableGunsToPickup(Set<String> guns) {
    broadcast(new AvailableGunsToPickupMessage(guns));
  }

  @Override
  public void showAvailableTagbackGrenade(PlayerColor player, List<PowerUpCard> powerUpCards) {
    broadcast(new AvailableTagbackGrenadeMessage(player, powerUpCards));
  }

  @Override
  public void showAvailableRooms(Set<CellColor> rooms) {
    broadcast(new AvailableRoomsMessage(rooms));
  }

  @Override
  public void showAvailableCellsToHit(Set<Position> cells) {
    broadcast(new AvailableCellsToHitMessage(cells));
  }

  @Override
  public void showRanking(List<Map.Entry<PlayerColor, Integer>> ranking) {
    broadcast(new RankingMessage(ranking));
  }
}
