package com.adrenalinici.adrenaline.view;

import com.adrenalinici.adrenaline.model.*;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.util.Observer;

import java.util.List;
import java.util.Optional;

public interface GameView extends Observer<ModelEvent> {
  void showAvailableActions(List<Action> actions);

  void showAvailableMovements(List<Position> availableMovements);

  void showEndTurn();

  void showReloadableGuns(List<Gun> reloadableGuns);

  void showLoadedGuns(List<Gun> reloadableGuns);

  void showBaseGunExtraEffects(List<Effect> effects);

  void showAvailableRespawnLocations(List<AmmoColor> respawnLocations);

  void showApplicableEffects(Optional<List<PlayerColor>> firstEffect, int numberOfChoosablePlayerFirstEffect, Optional<List<PlayerColor>> secondEffect, int numberOfChoosablePlayerSecondEffect);

  void showApplicableEffects(List<PlayerColor> baseEffect, int numberOfChoosablePlayerBaseEffect, Optional<List<PlayerColor>> firstExtraEffect, int numberOfChoosablePlayerFirstExtraEffect, Optional<List<PlayerColor>> secondExtraEffect, int numberOfChoosablePlayerSecondExtraEffect);

  void showAvailableVenomGranades(PlayerColor player);

  void showAvailablePowerUpCards(PlayerColor player);

  void showAvailableEnemyMovements(List<Position> availableEnemyMovements);

  void showAvailableGunsToPickup(List<Gun> availableGunsToPickup);
}
