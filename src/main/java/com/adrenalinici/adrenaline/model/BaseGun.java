package com.adrenalinici.adrenaline.model;

import java.util.*;
import java.util.function.Consumer;

public abstract class BaseGun implements Gun {

    private AmmoColor firstAmmo;
    private List<AmmoColor> extraAmmo;
    private String name;
    private String note;

    public BaseGun(AmmoColor firstAmmo, List<AmmoColor> extraAmmo, String name, String note) {
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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<String> getNote() {
        return Optional.ofNullable(note);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseGun baseGun = (BaseGun) o;
        return name.equals(baseGun.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
