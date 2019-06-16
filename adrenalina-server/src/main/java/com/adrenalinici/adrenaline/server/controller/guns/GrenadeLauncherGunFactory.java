package com.adrenalinici.adrenaline.server.controller.guns;

import com.adrenalinici.adrenaline.server.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.ApplyGrenadeLauncherEffectFlowNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collections;
import java.util.List;

public class GrenadeLauncherGunFactory extends BaseEffectGunFactory {
  @Override
  public boolean canConsume(String key, ObjectNode config) {
    return "grenade_launcher".equals(key);
  }

  @Override
  public List<ControllerFlowNode> getAdditionalNodes(String key, ObjectNode config) {
    return Collections.singletonList(new ApplyGrenadeLauncherEffectFlowNode());
  }
}
