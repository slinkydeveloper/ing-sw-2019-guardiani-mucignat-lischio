package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.flow.FlowOrchestrator;
import com.adrenalinici.adrenaline.flow.impl.FlowOrchestratorImpl;
import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.util.DecoratedEvent;
import com.adrenalinici.adrenaline.util.Observer;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.Collections;
import java.util.List;

public class GameController implements Observer<DecoratedEvent<ViewEvent, GameView>> {

  private GameModel gameModel;
  private FlowOrchestrator<ControllerFlowContext> flowOrchestrator;
  private GunLoader gunLoader;

  public GameController(List<? extends FlowNode> flowNodes, GameModel gameModel, GunLoader gunLoader) {
    this.gameModel = gameModel;
    this.flowOrchestrator = new FlowOrchestratorImpl<>(
      flowNodes,
      this.gameModel,
      this::endTurnCallback
    );
    this.startNewTurn(null, gameModel.getPlayers().get(0));
    this.gunLoader = gunLoader;
  }

  @Override
  public void onEvent(DecoratedEvent<ViewEvent, GameView> event) {
    flowOrchestrator.handleEvent(event.getInnerEvent(), event.getEventSource());
  }

  private PlayerColor nextTurnPlayer() {
    List<PlayerColor> players = gameModel.getPlayers();
    if (flowOrchestrator.getActualContext().getTurnOfPlayer() == null)
      return players.get(0);
    return players.get(players.indexOf(flowOrchestrator.getActualContext().getTurnOfPlayer()) + 1 % players.size());
  }

  void endTurnCallback(GameView view) {
    //TODO P2 refill dashboard
    //TODO P2 somewhere maybe here should go the check if match finished
    PlayerColor playerTurn = nextTurnPlayer();
    startNewTurn(view, playerTurn);
  }

  private void startNewTurn(GameView view, PlayerColor player) {
    this.flowOrchestrator.startNewFlow(view, new ControllerFlowContext(
      this.flowOrchestrator, Collections.singletonList(ControllerNodes.START_TURN.name()), gunLoader
    ).setTurnOfPlayer(player));
    if (view != null) view.showNextTurn(player);
  }

  protected ControllerFlowContext getFlowContext() {
    return this.flowOrchestrator.getActualContext();
  }

}
