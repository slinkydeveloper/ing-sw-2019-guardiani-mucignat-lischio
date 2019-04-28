package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.model.Gun;
import com.adrenalinici.adrenaline.view.GameView;

import java.util.function.Consumer;

public class GunToReloadChosenEvent extends BaseViewEvent {

  Gun chosenGunToReload;

  public GunToReloadChosenEvent(GameView view, Gun chosenGunToReload) {
    super(view);
    this.chosenGunToReload = chosenGunToReload;
  }

  public Gun getChosenGunToReload() {
    return chosenGunToReload;
  }

  @Override
  public void onGunToReloadChosenEvent(Consumer<GunToReloadChosenEvent> consumer) {
    consumer.accept(this);
  }
}
