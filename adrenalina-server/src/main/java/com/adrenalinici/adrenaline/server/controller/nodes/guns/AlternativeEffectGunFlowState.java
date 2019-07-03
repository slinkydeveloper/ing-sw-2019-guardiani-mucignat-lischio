package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.server.controller.DecoratedAlternativeEffectGun;
import com.adrenalinici.adrenaline.server.controller.DecoratedEffect;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents the state of an AlternativeEffectGun.
 * It stores information about the chosen effect and provides the consequent phase configuration.
 */
public interface AlternativeEffectGunFlowState extends GunFlowState {
  DecoratedAlternativeEffectGun getChosenGun();

  DecoratedEffect getChosenEffect();

  AlternativeEffectGunFlowState setChosenEffect(DecoratedEffect chosenEffect, boolean chosenEffectIsFirstEffect);

  boolean isFirstEffect();

  @Override
  ObjectNode resolvePhaseConfiguration(String phaseId);
}
