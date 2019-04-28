package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.flow.FlowState;

public interface ControllerFlowNode<T extends FlowState> extends FlowNode<T, ControllerFlowContext> {
}
