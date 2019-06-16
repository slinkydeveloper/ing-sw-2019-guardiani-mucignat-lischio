package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.server.JsonUtils;
import com.adrenalinici.adrenaline.server.controller.DecoratedAlternativeEffectGun;
import com.adrenalinici.adrenaline.server.controller.DecoratedEffect;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static com.adrenalinici.adrenaline.server.JsonUtils.pointer;

public class AlternativeEffectGunFlowStateImpl extends GunFlowStateImpl implements AlternativeEffectGunFlowState {

  private Boolean firstEffect;
  private DecoratedEffect chosenEffect;

  public AlternativeEffectGunFlowStateImpl(DecoratedAlternativeEffectGun chosenGun) {
    super(chosenGun);
  }

  @Override
  public DecoratedAlternativeEffectGun getChosenGun() {
    return (DecoratedAlternativeEffectGun) chosenGun;
  }

  @Override
  public DecoratedEffect getChosenEffect() {
    return chosenEffect;
  }

  @Override
  public AlternativeEffectGunFlowState setChosenEffect(DecoratedEffect chosenEffect, boolean chosenEffectIsFirstEffect) {
    this.chosenEffect = chosenEffect;
    this.firstEffect = chosenEffectIsFirstEffect;
    return this;
  }

  @Override
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
