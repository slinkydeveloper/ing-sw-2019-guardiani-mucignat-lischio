package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.server.controller.DecoratedBaseEffectGun;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface BaseEffectGunFlowState extends GunFlowState {
  DecoratedBaseEffectGun getChosenGun();

  boolean isActivatedFirstExtraEffect();

  BaseEffectGunFlowState setActivatedFirstExtraEffect(boolean activatedFirstExtraEffect);

  boolean isActivatedSecondExtraEffect();

  BaseEffectGunFlowState setActivatedSecondExtraEffect(boolean activatedSecondExtraEffect);

  @Override
  ObjectNode resolvePhaseConfiguration(String phaseId);
}
