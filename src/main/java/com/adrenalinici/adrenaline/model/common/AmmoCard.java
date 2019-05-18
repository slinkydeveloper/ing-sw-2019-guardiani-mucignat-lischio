package com.adrenalinici.adrenaline.model.common;

import java.util.Objects;
import java.util.Set;

public final class AmmoCard {
  private final Set<AmmoColor> ammoColor;
  private final boolean pickPowerUp;

  public AmmoCard(Set<AmmoColor> ammoColor, boolean pickPowerUp) {
    this.ammoColor = ammoColor;
    this.pickPowerUp = pickPowerUp;
  }

  public Set<AmmoColor> getAmmoColor() {
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
