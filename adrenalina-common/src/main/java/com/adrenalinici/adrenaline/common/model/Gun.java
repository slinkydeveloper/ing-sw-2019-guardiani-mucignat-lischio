package com.adrenalinici.adrenaline.common.model;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

public interface Gun extends Serializable {

  List<AmmoColor> getRequiredAmmoToPickup();

  List<AmmoColor> getRequiredAmmoToReload();

  String getId();

  void visit(Consumer<AlternativeEffectGun> visitAlternativeEffectGun, Consumer<BaseEffectGun> visitBaseEffectGun);
}
