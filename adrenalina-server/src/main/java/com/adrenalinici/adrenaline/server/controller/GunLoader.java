package com.adrenalinici.adrenaline.server.controller;

import com.adrenalinici.adrenaline.common.model.Gun;
import com.adrenalinici.adrenaline.common.util.LogUtils;
import com.adrenalinici.adrenaline.common.util.StreamUtils;
import com.adrenalinici.adrenaline.server.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GunLoader {

  private static final Logger LOG = LogUtils.getLogger(GunLoader.class);

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
    LOG.info("Created new model gun " + id);
    return guns.get(id);
  }

  public DecoratedGun getDecoratedGun(String id) {
    if (decoratedGuns.containsKey(id)) return decoratedGuns.get(id);
    decoratedGuns.put(id, resolveGunFactory(id).getDecoratedGun(id, (ObjectNode) getGunConfigJson(id)));
    LOG.info("Created new decorated gun " + id);
    return decoratedGuns.get(id);
  }

  public List<ControllerFlowNode> getAdditionalNodes(String id) {
    if (nodes.containsKey(id)) return nodes.get(id);
    nodes.put(id, resolveGunFactory(id).getAdditionalNodes(id, (ObjectNode) getGunConfigJson(id)));
    LOG.info("Discovered additional nodes for gun " + id);
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
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      List<String> guns = StreamUtils.enumerationStream(loader.getResources("guns"))
          .flatMap(GunLoader::getResourceListingForResourceURL)
          .map(s -> s.replace(".json", ""))
          .collect(Collectors.toList());
      LOG.info("Discovered guns: " + guns.toString());
      return guns;
    } catch (IOException e) {
      return null;
    }
  }

  private static Stream<String> getResourceListingForResourceURL(URL dirURL) {
    try {
      if (dirURL != null && dirURL.getProtocol().equals("file")) {
        /* A file path: easy enough */
        return Stream
          .of(new File(dirURL.toURI()).listFiles())
          .map(File::getName);
      }

      if (dirURL.getProtocol().equals("jar")) {
        /* A JAR path */
        String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
        String innerPath = dirURL.getPath().substring(dirURL.getPath().indexOf("!") + 2) + "/";

        JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
        return StreamUtils
          .enumerationStream(jar.entries())
          .map(JarEntry::getName)
          .filter(name -> name.startsWith(innerPath) && !name.equals(innerPath))
          .map(name -> name.substring(innerPath.length()));
      }
    } catch (Exception e) {}
    throw new UnsupportedOperationException("Cannot list files for URL " + dirURL);
  }
}
