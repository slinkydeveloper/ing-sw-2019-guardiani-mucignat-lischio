package com.adrenalinici.adrenaline.model.common;

import java.util.List;
import java.util.function.Consumer;

public class BaseEffectGun extends BaseGun {

  private Effect baseEffect;
  private Effect firstExtraEffect;
  private List<AmmoColor> firstExtraEffectCost;
  private Effect secondExtraEffect;
  private List<AmmoColor> secondExtraEffectCost;

  public BaseEffectGun(String id, AmmoColor firstAmmo, List<AmmoColor> extraAmmo, String name, String note, Effect baseEffect, Effect firstExtraEffect, List<AmmoColor> firstExtraEffectCost, Effect secondExtraEffect, List<AmmoColor> secondExtraEffectCost) {
    super(id, firstAmmo, extraAmmo, name, note);
    this.baseEffect = baseEffect;
    this.firstExtraEffect = firstExtraEffect;
    this.firstExtraEffectCost = firstExtraEffectCost;
    this.secondExtraEffect = secondExtraEffect;
    this.secondExtraEffectCost = secondExtraEffectCost;
  }

  public Effect getBaseEffect() {
    return baseEffect;
  }

  public boolean hasFirstExtraEffect() {
    return firstExtraEffect != null;
  }

  public Effect getFirstExtraEffect() {
    return firstExtraEffect;
  }

  public List<AmmoColor> getFirstExtraEffectCost() {
    return firstExtraEffectCost;
  }

  public boolean hasSecondExtraEffect() {
    return secondExtraEffect != null;
  }

  public Effect getSecondExtraEffect() {
    return secondExtraEffect;
  }

  public List<AmmoColor> getSecondExtraEffectCost() {
    return secondExtraEffectCost;
  }

  @Override
  public void visit(Consumer<AlternativeEffectGun> visitAlternativeEffectGun, Consumer<BaseEffectGun> visitBaseEffectGun) {
    if (visitBaseEffectGun != null) {
      visitBaseEffectGun.accept(this);
    }
  }
}
