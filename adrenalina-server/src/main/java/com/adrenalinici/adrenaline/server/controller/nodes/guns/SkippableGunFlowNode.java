package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.server.JsonUtils;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowNode;

public interface SkippableGunFlowNode<T extends GunFlowState> extends ControllerFlowNode<T> {

  @Override
  default boolean skip(T oldState, ControllerFlowContext context) {
    return !JsonUtils.resolveEnabledPredicate(id(), oldState).test(oldState);
  }

}
