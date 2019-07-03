package com.adrenalinici.adrenaline.server.flow;

import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.model.GameModel;

/**
 * This interface helps the start of a new flow and also the shut down of it, at the end of a turn.
 *
 * @param <T>
 */
public interface FlowOrchestrator<T extends FlowContext> {

  GameModel getModel();

  /**
   * Delegated to call handleEvent on the actual FlowContext.
   *
   * @param event
   * @param view
   */
  void handleEvent(ViewEvent event, GameView view);

  /**
   * Delegated to call the end turn callback.
   *
   * @param view
   */
  void onEnd(GameView view);

  /**
   * Used to resolve a flow node, starting from its name.
   *
   * @param id
   * @return
   */
  FlowNode resolveNode(String id);

  /**
   * Starts a new flow after nodes are loaded in the context
   *
   * @param view
   * @param context
   */
  void startNewFlow(GameView view, T context);

  T getActualContext();

}
