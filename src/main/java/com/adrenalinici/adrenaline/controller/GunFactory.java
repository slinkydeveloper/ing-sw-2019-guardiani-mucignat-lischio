package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.Gun;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public interface GunFactory {

  boolean canConsume(String key, ObjectNode config);

  Gun getModelGun(String key, ObjectNode config);

  DecoratedGun getDecoratedGun(String key, ObjectNode config);

  List<ControllerFlowNode> getAdditionalNodes(String key, ObjectNode config);

}
