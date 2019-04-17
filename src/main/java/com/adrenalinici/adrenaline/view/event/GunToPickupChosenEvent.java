package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.model.Gun;
import com.adrenalinici.adrenaline.model.RespawnDashboardCell;
import com.adrenalinici.adrenaline.view.GameView;

import java.util.List;
import java.util.function.Consumer;

public class GunToPickupChosenEvent extends BaseViewEvent {

  Gun chosenGunToPickup;
  RespawnDashboardCell cell;

  public GunToPickupChosenEvent(GameView view, Gun chosenGunToPickup, RespawnDashboardCell cell) {
    super(view);
    this.chosenGunToPickup = chosenGunToPickup;
    this.cell = cell;
  }

  public Gun getChosenGunToPickup() {
    return chosenGunToPickup;
  }

  public RespawnDashboardCell getCell() {
    return cell;
  }

  @Override
  public void onActionChosenEvent(Consumer<ActionChosenEvent> consumer) {
  }

  @Override
  public void onMovementChosenEvent(Consumer<MovementChosenEvent> consumer) {
  }

  @Override
  public void onNewTurnEvent(Consumer<NewTurnEvent> consumer) {
  }

  @Override
  public void onGunToPickupChosenEvent(Consumer<GunToPickupChosenEvent> consumer) {
    consumer.accept(this);
  }
}