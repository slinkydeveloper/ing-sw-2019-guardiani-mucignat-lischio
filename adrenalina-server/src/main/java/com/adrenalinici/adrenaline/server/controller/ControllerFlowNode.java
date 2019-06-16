package com.adrenalinici.adrenaline.server.controller;

import com.adrenalinici.adrenaline.server.flow.FlowNode;
import com.adrenalinici.adrenaline.server.flow.FlowState;

public interface ControllerFlowNode<T extends FlowState> extends FlowNode<T, ControllerFlowContext> {
}
