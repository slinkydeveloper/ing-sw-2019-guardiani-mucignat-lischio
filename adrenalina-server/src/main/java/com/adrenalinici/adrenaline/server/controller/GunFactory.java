package com.adrenalinici.adrenaline.server.controller;

import com.adrenalinici.adrenaline.common.model.Gun;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

/**
 * Factory interface for guns creation
 */
public interface GunFactory {

  /**
   * Called in resolveGunFactory in GunLoader. It is used in order to filter
   * the wanted factory among others
   * @param key
   * @param config
   * @return
   */
  boolean canConsume(String key, ObjectNode config);

  /**
   * Returns a Gun containing only basic information about effects and their cost
   *
   * @param key
   * @param config
   * @return
   */
  Gun getModelGun(String key, ObjectNode config);

  /**
   * Returns a DecoratedGun containing complete information about effects
   * and additional phases required from each effect
   *
   * @param key
   * @param config
   * @return
   */
  DecoratedGun getDecoratedGun(String key, ObjectNode config);

  /**
   * Returns a list of flow nodes that represents different effects applier
   * for each gun
   *
   * @param key
   * @param config
   * @return
   */
  List<ControllerFlowNode> getAdditionalNodes(String key, ObjectNode config);

}
