package com.adrenalinici.adrenaline.common.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public final class AmmoCard implements Serializable {
  private final List<AmmoColor> ammoColor;
  private final boolean pickPowerUp;

  public AmmoCard(List<AmmoColor> ammoColor, boolean pickPowerUp) {
    this.ammoColor = ammoColor;
    this.pickPowerUp = pickPowerUp;
  }

  public List<AmmoColor> getAmmoColor() {
    return ammoColor;
  }

  public boolean isPickPowerUp() {
    return pickPowerUp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AmmoCard ammoCard = (AmmoCard) o;
    return pickPowerUp == ammoCard.pickPowerUp &&
      Objects.equals(ammoColor, ammoCard.ammoColor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ammoColor, pickPowerUp);
  }
}
