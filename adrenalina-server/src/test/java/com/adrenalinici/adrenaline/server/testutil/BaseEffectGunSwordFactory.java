package com.adrenalinici.adrenaline.server.testutil;

import com.adrenalinici.adrenaline.server.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.server.controller.guns.BaseEffectGunFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collections;
import java.util.List;

public class BaseEffectGunSwordFactory extends BaseEffectGunFactory {
  public BaseEffectGunSwordFactory() {
    super();
  }

  @Override
  public boolean canConsume(String key, ObjectNode config) {
    return "test_sword".equals(key);
  }

  @Override
  public List<ControllerFlowNode> getAdditionalNodes(String key, ObjectNode config) {
    return Collections.emptyList();
  }
}
