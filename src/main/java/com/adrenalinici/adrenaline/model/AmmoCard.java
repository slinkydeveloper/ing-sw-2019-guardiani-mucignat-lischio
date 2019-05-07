package com.adrenalinici.adrenaline.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class AmmoCard {
  private final List<AmmoColor> ammoColor;
  private final PowerUpCard powerUpCard;

  public AmmoCard(List<AmmoColor> ammoColor, PowerUpCard powerUpCard) {
    this.ammoColor = ammoColor;
    this.powerUpCard = powerUpCard;
  }

  public List<AmmoColor> getAmmoColor() {
    return ammoColor;
  }

  public Optional<PowerUpCard> getPowerUpCard() {
    return Optional.ofNullable(this.powerUpCard);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AmmoCard ammoCard = (AmmoCard) o;
    return Objects.equals(ammoColor, ammoCard.ammoColor) &&
      Objects.equals(powerUpCard, ammoCard.powerUpCard);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ammoColor, powerUpCard);
  }
}
