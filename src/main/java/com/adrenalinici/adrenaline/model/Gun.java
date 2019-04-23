package com.adrenalinici.adrenaline.model;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface Gun {

  List<AmmoColor> getRequiredAmmoToPickup();

  List<AmmoColor> getRequiredAmmoToReload();

  String getName();

  String getId();

  Optional<String> getNote();

  void visit(Consumer<AlternativeEffectGun> visitAlternativeEffectGun, Consumer<BaseEffectGun> visitBaseEffectGun);
}
