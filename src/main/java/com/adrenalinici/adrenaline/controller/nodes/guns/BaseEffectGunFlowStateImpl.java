package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.DecoratedBaseEffectGun;
import com.adrenalinici.adrenaline.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static com.adrenalinici.adrenaline.util.JsonUtils.pointer;

public class BaseEffectGunFlowStateImpl extends GunFlowStateImpl implements BaseEffectGunFlowState {

  private boolean activatedFirstExtraEffect = false;
  private boolean activatedSecondExtraEffect = false;

  public BaseEffectGunFlowStateImpl(DecoratedBaseEffectGun chosenGun) {
    super(chosenGun);
  }

  @Override
  public DecoratedBaseEffectGun getChosenGun() {
    return (DecoratedBaseEffectGun) chosenGun;
  }

  @Override
  public boolean isActivatedFirstExtraEffect() {
    return activatedFirstExtraEffect;
  }

  @Override
  public BaseEffectGunFlowState setActivatedFirstExtraEffect(boolean activatedFirstExtraEffect) {
    this.activatedFirstExtraEffect = activatedFirstExtraEffect;
    return this;
  }

  @Override
  public boolean isActivatedSecondExtraEffect() {
    return activatedSecondExtraEffect;
  }

  @Override
  public BaseEffectGunFlowState setActivatedSecondExtraEffect(boolean activatedSecondExtraEffect) {
    this.activatedSecondExtraEffect = activatedSecondExtraEffect;
    return this;
  }

  @Override
  public ObjectNode resolvePhaseConfiguration(String phaseId) {
    JsonNode gunWideConfig = JsonUtils.getConfigurationJSONFromClasspath("guns/" + chosenGun.getId() + ".json")
      .at(pointer("phasesConfig", phaseId));

    JsonNode firstExtraEffectWideConfig = activatedFirstExtraEffect ?
      JsonUtils.getConfigurationJSONFromClasspath("guns/" + chosenGun.getId() + ".json").at(
        pointer("firstExtraEffect", "phasesConfig", phaseId)
      ) : null;
    JsonNode secondExtraEffectWideConfig = activatedSecondExtraEffect ?
      JsonUtils.getConfigurationJSONFromClasspath("guns/" + chosenGun.getId() + ".json").at(
        pointer("secondExtraEffect", "phasesConfig", phaseId)
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
