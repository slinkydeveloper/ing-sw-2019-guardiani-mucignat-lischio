package com.adrenalinici.adrenaline.server.flow;

import com.adrenalinici.adrenaline.server.flow.impl.VoidState;

public interface StatelessFlowNode<C extends FlowContext> extends FlowNode<VoidState, C> {

  @Override
  default VoidState mapState(FlowState oldState) {
    return null;
  }
}
