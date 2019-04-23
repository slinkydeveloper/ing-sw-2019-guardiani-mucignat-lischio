package com.adrenalinici.adrenaline.flow;

import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

public interface FlowContext {

  FlowOrchestrator getOrchestrator();

  /**
   * Jump
   *
   * @param stateId
   * @param event
   */
  void jump(String stateId, GameView view, FlowState event);

  void replayState(GameView view);

  void replayPhase(GameView view);

  /**
   * The context is built with a list of phases, where each phase is a stateless FlowNode. nextPhase() calls next phase
   */
  void nextPhase(GameView view);

  String actualPhase();

  FlowNode actualPhaseState();

  FlowNode actualState();

  void addPhasesToHead(String... phases);

  void addPhasesToEnd(String... phases);

  void end(GameView view);

  void handleEvent(ViewEvent event);

}
