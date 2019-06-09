package com.adrenalinici.adrenaline.view;

import com.adrenalinici.adrenaline.model.common.*;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.util.Observer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GameView extends Observer<ModelEvent> {

  void showAvailableActions(List<Action> actions);

  void showAvailableMovements(List<Position> positions);

  void showNextTurn(PlayerColor player);

  void showReloadableGuns(Set<String> guns);

  void showAvailablePowerUpCardsForRespawn(PlayerColor player, List<PowerUpCard> powerUpCards);

  void showAvailableAlternativeEffectsGun(Effect firstEffect, Effect secondEffect);

  void showChoosePlayerToHit(List<PlayerColor> players);

  void showAvailableExtraEffects(Effect firstExtraEffect, Effect secondExtraEffect);

  void showAvailableGuns(Set<String> guns);

  void showAvailableGunsToPickup(Set<String> guns);

  void showAvailableTagbackGrenade(PlayerColor player, List<PowerUpCard> powerUpCards);

  void showAvailableRooms(Set<CellColor> rooms);

  void showAvailableCellsToHit(Set<Position> cells);

  void showRanking(List<Map.Entry<PlayerColor, Integer>> ranking);
}
