package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.controller.nodes.*;
import com.adrenalinici.adrenaline.controller.nodes.guns.*;
import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.flow.FlowOrchestrator;
import com.adrenalinici.adrenaline.flow.impl.FlowOrchestratorImpl;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.util.DecoratedEvent;
import com.adrenalinici.adrenaline.util.Observer;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameController implements Observer<DecoratedEvent<ViewEvent, GameView>> {

  private GameModel gameModel;
  private FlowOrchestrator<ControllerFlowContext> flowOrchestrator;
  private boolean firstTurn = true;

  public GameController(List<? extends FlowNode> flowNodes, GameModel gameModel) {
    this.gameModel = gameModel;
    this.flowOrchestrator = new FlowOrchestratorImpl<>(
      flowNodes,
      this.gameModel,
      this::endTurnCallback
    );
  }

  public GameController(GameModel model) {
    this(loadNodes(), model);
  }

  public void startMatch(GameView view) {
    this.flowOrchestrator.startNewFlow(view, new ControllerFlowContext(
      this.flowOrchestrator,
      Collections.singletonList(ControllerNodes.FIRST_TURN.name())
    ));
  }

  @Override
  public void onEvent(DecoratedEvent<ViewEvent, GameView> event) {
    if (event.getInnerEvent().isStartMatchEvent()) {
      startMatch(event.getEventSource());
    } else {
      flowOrchestrator.handleEvent(event.getInnerEvent(), event.getEventSource());
    }
  }

  private PlayerColor nextTurnPlayer() {
    List<PlayerColor> players = gameModel.getPlayers();
    if (flowOrchestrator.getActualContext().getTurnOfPlayer() == null)
      return players.get(0);
    return players.get(players.indexOf(flowOrchestrator.getActualContext().getTurnOfPlayer()) + 1 % players.size());
  }

  void endTurnCallback(GameView view) {
    gameModel.refillDashboard();
    //TODO P2 somewhere maybe here we should check if match is finished
    if (firstTurn) {
      firstTurn = false;
      startNewTurn(view, gameModel.getPlayers().get(0));
    } else {
      PlayerColor playerTurn = nextTurnPlayer();
      startNewTurn(view, playerTurn);
    }
  }

  private void startNewTurn(GameView view, PlayerColor player) {
    this.flowOrchestrator.startNewFlow(view, new ControllerFlowContext(
      this.flowOrchestrator,
      Collections.singletonList(ControllerNodes.START_TURN.name())
    ).setTurnOfPlayer(player));
    if (view != null) view.showNextTurn(player);
  }

  public ControllerFlowContext getFlowContext() {
    return this.flowOrchestrator.getActualContext();
  }

  public static List<FlowNode> loadNodes() {
    List<FlowNode> nodes = new ArrayList<>(Arrays.asList(
      new ChooseActionFlowNode(),
      new ChooseGunFlowNode(),
      new ChooseMovementFlowNode(1),
      new ChooseMovementFlowNode(2),
      new ChooseMovementFlowNode(3),
      new ChooseMovementFlowNode(4),
      new FirstTurnFlowNode(),
      new NewTurnFlowNode(),
      new PickupFlowNode(),
      new ReloadFlowNode(),
      new RespawnFlowNode(),
      new ChoosePlayersToHitFlowNode(),
      new ChooseBaseEffectForGunFlowNode(),
      new ChooseAlternativeEffectForGunFlowNode(),
      new GunChooseMovementFlowNode(1),
      new GunChooseMovementFlowNode(2),
      new GunChooseEnemyMovementFlowNode(1),
      new GunChooseEnemyMovementFlowNode(2),
      new ChooseRoomToHitFlowNode(),
      new ChooseCellToHitFlowNode()
    ));
    nodes.addAll(GunLoader.INSTANCE.getAllAdditionalNodes());
    return nodes;
  }

  public GameModel getGameModel() {
    return gameModel;
  }
}
