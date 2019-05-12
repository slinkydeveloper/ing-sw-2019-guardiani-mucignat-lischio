package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.flow.FlowState;
import com.adrenalinici.adrenaline.util.JsonUtils;

import java.util.function.Predicate;

public interface SkippableGunFlowNode<T extends GunFlowState> extends ControllerFlowNode<T> {

  @Override
  default boolean skip(T oldState, ControllerFlowContext context) {
    return !JsonUtils.resolveEnabledPredicate(id(), oldState).test(oldState);
  }

}
