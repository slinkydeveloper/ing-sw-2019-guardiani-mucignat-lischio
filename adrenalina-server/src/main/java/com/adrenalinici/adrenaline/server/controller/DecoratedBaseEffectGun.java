package com.adrenalinici.adrenaline.server.controller;

import com.adrenalinici.adrenaline.common.model.AmmoColor;
import com.adrenalinici.adrenaline.common.model.BaseEffectGun;
import com.adrenalinici.adrenaline.common.model.Gun;

import java.util.List;

public class DecoratedBaseEffectGun extends DecoratedGun {

  DecoratedEffect baseEffect;

  DecoratedEffect firstExtraEffect;
  DecoratedEffect secondExtraEffect;

  public DecoratedBaseEffectGun(Gun gun, List<String> phases, DecoratedEffect baseEffect, DecoratedEffect firstExtraEffect, DecoratedEffect secondExtraEffect) {
    super(gun, phases);
    this.baseEffect = baseEffect;
    this.firstExtraEffect = firstExtraEffect;
    this.secondExtraEffect = secondExtraEffect;
  }

  public DecoratedEffect getBaseEffect() {
    return baseEffect;
  }

  public List<AmmoColor> getFirstExtraEffectCost() {
    return firstExtraEffect.getRequiredAmmos();
  }

  public DecoratedEffect getFirstExtraEffect() {
    return firstExtraEffect;
  }

  public boolean hasFirstExtraEffect() {
    return firstExtraEffect != null;
  }

  public List<AmmoColor> getSecondExtraEffectCost() {
    return secondExtraEffect.getRequiredAmmos();
  }

  public DecoratedEffect getSecondExtraEffect() {
    return secondExtraEffect;
  }

  public boolean hasSecondExtraEffect() {
    return secondExtraEffect != null;
  }

  @Override
  public BaseEffectGun get() {
    return (BaseEffectGun) gun;
  }
}
