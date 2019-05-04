package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.Gun;
import com.adrenalinici.adrenaline.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.Map;

public class GunLoader {

  //public static JsonNode config = JsonUtils.getConfigurationJSONFromClasspath("guns.json");
  //public List<JsonNode> config;
  //public List<Map<String, JsonNode>> config; //mappa dove la stringa sarebbe l'id dell'arma

  private List<GunFactory> factories;

  public GunLoader(List<GunFactory> factories) {
    this.factories = factories;
  }
  /*public GunLoader(List<GunFactory> factories, String id) {
    this.factories = factories;
  }*/


  /*public Gun getModelGun(String id) {
    return resolveGunFactory(id).getModelGun(id, (ObjectNode) config.get(id));
  }*/
  public Gun getModelGun(String id) {
    return resolveGunFactory(id).getModelGun(id, (ObjectNode) JsonUtils.getConfigurationJSONFromClasspath(id + ".json"));
  }

  /*public DecoratedGun getDecoratedGun(String id) {
    return resolveGunFactory(id).getDecoratedGun(id, (ObjectNode) config.get(id));
  }*/
  public DecoratedGun getDecoratedGun(String id) {
    return resolveGunFactory(id).getDecoratedGun(id, (ObjectNode) JsonUtils.getConfigurationJSONFromClasspath(id + ".json"));
  }

  /*public List<ControllerFlowNode> getAdditionalNodes(String id) {
    return resolveGunFactory(id).getAdditionalNodes(id, (ObjectNode) config.get(id));
  }*/
  public List<ControllerFlowNode> getAdditionalNodes(String id) {
    return resolveGunFactory(id).getAdditionalNodes(id, (ObjectNode) JsonUtils.getConfigurationJSONFromClasspath(id + ".json"));
  }

  /*private GunFactory resolveGunFactory(String id) {
    return factories
      .stream()
      .filter(f -> f.canConsume(id, (ObjectNode) config.get(id)))
      .findFirst()
      .orElseThrow(() -> new IllegalStateException("Cannot find in config gun " + id));
  }*/
  private GunFactory resolveGunFactory(String id) {
    return factories
      .stream()
      .filter(f -> f.canConsume(id, (ObjectNode) JsonUtils.getConfigurationJSONFromClasspath(id)))
      .findFirst()
      .orElseThrow(() -> new IllegalStateException("Cannot find in config gun " + id));
  }

}
