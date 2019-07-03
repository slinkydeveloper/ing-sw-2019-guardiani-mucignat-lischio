package com.adrenalinici.adrenaline.server.flow;

import com.adrenalinici.adrenaline.server.flow.impl.VoidState;

/**
 * This interface represents a flow node that doesn't need to carry any
 * flow state inside, in order to complete its task
 *
 * @param <C>
 */
public interface StatelessFlowNode<C extends FlowContext> extends FlowNode<VoidState, C> {

  @Override
  default VoidState mapState(FlowState oldState) {
    return null;
  }
}
