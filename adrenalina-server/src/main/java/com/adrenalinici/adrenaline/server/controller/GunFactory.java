package com.adrenalinici.adrenaline.server.controller;

import com.adrenalinici.adrenaline.common.model.Gun;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public interface GunFactory {

  boolean canConsume(String key, ObjectNode config);

  Gun getModelGun(String key, ObjectNode config);

  DecoratedGun getDecoratedGun(String key, ObjectNode config);

  List<ControllerFlowNode> getAdditionalNodes(String key, ObjectNode config);

}
