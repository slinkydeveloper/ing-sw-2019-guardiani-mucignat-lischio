package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.server.controller.DecoratedBaseEffectGun;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents the state of a BaseEffectGun.
 * It stores information about the chosen effect and provides the consequent phase configuration.
 */
public interface BaseEffectGunFlowState extends GunFlowState {
  DecoratedBaseEffectGun getChosenGun();

  boolean isActivatedFirstExtraEffect();

  BaseEffectGunFlowState setActivatedFirstExtraEffect(boolean activatedFirstExtraEffect);

  boolean isActivatedSecondExtraEffect();

  BaseEffectGunFlowState setActivatedSecondExtraEffect(boolean activatedSecondExtraEffect);

  @Override
  ObjectNode resolvePhaseConfiguration(String phaseId);
}
