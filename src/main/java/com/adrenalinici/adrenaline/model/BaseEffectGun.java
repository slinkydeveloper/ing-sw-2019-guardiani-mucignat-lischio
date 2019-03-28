package com.adrenalinici.adrenaline.model;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class BaseEffectGun extends BaseGun {

    private Effect baseEffect;
    private Optional<Effect> firstExtraEffect;
    private Optional<List<AmmoColor>> firstExtraEffectCost;
    private Optional<Effect> secondExtraEffect;
    private Optional<List<AmmoColor>> secondExtraEffectCost;

    public BaseEffectGun(AmmoColor firstAmmo, List<AmmoColor> extraAmmo, String name, Optional<String> note, Effect baseEffect, Optional<Effect> firstExtraEffect, Optional<List<AmmoColor>> firstExtraEffectCost, Optional<Effect> secondExtraEffect, Optional<List<AmmoColor>> secondExtraEffectCost) {
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
        return firstExtraEffect;
    }

    public Optional<List<AmmoColor>> getFirstExtraEffectCost() {
        return firstExtraEffectCost;
    }

    public Optional<Effect> getSecondExtraEffect() {
        return secondExtraEffect;
    }

    public Optional<List<AmmoColor>> getSecondExtraEffectCost() {
        return secondExtraEffectCost;
    }

    @Override
    public void visit(Consumer<AlternativeEffectGun> visitAlternativeEffectGun, Consumer<BaseEffectGun> visitBaseEffectGun) {
        if (visitBaseEffectGun != null) {
            visitBaseEffectGun.accept(this);
        }
    }
}