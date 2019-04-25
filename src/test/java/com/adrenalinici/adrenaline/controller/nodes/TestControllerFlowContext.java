package com.adrenalinici.adrenaline.controller.nodes;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.GunLoader;
import com.adrenalinici.adrenaline.flow.FlowOrchestrator;
import com.adrenalinici.adrenaline.flow.FlowState;
import com.adrenalinici.adrenaline.view.GameView;

import java.util.List;

public class TestControllerFlowContext extends ControllerFlowContext {

  List<String> loadedPhases;

  public TestControllerFlowContext(List<String> loadedPhases, FlowOrchestrator orchestrator, List<String> initialPhases, GunLoader gunLoader) {
    super(orchestrator, initialPhases, gunLoader);
    this.loadedPhases = loadedPhases;
  }

  @Override
  public void nextPhase(GameView view, FlowState flowState) {
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
