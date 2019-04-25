package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.Gun;
import com.adrenalinici.adrenaline.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public class GunLoader {

  public static JsonNode config = JsonUtils.getConfigurationJSONFromClasspath("guns.json");

  private List<GunFactory> factories;

  public GunLoader(List<GunFactory> factories) {
    this.factories = factories;
  }

  public Gun getModelGun(String id) {
    return resolveGunFactory(id).getModelGun(id, (ObjectNode) config.get(id));
  }

  public DecoratedGun getDecoratedGun(String id) {
    return resolveGunFactory(id).getDecoratedGun(id, (ObjectNode) config.get(id));
  }

  public List<ControllerFlowNode> getAdditionalNodes(String id) {
    return resolveGunFactory(id).getAdditionalNodes(id, (ObjectNode) config.get(id));
  }

  private GunFactory resolveGunFactory(String id) {
    return factories
      .stream()
      .filter(f -> f.canConsume(id, (ObjectNode) config.get(id)))
      .findFirst()
      .orElseThrow(() -> new IllegalStateException("Cannot find in config gun " + id));
  }



}
