package com.adrenalinici.adrenaline.model;

import java.util.List;
import java.util.Optional;

public final class AmmoCard {
    private List<AmmoColor> ammoColor;
    private Optional<PowerUpCard> powerUpCard;

    public List<AmmoColor> getAmmoColor() {
        return ammoColor;
    }

    public Optional<PowerUpCard> getPowerUpCard() {
        return powerUpCard;
    }
}
