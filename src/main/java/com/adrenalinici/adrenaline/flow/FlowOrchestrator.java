package com.adrenalinici.adrenaline.flow;

import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

public interface FlowOrchestrator<T extends FlowContext> {

  GameModel getModel();

  void handleEvent(ViewEvent event, GameView view);

  void onEnd(GameView view);

  FlowNode resolveNode(String id);

  void startNewFlow(GameView view, T context);

  T getActualContext();

}
