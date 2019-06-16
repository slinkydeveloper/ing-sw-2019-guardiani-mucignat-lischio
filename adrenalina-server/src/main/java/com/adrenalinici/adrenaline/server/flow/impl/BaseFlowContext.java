package com.adrenalinici.adrenaline.server.flow.impl;

import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.flow.FlowContext;
import com.adrenalinici.adrenaline.server.flow.FlowNode;
import com.adrenalinici.adrenaline.server.flow.FlowOrchestrator;
import com.adrenalinici.adrenaline.server.flow.FlowState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class BaseFlowContext implements FlowContext {

  private FlowNode actualNode;
  protected FlowState actualState;

  private String actualPhaseId;
  private List<String> phasesQueue;

  private FlowOrchestrator orchestrator;

  private final List<String> additionalNodesToExecuteAlways;

  private int actualIndex;

  public BaseFlowContext(FlowOrchestrator orchestrator, List<String> initialPhases) {
    this(orchestrator, initialPhases, Collections.emptyList());
  }

  public BaseFlowContext(FlowOrchestrator orchestrator, List<String> initialPhases, List<String> additionalNodesToExecuteAlways) {
    this.orchestrator = orchestrator;
    this.phasesQueue = initialPhases != null ? new ArrayList<>(initialPhases) : new ArrayList<>();
    this.actualIndex = this.phasesQueue.size();
    this.additionalNodesToExecuteAlways = additionalNodesToExecuteAlways;
  }

  @Override
  public FlowOrchestrator getOrchestrator() {
    return orchestrator;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void jump(String stateId, GameView view, FlowState state) {
    actualNode = orchestrator.resolveNode(stateId);
    FlowState newState = actualNode.mapState(state);
    if (actualNode.skip(newState, this)) this.nextPhase(view, state);
    else {
      actualState = newState;
      actualNode.onJump(actualState, view, getOrchestrator().getModel(), this);
    }
  }

  @Override
  public void replayNode(GameView view) {
    jump(actualNode.id(), view, actualState);
  }

  @Override
  public void nextPhase(GameView view, FlowState flowState) {
    if (phasesQueue.isEmpty()) {
      end(view);
    } else {
      actualPhaseId = phasesQueue.remove(0);
      if (actualIndex > 0) actualIndex--;
      jump(actualPhaseId, view, flowState);
    }
  }

  @Override
  public String actualPhase() {
    return this.actualPhaseId;
  }

  @Override
  public FlowNode actualFlowNode() {
    return this.orchestrator.resolveNode(this.actualPhaseId);
  }

  @Override
  public FlowNode actualNode() {
    return this.actualNode;
  }

  @Override
  public void replayPhase(GameView view) {
    jump(this.actualPhaseId, view, null);
  }

  @Override
  public void addPhases(String... phases) {
    this.phasesQueue.addAll(actualIndex, Arrays.asList(phases));
    actualIndex = actualIndex + phases.length;
  }

  @Override
  public void addPhasesToEnd(String... phases) {
    this.phasesQueue.addAll(Arrays.asList(phases));
  }

  @Override
  public void end(GameView view) {
    getOrchestrator().onEnd(view);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void handleEvent(ViewEvent event, GameView view) {
    if (this.actualNode() == null) {
      this.nextPhase(view);
      this.actualNode.handleEvent(event, actualState, view, getOrchestrator().getModel(), this);
    } else {
      this.actualNode.handleEvent(event, actualState, view, getOrchestrator().getModel(), this);
    }
    if (additionalNodesToExecuteAlways != null) {
      this.additionalNodesToExecuteAlways.forEach(n ->
        this.getOrchestrator().resolveNode(n).handleEvent(event, null, view, getOrchestrator().getModel(), this)
      );
    }
  }

  public List<String> getPhasesQueue() {
    return phasesQueue;
  }
}
