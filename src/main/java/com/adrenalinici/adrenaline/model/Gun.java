package com.adrenalinici.adrenaline.model;

import java.util.List;
import java.util.function.Consumer;

public interface Gun {
    List<AmmoColor> getRequiredAmmoToPickup();

    List<AmmoColor> getRequiredAmmoToReload();

    void visit(Consumer<AlternativeEffectGun> visitAlternativeEffectGun, Consumer<BaseEffectGun> visitBaseEffectGun);
}
