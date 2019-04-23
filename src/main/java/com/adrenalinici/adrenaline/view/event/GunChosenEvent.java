package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.model.Gun;
import com.adrenalinici.adrenaline.view.GameView;

import java.util.function.Consumer;

public class GunChosenEvent extends BaseViewEvent {

  Gun gun;

  public GunChosenEvent(GameView view, Gun gun) {
    super(view);
    this.gun = gun;
  }

  public Gun getGun() {
    return gun;
  }

  @Override
  public void onGunChosenEvent(Consumer<GunChosenEvent> consumer) {
    consumer.accept(this);
  }
}
