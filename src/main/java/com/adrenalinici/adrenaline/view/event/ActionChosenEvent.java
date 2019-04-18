package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.model.Action;
import com.adrenalinici.adrenaline.view.GameView;

import java.util.function.Consumer;

public class ActionChosenEvent extends BaseViewEvent {

  private Action action;

  public ActionChosenEvent(GameView view, Action action) {
    super(view);
    this.action = action;
  }

  public Action getAction() {
    return action;
  }

  @Override
  public void onActionChosenEvent(Consumer<ActionChosenEvent> consumer) {
    consumer.accept(this);
  }

  @Override
  public void onMovementChosenEvent(Consumer<MovementChosenEvent> consumer) {
  }

  @Override
  public void onNewTurnEvent(Consumer<NewTurnEvent> consumer) {
  }

  @Override
  public void onGunToPickupChosenEvent(Consumer<GunToPickupChosenEvent> consumer) {
  }
}
