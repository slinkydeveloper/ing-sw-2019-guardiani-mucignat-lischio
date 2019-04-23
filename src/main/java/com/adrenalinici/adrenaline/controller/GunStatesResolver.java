package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.Gun;
import com.adrenalinici.adrenaline.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public class GunStatesResolver {

  public static JsonNode config = JsonUtils.getConfigurationJSONFromClasspath("guns.json");

  private List<GunStatesFactory> factories;

  public GunStatesResolver(List<GunStatesFactory> factories) {
    this.factories = factories;
  }

  @SuppressWarnings("unchecked")
  public ControllerState resolveGunState(Gun g) {
    return factories
      .stream()
      .filter(f -> f.canConsume(g.getId(), config.get(g.getId())))
      .findFirst()
      .orElseThrow(() -> new IllegalStateException("Cannot find in config gun " + g.getId()))
      .create(g, config.get(g.getId()));
  }

}
