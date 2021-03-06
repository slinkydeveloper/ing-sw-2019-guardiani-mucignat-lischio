package com.adrenalinici.adrenaline.server.controller;

import com.adrenalinici.adrenaline.common.model.AlternativeEffectGun;

import java.util.List;

public class DecoratedAlternativeEffectGun extends DecoratedGun {

  DecoratedEffect firstEffect;
  DecoratedEffect secondEffect;

  public DecoratedAlternativeEffectGun(AlternativeEffectGun alternativeEffectGun, List<String> phases, DecoratedEffect firstEffect, DecoratedEffect secondEffect) {
    super(alternativeEffectGun, phases);
    this.firstEffect = firstEffect;
    this.secondEffect = secondEffect;
  }

  public DecoratedEffect getFirstEffect() {
    return firstEffect;
  }

  public DecoratedEffect getSecondEffect() {
    return secondEffect;
  }

  @Override
  public AlternativeEffectGun get() {
    return (AlternativeEffectGun) gun;
  }
}
