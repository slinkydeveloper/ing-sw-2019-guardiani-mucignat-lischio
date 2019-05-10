package com.adrenalinici.adrenaline.model.common;

import java.util.Objects;

public class PowerUpCard {

  private AmmoColor ammoColor;
  private PowerUpType powerUpType;

  public PowerUpCard(AmmoColor ammoColor, PowerUpType powerUpType) {
    this.ammoColor = ammoColor;
    this.powerUpType = powerUpType;
  }

  public AmmoColor getAmmoColor() {
    return ammoColor;
  }

  public PowerUpType getPowerUpType() {
    return powerUpType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PowerUpCard that = (PowerUpCard) o;
    return ammoColor == that.ammoColor &&
      powerUpType == that.powerUpType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(ammoColor, powerUpType);
  }

  @Override
  public String toString() {
    return "PowerUpCard{" +
      "ammoColor=" + ammoColor +
      ", powerUpType=" + powerUpType +
      '}';
  }
}
