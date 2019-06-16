package com.adrenalinici.adrenaline.server.controller.nodes;

import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.flow.FlowOrchestrator;
import com.adrenalinici.adrenaline.server.flow.FlowState;

import java.util.List;

public class TestControllerFlowContext extends ControllerFlowContext {

  List<String> loadedPhases;

  public TestControllerFlowContext(List<String> loadedPhases, FlowOrchestrator orchestrator, List<String> initialPhases) {
    super(orchestrator, initialPhases);
    this.loadedPhases = loadedPhases;
  }

  @Override
  public void nextPhase(GameView view, FlowState flowState) {
    this.setActualState(flowState);
    if (!getPhasesQueue().isEmpty() && loadedPhases.contains(getPhasesQueue().get(0)))
      super.nextPhase(view, flowState);
    else
      super.end(view);
  }

  public FlowState getActualState() {
    return this.actualState;
  }

  public void setActualState(FlowState actualState) {
    this.actualState = actualState;
  }
}
