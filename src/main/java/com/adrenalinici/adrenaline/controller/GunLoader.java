package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.common.Gun;
import com.adrenalinici.adrenaline.util.JsonUtils;
import com.adrenalinici.adrenaline.util.StreamUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

public class GunLoader {

  public static final GunLoader INSTANCE = new GunLoader();
  private List<GunFactory> factories;

  private static Map<String, JsonNode> configs = new HashMap<>();
  private Map<String, Gun> guns;
  private Map<String, DecoratedGun> decoratedGuns;
  private Map<String, List<ControllerFlowNode>> nodes;

  private GunLoader() {
    factories = new ArrayList<>();
    guns = new HashMap<>();
    decoratedGuns = new HashMap<>();
    nodes = new HashMap<>();
    ServiceLoader<GunFactory> loader = ServiceLoader
      .load(
        GunFactory.class,
        GunFactory.class.getClassLoader()
      );
    for (GunFactory currLoader : loader) {
      factories.add(currLoader);
    }
  }

  public Gun getModelGun(String id) {
    if (guns.containsKey(id)) return guns.get(id);
    guns.put(id, resolveGunFactory(id).getModelGun(id, (ObjectNode) getGunConfigJson(id)));
    return guns.get(id);
  }

  public DecoratedGun getDecoratedGun(String id) {
    if (decoratedGuns.containsKey(id)) return decoratedGuns.get(id);
    decoratedGuns.put(id, resolveGunFactory(id).getDecoratedGun(id, (ObjectNode) getGunConfigJson(id)));
    return decoratedGuns.get(id);
  }

  public List<ControllerFlowNode> getAdditionalNodes(String id) {
    if (nodes.containsKey(id)) return nodes.get(id);
    nodes.put(id, resolveGunFactory(id).getAdditionalNodes(id, (ObjectNode) getGunConfigJson(id)));
    return nodes.get(id);
  }

  public List<ControllerFlowNode> getAllAdditionalNodes() {
    return getAvailableGuns()
      .stream()
      .flatMap(id -> this.getAdditionalNodes(id).stream())
      .collect(Collectors.toList());
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
    if (configs.containsKey(id)) return configs.get(id);
    configs.put(id, JsonUtils.getConfigurationJSONFromClasspath("guns/" + id + ".json"));
    return configs.get(id);
  }

  protected static Map<String, JsonNode> getConfigs() {
    return configs;
  }

  protected Map<String, Gun> getGuns() {
    return guns;
  }

  protected Map<String, DecoratedGun> getDecoratedGuns() {
    return decoratedGuns;
  }

  protected Map<String, List<ControllerFlowNode>> getNodes() {
    return nodes;
  }

  /**
   * Resolve from guns directory all available guns
   *
   * @return
   */
  public static List<String> getAvailableGuns() {
    try {
      return
        StreamUtils.enumerationStream(ClassLoader.getSystemResources("guns"))
          .map(path -> {
            try {
              return path.toURI();
            } catch (URISyntaxException e) {
              return null; // Shame on you Java for this API
            }
          })
          .flatMap(path -> Arrays.stream(new File(path).listFiles()))
          .map(File::getName)
          .map(s -> s.replace(".json", ""))
          .collect(Collectors.toList());
    } catch (IOException e) {
      return null;
    }
  }
}
