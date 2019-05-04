package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.DecoratedBaseEffectGun;
import com.adrenalinici.adrenaline.controller.GunLoader;
import com.adrenalinici.adrenaline.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static com.adrenalinici.adrenaline.util.JsonUtils.pointer;

public class BaseEffectGunFlowState extends GunFlowState {

  private boolean activatedFirstExtraEffect = false;
  private boolean activatedSecondExtraEffect = false;

  public BaseEffectGunFlowState(DecoratedBaseEffectGun chosenGun) {
    super(chosenGun);
  }

  public DecoratedBaseEffectGun getChosenGun() {
    return (DecoratedBaseEffectGun) chosenGun;
  }

  public boolean isActivatedFirstExtraEffect() {
    return activatedFirstExtraEffect;
  }

  public BaseEffectGunFlowState setActivatedFirstExtraEffect(boolean activatedFirstExtraEffect) {
    this.activatedFirstExtraEffect = activatedFirstExtraEffect;
    return this;
  }

  public boolean isActivatedSecondExtraEffect() {
    return activatedSecondExtraEffect;
  }

  public BaseEffectGunFlowState setActivatedSecondExtraEffect(boolean activatedSecondExtraEffect) {
    this.activatedSecondExtraEffect = activatedSecondExtraEffect;
    return this;
  }

  /*@Override
  public ObjectNode resolvePhaseConfiguration(String phaseId) {
    JsonNode gunWideConfig = GunLoader.config.at(pointer(chosenGun.getId(), "phasesConfig", phaseId));
    JsonNode firstExtraEffectWideConfig = activatedFirstExtraEffect ?
      GunLoader.config.at(pointer(chosenGun.getId(), "firstExtraEffect", "phasesConfig", phaseId)) : null;
    JsonNode secondExtraEffectWideConfig = activatedSecondExtraEffect ?
      GunLoader.config.at(pointer(chosenGun.getId(), "secondExtraEffect", "phasesConfig", phaseId)) : null;
    return JsonUtils.mergeConfigs(
      gunWideConfig.isObject() ? (ObjectNode) gunWideConfig : null,
      JsonUtils.mergeConfigs(
        firstExtraEffectWideConfig != null && firstExtraEffectWideConfig.isObject() ? (ObjectNode) firstExtraEffectWideConfig : null,
        secondExtraEffectWideConfig != null && secondExtraEffectWideConfig.isObject() ? (ObjectNode) secondExtraEffectWideConfig : null
      )
    );
  }*/
  @Override
  public ObjectNode resolvePhaseConfiguration(String phaseId) {
    JsonNode gunWideConfig = JsonUtils.getConfigurationJSONFromClasspath(chosenGun.getId()).at(
      pointer("phasesConfig", phaseId)
    );

    JsonNode firstExtraEffectWideConfig = activatedFirstExtraEffect ?
      JsonUtils.getConfigurationJSONFromClasspath(chosenGun.getId()).at(
        pointer(chosenGun.getId(), "firstExtraEffect", "phasesConfig", phaseId)
      ) : null;
    JsonNode secondExtraEffectWideConfig = activatedSecondExtraEffect ?
      JsonUtils.getConfigurationJSONFromClasspath(chosenGun.getId()).at(
        pointer(chosenGun.getId(), "secondExtraEffect", "phasesConfig", phaseId)
      ) : null;
    return JsonUtils.mergeConfigs(
      gunWideConfig.isObject() ? (ObjectNode) gunWideConfig : null,
      JsonUtils.mergeConfigs(
        firstExtraEffectWideConfig != null && firstExtraEffectWideConfig.isObject() ? (ObjectNode) firstExtraEffectWideConfig : null,
        secondExtraEffectWideConfig != null && secondExtraEffectWideConfig.isObject() ? (ObjectNode) secondExtraEffectWideConfig : null
      )
    );
  }
}
