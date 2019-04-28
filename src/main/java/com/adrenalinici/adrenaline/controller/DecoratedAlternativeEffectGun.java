package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.AlternativeEffectGun;
import com.adrenalinici.adrenaline.model.AmmoColor;
import com.adrenalinici.adrenaline.model.BaseEffectGun;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class DecoratedAlternativeEffectGun implements DecoratedGun {

  AlternativeEffectGun alternativeEffectGun;
  List<String> phases;
  DecoratedEffect firstEffect;
  DecoratedEffect secondEffect;

  public DecoratedAlternativeEffectGun(AlternativeEffectGun alternativeEffectGun, List<String> phases, DecoratedEffect firstEffect, DecoratedEffect secondEffect) {
    this.alternativeEffectGun = alternativeEffectGun;
    this.phases = phases;
    this.firstEffect = firstEffect;
    this.secondEffect = secondEffect;
  }

  public DecoratedEffect getFirstEffect() {
    return firstEffect;
  }

  public DecoratedEffect getSecondEffect() {
    return secondEffect;
  }

  public List<AmmoColor> getSecondEffectCost() {
    return alternativeEffectGun.getSecondEffectCost();
  }

  public AlternativeEffectGun get() {
    return alternativeEffectGun;
  }

  @Override
  public void visit(Consumer<AlternativeEffectGun> visitAlternativeEffectGun, Consumer<BaseEffectGun> visitBaseEffectGun) {
    alternativeEffectGun.visit(visitAlternativeEffectGun, visitBaseEffectGun);
  }

  @Override
  public String getId() {
    return alternativeEffectGun.getId();
  }

  @Override
  public List<AmmoColor> getRequiredAmmoToPickup() {
    return alternativeEffectGun.getRequiredAmmoToPickup();
  }

  @Override
  public List<AmmoColor> getRequiredAmmoToReload() {
    return alternativeEffectGun.getRequiredAmmoToReload();
  }

  @Override
  public String getName() {
    return alternativeEffectGun.getName();
  }

  @Override
  public Optional<String> getNote() {
    return alternativeEffectGun.getNote();
  }

  @Override
  public List<String> getPhases() {
    return phases;
  }
}
