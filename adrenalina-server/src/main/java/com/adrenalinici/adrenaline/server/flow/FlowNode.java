package com.adrenalinici.adrenaline.server.flow;

import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.model.GameModel;

/**
 * This interface represents a generic node of the state machine which
 * actually operate as the whole game controller.
 *
 * @param <S>
 * @param <C>
 */
public interface FlowNode<S extends FlowState, C extends FlowContext> {

  String id();

  @SuppressWarnings("unchecked")
  default S mapState(FlowState oldState) {
    return (S) oldState;
  }

  /**
   * skip() called when the context jump to the next flow node.
   * It determines whether the next node can be skipped or not.
   *
   * @param newState
   * @param context
   * @return
   */
  default boolean skip(S newState, C context) {
    return false;
  }

  /**
   * onJump() called after flow.FlowNode called the first time
   *
   * @param flowState
   * @param view
   * @param model
   * @param context
   */
  void onJump(S flowState, GameView view, GameModel model, C context);

  /**
   * handleEvent() called every time a new event is received, till no
   *
   * @param event
   * @param flowState
   * @param view
   * @param model
   * @param context
   */
  void handleEvent(ViewEvent event, S flowState, GameView view, GameModel model, C context);
}
