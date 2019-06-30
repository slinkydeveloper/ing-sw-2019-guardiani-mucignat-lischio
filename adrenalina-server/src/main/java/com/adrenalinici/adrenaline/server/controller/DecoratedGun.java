package com.adrenalinici.adrenaline.server.controller;

import com.adrenalinici.adrenaline.common.model.AlternativeEffectGun;
import com.adrenalinici.adrenaline.common.model.AmmoColor;
import com.adrenalinici.adrenaline.common.model.BaseEffectGun;
import com.adrenalinici.adrenaline.common.model.Gun;

import java.util.List;
import java.util.function.Consumer;

public abstract class DecoratedGun implements Gun {

  Gun gun;
  List<String> phases;

  public DecoratedGun(Gun gun, List<String> phases) {
    this.gun = gun;
    this.phases = phases;
  }

  public Gun get() {
    return gun;
  }

  @Override
  public void visit(Consumer<AlternativeEffectGun> visitAlternativeEffectGun, Consumer<BaseEffectGun> visitBaseEffectGun) {
    gun.visit(visitAlternativeEffectGun, visitBaseEffectGun);
  }

  @Override
  public String getId() {
    return gun.getId();
  }

  @Override
  public List<AmmoColor> getRequiredAmmoToPickup() {
    return gun.getRequiredAmmoToPickup();
  }

  @Override
  public List<AmmoColor> getRequiredAmmoToReload() {
    return gun.getRequiredAmmoToReload();
  }

  public List<String> getPhases() {
    return phases;
  }
}
