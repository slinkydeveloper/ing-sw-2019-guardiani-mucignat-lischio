package com.adrenalinici.adrenaline.model;

import java.util.List;
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
}
