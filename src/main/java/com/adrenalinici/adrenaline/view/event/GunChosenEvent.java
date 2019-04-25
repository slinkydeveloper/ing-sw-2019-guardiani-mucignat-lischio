package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.view.GameView;

import java.util.function.Consumer;

public class GunChosenEvent extends BaseViewEvent {

  String gunid;

  public GunChosenEvent(GameView view, String gunid) {
    super(view);
    this.gunid = gunid;
  }

  public String getGunid() {
    return gunid;
  }

  @Override
  public void onGunChosenEvent(Consumer<GunChosenEvent> consumer) {
    consumer.accept(this);
  }
}
