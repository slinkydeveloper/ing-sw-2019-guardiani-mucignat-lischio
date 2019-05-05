package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.view.GameView;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;

public class GunChosenEvent implements ViewEvent {

  String gunid;

  public GunChosenEvent(String gunid) {
    this.gunid = gunid;
  }

  public String getGunid() {
    return gunid;
  }

  @Override
  public void onGunChosenEvent(Consumer<GunChosenEvent> consumer) {
    consumer.accept(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GunChosenEvent that = (GunChosenEvent) o;
    return Objects.equals(gunid, that.gunid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gunid);
  }
}
