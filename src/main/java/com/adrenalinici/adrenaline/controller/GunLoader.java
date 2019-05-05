package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.Gun;
import com.adrenalinici.adrenaline.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public class GunLoader {

  private List<GunFactory> factories;

  public GunLoader(List<GunFactory> factories) {
    this.factories = factories;
  }

  public Gun getModelGun(String id) {
    return resolveGunFactory(id).getModelGun(id, (ObjectNode) getGunConfigJson(id));
  }

  public DecoratedGun getDecoratedGun(String id) {
    return resolveGunFactory(id).getDecoratedGun(id, (ObjectNode) getGunConfigJson(id));
  }

  public List<ControllerFlowNode> getAdditionalNodes(String id) {
    return resolveGunFactory(id).getAdditionalNodes(id, (ObjectNode) getGunConfigJson(id));
  }

  private GunFactory resolveGunFactory(String id) {
    return factories
      .stream()
      .filter(f -> f.canConsume(id, (ObjectNode) getGunConfigJson(id)))
      .findFirst()
      .orElseThrow(() -> new IllegalStateException("Cannot find in config gun " + id));
  }

  public static JsonNode getGunConfigJson(String id) {
    return JsonUtils.getConfigurationJSONFromClasspath(id + ".json");
  }

}
