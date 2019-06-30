package com.adrenalinici.adrenaline.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class BaseGun implements Gun {

  private String id;
  private AmmoColor firstAmmo;
  private List<AmmoColor> extraAmmo;

  public BaseGun(String id, AmmoColor firstAmmo, List<AmmoColor> extraAmmo) {
    this.id = id;
    this.firstAmmo = firstAmmo;
    this.extraAmmo = extraAmmo;
  }

  @Override
  public String getId() {
    return id;
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BaseGun baseGun = (BaseGun) o;
    return Objects.equals(id, baseGun.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
