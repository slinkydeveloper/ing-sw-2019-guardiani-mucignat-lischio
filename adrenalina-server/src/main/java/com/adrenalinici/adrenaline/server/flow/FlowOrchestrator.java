package com.adrenalinici.adrenaline.server.flow;

import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.model.GameModel;

public interface FlowOrchestrator<T extends FlowContext> {

  GameModel getModel();

  void handleEvent(ViewEvent event, GameView view);

  void onEnd(GameView view);

  FlowNode resolveNode(String id);

  void startNewFlow(GameView view, T context);

  T getActualContext();

}
