package com.adrenalinici.adrenaline.model;

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
}
