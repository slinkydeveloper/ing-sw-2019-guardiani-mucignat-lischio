package com.adrenalinici.adrenaline.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class AlternativeEffectGun extends BaseGun {
    private Effect firstEffect;
    private Effect secondEffect;
    private List<AmmoColor> secondEffectCost;

    public AlternativeEffectGun(AmmoColor firstAmmo, List<AmmoColor> extraAmmo, String name, Optional<String> note, Effect firstEffect, Effect secondEffect, List<AmmoColor> secondEffectCost) {
        super(firstAmmo, extraAmmo, name, note);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlternativeEffectGun that = (AlternativeEffectGun) o;
        return firstEffect.equals(that.firstEffect) &&
                secondEffect.equals(that.secondEffect) &&
                secondEffectCost.equals(that.secondEffectCost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstEffect, secondEffect, secondEffectCost);
    }
}
