package com.adrenalinici.adrenaline.common.model;

import java.util.List;
import java.util.function.Consumer;

public class AlternativeEffectGun extends BaseGun {
  private Effect firstEffect;
  private Effect secondEffect;
  private List<AmmoColor> secondEffectCost;

  public AlternativeEffectGun(String id, AmmoColor firstAmmo, List<AmmoColor> extraAmmo, Effect firstEffect, Effect secondEffect, List<AmmoColor> secondEffectCost) {
    super(id, firstAmmo, extraAmmo);
    this.firstEffect = firstEffect;
    this.secondEffect = secondEffect;
    this.secondEffectCost = secondEffectCost;
  }

  public Effect getFirstEffect() {
    return firstEffect;
  }

  public Effect getSecondEffect() {
    return secondEffect;
  }

  public List<AmmoColor> getSecondEffectCost() {
    return secondEffectCost;
  }

  @Override
  public void visit(Consumer<AlternativeEffectGun> visitAlternativeEffectGun, Consumer<BaseEffectGun> visitBaseEffectGun) {
    if (visitAlternativeEffectGun != null) {
      visitAlternativeEffectGun.accept(this);
    }
  }
}
