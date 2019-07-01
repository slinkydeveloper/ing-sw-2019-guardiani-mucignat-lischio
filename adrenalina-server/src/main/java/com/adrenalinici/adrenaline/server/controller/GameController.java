package com.adrenalinici.adrenaline.server.controller;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.util.DecoratedEvent;
import com.adrenalinici.adrenaline.common.util.Observer;
import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.UnavailablePlayerEvent;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.controller.nodes.*;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.*;
import com.adrenalinici.adrenaline.server.flow.FlowNode;
import com.adrenalinici.adrenaline.server.flow.FlowOrchestrator;
import com.adrenalinici.adrenaline.server.flow.impl.FlowOrchestratorImpl;
import com.adrenalinici.adrenaline.server.model.GameModel;

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
    gameModel.refillDashboard();
    this.flowOrchestrator.startNewFlow(view, new ControllerFlowContext(
      this.flowOrchestrator,
      Collections.singletonList(ControllerNodes.FIRST_TURN.name())
    ));
  }

  @Override
  public void onEvent(DecoratedEvent<ViewEvent, GameView> event) {
    if (event.getInnerEvent().isStartMatchEvent()) {
      startMatch(event.getEventSource());
    } else if (
      event.getInnerEvent().isUnavailablePlayerEvent() &&
      flowOrchestrator.getActualContext().getTurnOfPlayer() == ((UnavailablePlayerEvent)event.getInnerEvent()).getPlayerColor()
    ) {
      this.endTurnCallback(event.getEventSource());
    } else if (event.getInnerEvent().isEndMatchEvent()) {
      this.endMatchCallback(event.getEventSource());
    } else {
      flowOrchestrator.handleEvent(event.getInnerEvent(), event.getEventSource());
    }
  }

  private PlayerColor nextTurnPlayer() {
    List<PlayerColor> players = gameModel.getPlayers();
    if (flowOrchestrator.getActualContext().getTurnOfPlayer() == null)
      return players.get(0);
    return players.get((players.indexOf(flowOrchestrator.getActualContext().getTurnOfPlayer()) + 1) % players.size());
  }

  void endTurnCallback(GameView view) {
    gameModel.refillDashboard();
    if (firstTurn) {
      // First turn
      firstTurn = false;
      startNewTurn(view, gameModel.getPlayers().get(0));
    } else if (gameModel.noRemainingSkulls()) {
      // Frenzy mode or end game
      PlayerColor playerTurn = nextTurnPlayer();
      if (gameModel.isFrenzyModeActivated()) {
        // Frenzy mode already activated
        if (gameModel.isFrenzyModeFinished(playerTurn)) {
          // Frenzy mode finished
          endMatchCallback(view);
        } else {
          startNewFrenzyTurn(view, playerTurn, gameModel.isFirstPlayerOrAfterFirstPlayerInFrenzyMode(playerTurn));
        }
      } else if (gameModel.isMustActivateFrenzyMode()) {
        // Frenzy mode still not activated, but it must be
        gameModel.activateFrenzyMode(nextTurnPlayer());
        startNewFrenzyTurn(view, playerTurn, gameModel.isFirstPlayerOrAfterFirstPlayerInFrenzyMode(playerTurn));
      } else {
        // No frenzy mode, close match
        endMatchCallback(view);
      }
    } else {
      // Normal turn
      PlayerColor playerTurn = nextTurnPlayer();
      startNewTurn(view, playerTurn);
    }
  }

  void endMatchCallback(GameView view) {
    gameModel.assignEndGamePoints();
    view.showRanking(gameModel.getRanking());
  }

  private void startNewTurn(GameView view, PlayerColor player) {
    this.flowOrchestrator.startNewFlow(view, new ControllerFlowContext(
      this.flowOrchestrator,
      Collections.singletonList(ControllerNodes.START_TURN.name()),
      ALWAYS_LOADED_NODES
    ).setTurnOfPlayer(player));
    if (view != null) view.showNextTurn(player);
  }

  private void startNewFrenzyTurn(GameView view, PlayerColor player, boolean isFirstPlayerOrAfterFirstPlayerInFrenzyMode) {
    this.flowOrchestrator.startNewFlow(view,
      new ControllerFlowContext(
        this.flowOrchestrator,
        Collections.singletonList(ControllerNodes.START_TURN.name())
      )
        .setTurnOfPlayer(player)
        .setFrenzyMode(true)
        .setFirstPlayerOrAfterFirstPlayerInFrenzyMode(isFirstPlayerOrAfterFirstPlayerInFrenzyMode)
    );
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
      new BaseGunChooseMovementFlowNode(1),
      new BaseGunChooseMovementFlowNode(2),
      new GunChooseEnemyMovementFlowNode(1),
      new GunChooseEnemyMovementFlowNode(2),
      new ChooseRoomToHitFlowNode(),
      new ChooseCellToHitFlowNode(),
      new ApplyNewtonFlowNode(),
      new ApplyTeleporterFlowNode(),
      new ApplyScopeFlowNode(),
      new TagbackGrenadeFlowNode(),
      new AlternativeGunChooseMovementFlowNode()
    ));
    nodes.addAll(GunLoader.INSTANCE.getAllAdditionalNodes());
    return nodes;
  }

  public static final List<String> ALWAYS_LOADED_NODES = Arrays.asList(ControllerNodes.APPLY_TELEPORT.name(), ControllerNodes.APPLY_NEWTON.name());

  public GameModel getGameModel() {
    return gameModel;
  }
}
