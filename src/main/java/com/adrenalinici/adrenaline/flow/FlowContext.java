package com.adrenalinici.adrenaline.flow;

import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

public interface FlowContext {

  /**
   * Get associated orchestrator
   *
   * @return
   */
  FlowOrchestrator getOrchestrator();

  /**
   * Jump to stateId
   *
   * @param stateId
   * @param event
   */
  void jump(String stateId, GameView view, FlowState event);

  /**
   * Replay the actual node
   *
   * @param view
   */
  void replayNode(GameView view);

  /**
   * Replay from the actual phase
   *
   * @param view
   */
  void replayPhase(GameView view);

  /**
   * The context is built with a list of phases, where each phase is a stateless FlowNode. nextPhase() calls next phase
   */
  default void nextPhase(GameView view) {
    nextPhase(view, null);
  }

  /**
   * Jump to next phase
   *
   * @param view
   * @param flowState
   */
  void nextPhase(GameView view, FlowState flowState);

  /**
   * Get actual phase
   *
   * @return
   */
  String actualPhase();

  /**
   * Get actual flow node
   *
   * @return
   */
  FlowNode actualFlowNode();

  FlowNode actualNode();

  /**
   * Add phases respecting add order. e.g. <br/>
   * <pre>
   * context.addPhases("a", "b"); // phases queue: ["a", "b"]
   * context.addPhasesToEnd("e", "f"); // phases queue: ["a", "b", "e", "f"]
   * context.addPhases("c", "d"); // phases queue: ["a", "b", "c", "d", "e", "f"]
   * </pre>
   *
   * @param phases
   */
  void addPhases(String... phases);

  /**
   * Add phases to end of phases queue. See {@link FlowContext#addPhases(String...)} for more details
   *
   * @param phases
   */
  void addPhasesToEnd(String... phases);

  /**
   * End the current context, calling {@link FlowOrchestrator#onEnd(GameView)}
   *
   * @param view
   */
  void end(GameView view);

  /**
   * Handle view event
   *
   * @param event
   */
  void handleEvent(ViewEvent event, GameView view);

}
