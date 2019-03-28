package com.adrenalinici.adrenaline.model;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class AlternativeEffectGun extends BaseGun {
    private Effect firstEffect;
    private Effect secondEffect;
    private List<AmmoColor> secondEffectCost;

    public AlternativeEffectGun(AmmoColor firstAmmo, List<AmmoColor> extraAmmo, String name, Optional<String> note) {
        super(firstAmmo, extraAmmo, name, note);
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
