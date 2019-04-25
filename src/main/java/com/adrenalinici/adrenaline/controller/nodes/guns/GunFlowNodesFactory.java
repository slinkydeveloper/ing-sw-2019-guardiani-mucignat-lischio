package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.model.Gun;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface GunFlowNodesFactory<G extends Gun> {

  boolean canConsume(String key, JsonNode config);

  List<ControllerFlowNode> create(G gun, JsonNode gunConfig);

}
