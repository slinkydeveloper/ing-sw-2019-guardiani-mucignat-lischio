package com.adrenalinici.adrenaline.flow.impl;


import com.adrenalinici.adrenaline.flow.FlowContext;
import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.flow.FlowOrchestrator;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FlowOrchestratorImpl<T extends FlowContext> implements FlowOrchestrator<T> {

  private Map<String, ? extends FlowNode> nodes;
  private T actualContext;
  private GameModel model;
  private Consumer<GameView> onEndCallback;

  public FlowOrchestratorImpl(List<? extends FlowNode> nodes, GameModel model, Consumer<GameView> onEndCallback) {
    this.nodes = nodes
      .stream()
      .map(n -> new AbstractMap.SimpleImmutableEntry<>(n.id(), n))
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    this.model = model;
    this.onEndCallback = onEndCallback;
  }

  @Override
  public GameModel getModel() {
    return model;
  }

  @Override
  public void handleEvent(ViewEvent event, GameView view) {
    if (actualContext != null)
      actualContext.handleEvent(event, view);
  }

  public void onEnd(GameView view) {
    onEndCallback.accept(view);
  }

  @Override
  public FlowNode resolveNode(String id) {
    return nodes.get(id);
  }

  @Override
  public void startNewFlow(GameView gameView, T context) {
    this.actualContext = context;
    context.nextPhase(gameView);
  }

  @Override
  public T getActualContext() {
    return actualContext;
  }
}
