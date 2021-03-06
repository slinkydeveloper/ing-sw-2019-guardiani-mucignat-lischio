package com.adrenalinici.adrenaline.server.controller;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.server.flow.FlowOrchestrator;
import com.adrenalinici.adrenaline.server.flow.impl.BaseFlowContext;

import java.util.ArrayList;
import java.util.List;

public class ControllerFlowContext extends BaseFlowContext {

  private int remainingActions;
  private PlayerColor turnOfPlayer;
  private List<PlayerColor> killedPlayers;
  private boolean isFrenzyMode;
  private boolean isFirstPlayerOrAfterFirstPlayerInFrenzyMode;

  public ControllerFlowContext(FlowOrchestrator orchestrator, List<String> initialPhases) {
    this(orchestrator, initialPhases, null);
  }

  public ControllerFlowContext(FlowOrchestrator orchestrator, List<String> initialPhases, List<String> additionalNodesToExecuteAlways) {
    super(orchestrator, initialPhases, additionalNodesToExecuteAlways);
    this.killedPlayers = new ArrayList<>();
    this.isFrenzyMode = false;
    this.isFirstPlayerOrAfterFirstPlayerInFrenzyMode = false;
  }

  public int getRemainingActions() {
    return remainingActions;
  }

  public ControllerFlowContext setRemainingActions(int remainingActions) {
    this.remainingActions = remainingActions;
    return this;
  }

  public ControllerFlowContext decrementRemainingActions() {
    this.remainingActions--;
    return this;
  }

  public ControllerFlowContext incrementRemainingActions() {
    this.remainingActions++;
    return this;
  }

  public PlayerColor getTurnOfPlayer() {
    return turnOfPlayer;
  }

  public ControllerFlowContext setTurnOfPlayer(PlayerColor turnOfPlayer) {
    this.turnOfPlayer = turnOfPlayer;
    return this;
  }

  public List<PlayerColor> getKilledPlayers() {
    return killedPlayers;
  }

  public ControllerFlowContext setKilledPlayers(List<PlayerColor> killedPlayers) {
    this.killedPlayers = killedPlayers;
    return this;
  }

  public boolean isFrenzyMode() {
    return isFrenzyMode;
  }

  public ControllerFlowContext setFrenzyMode(boolean frenzyMode) {
    isFrenzyMode = frenzyMode;
    return this;
  }

  public boolean isFirstPlayerOrAfterFirstPlayerInFrenzyMode() {
    return isFirstPlayerOrAfterFirstPlayerInFrenzyMode;
  }

  public ControllerFlowContext setFirstPlayerOrAfterFirstPlayerInFrenzyMode(boolean firstPlayerOrAfterFirstPlayerInFrenzyMode) {
    isFirstPlayerOrAfterFirstPlayerInFrenzyMode = firstPlayerOrAfterFirstPlayerInFrenzyMode;
    return this;
  }
}
