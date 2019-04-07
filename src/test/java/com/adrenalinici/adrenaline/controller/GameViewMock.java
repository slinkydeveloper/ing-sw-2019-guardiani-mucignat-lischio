package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.*;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.util.Observable;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class GameViewMock extends Observable<ViewEvent> implements GameView {

  private Consumer<List<Action>> availableActionsListener;
  private Consumer<List<Position>> availableMovementsListener;
  private Consumer<ModelEvent> modelEventListener;
  private Runnable endTurnListener;

  public void setAvailableActionsListener(Consumer<List<Action>> availableActionsListener, AtomicBoolean called) {
    this.availableActionsListener = (v) -> {
      called.set(true);
      availableActionsListener.accept(v);
    };
  }

  public void setAvailableMovementsListener(Consumer<List<Position>> availableMovementsListener, AtomicBoolean called) {
    this.availableMovementsListener = (v) -> {
      called.set(true);
      availableMovementsListener.accept(v);
    };
  }

  public void setEndTurnListener(AtomicBoolean called) {
    this.endTurnListener = () -> called.set(true);
  }

  public void setModelEventListener(Consumer<ModelEvent> modelEventListener) {
    this.modelEventListener = modelEventListener;
  }

  @Override
  public void notifyEvent(ViewEvent value) {
    super.notifyEvent(value);
  }

  @Override
  public void showAvailableActions(List<Action> actions) {
    this.availableActionsListener.accept(actions);
  }

  @Override
  public void showAvailableMovements(List<Position> availableMovements) {
    this.availableMovementsListener.accept(availableMovements);
  }

  @Override
  public void showEndTurn() {
    endTurnListener.run();
  }

  @Override
  public void showReloadableGuns(List<Gun> reloadableGuns) {

  }

  @Override
  public void showLoadedGuns(List<Gun> reloadableGuns) {

  }

  @Override
  public void showBaseGunExtraEffects(List<Effect> effects) {

  }

  @Override
  public void showAvailableRespawnLocations(List<AmmoColor> respawnLocations) {

  }

  @Override
  public void showApplicableEffects(Optional<List<PlayerColor>> firstEffect, int numberOfChoosablePlayerFirstEffect, Optional<List<PlayerColor>> secondEffect, int numberOfChoosablePlayerSecondEffect) {

  }

  @Override
  public void showApplicableEffects(List<PlayerColor> baseEffect, int numberOfChoosablePlayerBaseEffect, Optional<List<PlayerColor>> firstExtraEffect, int numberOfChoosablePlayerFirstExtraEffect, Optional<List<PlayerColor>> secondExtraEffect, int numberOfChoosablePlayerSecondExtraEffect) {

  }

  @Override
  public void showAvailableVenomGranades(PlayerColor player) {

  }

  @Override
  public void showAvailablePowerUpCards(PlayerColor player) {

  }

  @Override
  public void showAvailableEnemyMovements(List<Position> availableEnemyMovements) {

  }

  @Override
  public void onEvent(ModelEvent newValue) {
    modelEventListener.accept(newValue);
  }
}
