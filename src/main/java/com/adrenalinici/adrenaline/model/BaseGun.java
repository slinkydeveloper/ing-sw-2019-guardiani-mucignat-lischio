package com.adrenalinici.adrenaline.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class BaseGun implements Gun {

    private AmmoColor firstAmmo;
    private List<AmmoColor> extraAmmo;
    private String name;
    private Optional<String> note;

    public BaseGun(AmmoColor firstAmmo, List<AmmoColor> extraAmmo, String name, Optional<String> note) {
        this.firstAmmo = firstAmmo;
        this.extraAmmo = extraAmmo;
        this.name = name;
        this.note = note;
    }

    @Override
    public List<AmmoColor> getRequiredAmmoToPickup() {
        return extraAmmo;
    }

    @Override
    public List<AmmoColor> getRequiredAmmoToReload() {
        List<AmmoColor> requiredAmmoToReload = new ArrayList<>();
        requiredAmmoToReload.add(firstAmmo);
        requiredAmmoToReload.addAll(extraAmmo);
        return requiredAmmoToReload;
    }
}
