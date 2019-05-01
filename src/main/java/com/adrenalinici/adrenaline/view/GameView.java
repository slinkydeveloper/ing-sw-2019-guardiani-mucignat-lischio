package com.adrenalinici.adrenaline.view;

import com.adrenalinici.adrenaline.model.*;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.util.Observer;

import java.util.List;
import java.util.Map;

public interface GameView extends Observer<ModelEvent> {

  void showAvailableActions(List<Action> actions);

  void showAvailableMovements(List<Position> positions);

  void showNextTurn(PlayerColor player);

  void showReloadableGuns(List<Gun> guns);

  void showLoadedGuns(List<Gun> guns);

  void showBaseGunExtraEffects(List<Effect> effects);

  void showAvailableRespawnLocations(List<AmmoColor> respawnLocations);

  void showAvailableAlternativeEffectsGun(Effect firstEffect, Effect secondEffect);

  void showChoosePlayerToHit(List<PlayerColor> players);

  void showChoosePlayerToMove(Map<PlayerColor, List<Position>> availableMovements);

  void showAvailableExtraEffects(Effect firstExtraEffect, Effect secondExtraEffect);

  void showAvailableVenomGranades(PlayerColor player);

  void showAvailablePowerUpCards(PlayerColor player);

  void showAvailableEnemyMovements(List<Position> positions);

  void showAvailableGuns(List<Gun> guns);

  void showAvailableGunsToPickup(List<Gun> guns);

}
