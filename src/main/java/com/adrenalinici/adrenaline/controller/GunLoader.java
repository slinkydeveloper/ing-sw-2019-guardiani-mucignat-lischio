package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.Gun;
import com.adrenalinici.adrenaline.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;
import java.util.stream.Collectors;

public class GunLoader {

  private List<GunFactory> factories;

  private static List<Map.Entry<String, JsonNode>> configs = new ArrayList<>();
  private List<Map.Entry<String, Gun>> guns;
  private List<Map.Entry<String, DecoratedGun>> decoratedGuns;

  public GunLoader(List<GunFactory> factories) {
    this.factories = factories;
    guns = new ArrayList<>();
    decoratedGuns = new ArrayList<>();
  }

  public Gun getModelGun(String id) {
    List<Map.Entry<String, Gun>> cachedGuns = new ArrayList<>();
    if (!guns.isEmpty()) {
      cachedGuns = guns.stream()
        .filter(stringGunEntry -> stringGunEntry.getKey().equals(id))
        .collect(Collectors.toList());
    }
    if (!cachedGuns.isEmpty()) return cachedGuns.get(0).getValue();

    Gun gunToAdd = resolveGunFactory(id).getModelGun(id, (ObjectNode) getGunConfigJson(id));
    guns.add(new AbstractMap.SimpleImmutableEntry<>(
      id,
      gunToAdd
    ));
    return gunToAdd;
  }
  //public Gun getModelGun(String id) { return resolveGunFactory(id).getModelGun(id, (ObjectNode) getGunConfigJson(id));}

  public DecoratedGun getDecoratedGun(String id) {
    List<Map.Entry<String, DecoratedGun>> cachedDecoratedGuns = new ArrayList<>();
    if (!decoratedGuns.isEmpty()) {
      cachedDecoratedGuns = decoratedGuns.stream()
        .filter(stringDecoratedGunEntry -> stringDecoratedGunEntry.getKey().equals(id))
        .collect(Collectors.toList());
    }
    if (!cachedDecoratedGuns.isEmpty()) return cachedDecoratedGuns.get(0).getValue();

    DecoratedGun decoratedGunToAdd = resolveGunFactory(id).getDecoratedGun(id, (ObjectNode) getGunConfigJson(id));
    decoratedGuns.add(new AbstractMap.SimpleImmutableEntry<>(
      id,
      decoratedGunToAdd
    ));
    return decoratedGunToAdd;
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


  /**
   * This method loads and returns the gun configuration taken from json config file.
   * It checks if the configuration of the gun is already stored in the configs
   * cache and return it if present, in order to avoid multiple creation of the config.
   *
   * @param id of wanted gun
   * @return Json configuration of the gun
   */
  private static JsonNode getGunConfigJson(String id) {
    List<Map.Entry<String, JsonNode>> cachedConfigs = new ArrayList<>();
    if (!configs.isEmpty()) {
      cachedConfigs = configs.stream()
        .filter(stringJsonNodeEntry -> stringJsonNodeEntry.getKey().equals(id))
        .collect(Collectors.toList());
    }
    if (!cachedConfigs.isEmpty()) return cachedConfigs.get(0).getValue();

    JsonNode nodeToAdd = JsonUtils.getConfigurationJSONFromClasspath(id + ".json");
    configs.add(new AbstractMap.SimpleImmutableEntry<>(
        id,
        nodeToAdd
      )
    );
    return nodeToAdd;
  }

  public static List<Map.Entry<String, JsonNode>> getConfigs() {
    return configs;
  }

  public List<Map.Entry<String, Gun>> getGuns() {
    return guns;
  }

  public List<Map.Entry<String, DecoratedGun>> getDecoratedGuns() {
    return decoratedGuns;
  }
}
