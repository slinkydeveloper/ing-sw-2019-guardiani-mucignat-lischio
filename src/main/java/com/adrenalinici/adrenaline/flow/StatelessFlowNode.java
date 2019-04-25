package com.adrenalinici.adrenaline.flow;

import com.adrenalinici.adrenaline.flow.impl.VoidState;

public interface StatelessFlowNode<C extends FlowContext> extends FlowNode<VoidState, C> {

  @Override
  default VoidState mapState(FlowState oldState) {
    return null;
  }
}
