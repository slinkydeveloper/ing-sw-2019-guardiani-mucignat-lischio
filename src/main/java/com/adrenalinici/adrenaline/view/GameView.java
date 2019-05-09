package com.adrenalinici.adrenaline.view;

import com.adrenalinici.adrenaline.model.common.Action;
import com.adrenalinici.adrenaline.model.common.AmmoColor;
import com.adrenalinici.adrenaline.model.common.Effect;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.model.common.Position;
import com.adrenalinici.adrenaline.util.Observer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GameView extends Observer<ModelEvent> {

  void showAvailableActions(List<Action> actions);

  void showAvailableMovements(List<Position> positions);

  void showNextTurn(PlayerColor player);

  void showReloadableGuns(Set<String> guns);

  void showLoadedGuns(Set<String> guns);

  void showBaseGunExtraEffects(List<Effect> effects);

  void showAvailableRespawnLocations(List<AmmoColor> respawnLocations);

  void showAvailableAlternativeEffectsGun(Effect firstEffect, Effect secondEffect);

  void showChoosePlayerToHit(List<PlayerColor> players);

  void showChoosePlayerToMove(Map<PlayerColor, List<Position>> availableMovements);

  void showAvailableExtraEffects(Effect firstExtraEffect, Effect secondExtraEffect);

  void showAvailableVenomGranades(PlayerColor player);

  void showAvailablePowerUpCards(PlayerColor player);

  void showAvailableEnemyMovements(List<Position> positions);

  void showAvailableGuns(Set<String> guns);

  void showAvailableGunsToPickup(Set<String> guns);

}
