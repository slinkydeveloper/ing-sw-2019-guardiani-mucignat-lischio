package com.adrenalinici.adrenaline.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class BaseEffectGun extends BaseGun {

    private Effect baseEffect;
    private Effect firstExtraEffect;
    private List<AmmoColor> firstExtraEffectCost;
    private Effect secondExtraEffect;
    private List<AmmoColor> secondExtraEffectCost;

    public BaseEffectGun(AmmoColor firstAmmo, List<AmmoColor> extraAmmo, String name, String note, Effect baseEffect, Effect firstExtraEffect, List<AmmoColor> firstExtraEffectCost, Effect secondExtraEffect, List<AmmoColor> secondExtraEffectCost) {
        super(firstAmmo, extraAmmo, name, note);
        this.baseEffect = baseEffect;
        this.firstExtraEffect = firstExtraEffect;
        this.firstExtraEffectCost = firstExtraEffectCost;
        this.secondExtraEffect = secondExtraEffect;
        this.secondExtraEffectCost = secondExtraEffectCost;
    }

    public Effect getBaseEffect() {
        return baseEffect;
    }

    public Optional<Effect> getFirstExtraEffect() {
        return Optional.ofNullable(firstExtraEffect);
    }

    public Optional<List<AmmoColor>> getFirstExtraEffectCost() {
        return Optional.ofNullable(firstExtraEffectCost);
    }

    public Optional<Effect> getSecondExtraEffect() {
        return Optional.ofNullable(secondExtraEffect);
    }

    public Optional<List<AmmoColor>> getSecondExtraEffectCost() {
        return Optional.ofNullable(secondExtraEffectCost);
    }

    @Override
    public void visit(Consumer<AlternativeEffectGun> visitAlternativeEffectGun, Consumer<BaseEffectGun> visitBaseEffectGun) {
        if (visitBaseEffectGun != null) {
            visitBaseEffectGun.accept(this);
        }
    }
}
