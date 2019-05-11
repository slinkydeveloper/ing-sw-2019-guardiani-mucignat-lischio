package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.DecoratedAlternativeEffectGun;
import com.adrenalinici.adrenaline.controller.DecoratedEffect;
import com.adrenalinici.adrenaline.controller.GunLoader;
import com.adrenalinici.adrenaline.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static com.adrenalinici.adrenaline.util.JsonUtils.pointer;

public class AlternativeEffectGunFlowState extends GunFlowState {

  private Boolean firstEffect;
  private DecoratedEffect chosenEffect;

  public AlternativeEffectGunFlowState(DecoratedAlternativeEffectGun chosenGun) {
    super(chosenGun);
  }

  public DecoratedAlternativeEffectGun getChosenGun() {
    return (DecoratedAlternativeEffectGun) chosenGun;
  }

  public DecoratedEffect getChosenEffect() {
    return chosenEffect;
  }

  public AlternativeEffectGunFlowState setChosenEffect(DecoratedEffect chosenEffect, boolean chosenEffectIsFirstEffect) {
    this.chosenEffect = chosenEffect;
    this.firstEffect = chosenEffectIsFirstEffect;
    return this;
  }

  public boolean isFirstEffect() {
    return firstEffect;
  }

  @Override
  public ObjectNode resolvePhaseConfiguration(String phaseId) {
    JsonNode gunWideConfig = JsonUtils.getConfigurationJSONFromClasspath("guns/" + chosenGun.getId() + ".json")
      .at(pointer("phasesConfig", phaseId));

    JsonNode effectWideConfig = (firstEffect != null) ?
      (ObjectNode) JsonUtils.getConfigurationJSONFromClasspath("guns/" + getChosenGun().getId() + ".json")
        .at(
        pointer(firstEffect ? "firstEffect" : "secondEffect", "phasesConfig", phaseId)
        ) : null;

    return JsonUtils.mergeConfigs(
      gunWideConfig != null && gunWideConfig.isObject() ? (ObjectNode) gunWideConfig : null,
      effectWideConfig != null && effectWideConfig.isObject() ? (ObjectNode) effectWideConfig : null
    );
  }
}
