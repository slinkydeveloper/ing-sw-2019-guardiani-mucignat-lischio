package com.adrenalinici.adrenaline.server.controller;

import com.adrenalinici.adrenaline.common.model.AmmoColor;
import com.adrenalinici.adrenaline.common.model.Effect;

import java.util.Collections;
import java.util.List;

public class DecoratedEffect {

  private Effect effect;
  private List<String> additionalPhases;
  private List<AmmoColor> requiredAmmos;

  public DecoratedEffect(Effect effect, List<String> additionalPhases, List<AmmoColor> requiredAmmos) {
    this.effect = effect;
    this.additionalPhases = additionalPhases == null ? Collections.emptyList() : additionalPhases;
    this.requiredAmmos = requiredAmmos == null ? Collections.emptyList() : requiredAmmos;
  }

  public Effect get() {
    return effect;
  }

  public String getId() {
    return effect.getId();
  }

  public String getName() {
    return effect.getName();
  }

  public String getDescription() {
    return effect.getDescription();
  }

  public List<AmmoColor> getRequiredAmmos() {
    return requiredAmmos;
  }

  public List<String> getAdditionalPhases() {
    return additionalPhases;
  }
}
