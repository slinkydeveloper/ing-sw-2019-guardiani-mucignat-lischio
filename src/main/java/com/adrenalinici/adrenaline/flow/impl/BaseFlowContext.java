package com.adrenalinici.adrenaline.flow.impl;

import com.adrenalinici.adrenaline.flow.FlowContext;
import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.flow.FlowOrchestrator;
import com.adrenalinici.adrenaline.flow.FlowState;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class BaseFlowContext implements FlowContext {

  private FlowNode actualNode;
  private FlowState actualState;

  private String actualPhaseId;
  private List<String> phasesQueue;

  private FlowOrchestrator orchestrator;

  public BaseFlowContext(FlowOrchestrator orchestrator) {
    this(orchestrator, Collections.emptyList());
  }

  public BaseFlowContext(FlowOrchestrator orchestrator, List<String> initialPhases) {
    this.orchestrator = orchestrator;
    this.phasesQueue = initialPhases != null ? new ArrayList<>(initialPhases) : new ArrayList<>();
  }

  @Override
  public FlowOrchestrator getOrchestrator() {
    return orchestrator;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void jump(String stateId, GameView view, FlowState state) {
    FlowNode s = orchestrator.resolveState(stateId);
    actualNode = s;
    actualState = state;
    s.onJump(state, view, getOrchestrator().getModel(), this);
  }

  @Override
  public void replayState(GameView view) {
    jump(actualNode.id(), view, actualState);
  }

  @Override
  public void nextPhase(GameView view) {
    if (phasesQueue.isEmpty()) {
      end(view);
    } else {
      actualPhaseId = phasesQueue.remove(0);
      jump(actualPhaseId, view, null);
    }
  }

  @Override
  public String actualPhase() {
    return this.actualPhaseId;
  }

  @Override
  public FlowNode actualPhaseState() {
    return this.orchestrator.resolveState(this.actualPhaseId);
  }

  @Override
  public FlowNode actualState() {
    return this.actualNode;
  }

  @Override
  public void replayPhase(GameView view) {
    jump(this.actualPhaseId, view, null);
  }

  @Override
  public void addPhasesToHead(String... phases) {
    this.phasesQueue.addAll(0, Arrays.asList(phases));
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
  public void handleEvent(ViewEvent event) {
    if (this.actualState() == null) {
      this.nextPhase(event.getView());
      this.actualNode.handleEvent(event, actualState, event.getView(), getOrchestrator().getModel(), this);
    } else {
      this.actualNode.handleEvent(event, actualState, event.getView(), getOrchestrator().getModel(), this);
    }
  }

  public List<String> getPhasesQueue() {
    return phasesQueue;
  }
}
