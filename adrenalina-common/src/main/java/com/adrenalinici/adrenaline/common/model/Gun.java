package com.adrenalinici.adrenaline.common.model;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface Gun extends Serializable {

  List<AmmoColor> getRequiredAmmoToPickup();

  List<AmmoColor> getRequiredAmmoToReload();

  String getName();

  String getId();

  Optional<String> getNote();

  void visit(Consumer<AlternativeEffectGun> visitAlternativeEffectGun, Consumer<BaseEffectGun> visitBaseEffectGun);
}
