package com.adrenalinici.adrenaline.flow;

import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

public interface FlowNode<S extends FlowState, C extends FlowContext> {

  String id();

  @SuppressWarnings("unchecked")
  default S mapState(FlowState oldState) {
    return (S) oldState;
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
