package com.adrenalinici.adrenaline.server.flow.impl;

import com.adrenalinici.adrenaline.server.flow.FlowOrchestrator;

import java.util.List;

public class FlowContextImpl extends BaseFlowContext {

  public FlowContextImpl(FlowOrchestrator orchestrator, List<String> initialPhases) {
    super(orchestrator, initialPhases);
  }
}
