package com.adrenalinici.adrenaline.testutil;

import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.controller.guns.BaseEffectGunFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collections;
import java.util.List;

public class BaseEffectGunRifleFactory extends BaseEffectGunFactory {
  public BaseEffectGunRifleFactory() {
    super();
  }

  @Override
  public boolean canConsume(String key, ObjectNode config) {
    return "test_rifle".equals(key);
  }

  @Override
  public List<ControllerFlowNode> getAdditionalNodes(String key, ObjectNode config) {
    return Collections.emptyList();
  }
}