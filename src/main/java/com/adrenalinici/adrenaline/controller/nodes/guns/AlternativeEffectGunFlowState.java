package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.DecoratedAlternativeEffectGun;
import com.adrenalinici.adrenaline.controller.DecoratedEffect;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface AlternativeEffectGunFlowState extends GunFlowState {
  DecoratedAlternativeEffectGun getChosenGun();

  DecoratedEffect getChosenEffect();

  AlternativeEffectGunFlowState setChosenEffect(DecoratedEffect chosenEffect, boolean chosenEffectIsFirstEffect);

  boolean isFirstEffect();

  @Override
  ObjectNode resolvePhaseConfiguration(String phaseId);
}
