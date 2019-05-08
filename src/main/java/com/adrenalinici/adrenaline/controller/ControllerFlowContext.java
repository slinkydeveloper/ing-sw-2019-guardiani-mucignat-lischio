package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.flow.FlowOrchestrator;
import com.adrenalinici.adrenaline.flow.impl.BaseFlowContext;
import com.adrenalinici.adrenaline.model.PlayerColor;

import java.util.ArrayList;
import java.util.List;

public class ControllerFlowContext extends BaseFlowContext {

  private int remainingActions;
  private PlayerColor turnOfPlayer;
  private List<PlayerColor> killedPlayers;

  public ControllerFlowContext(FlowOrchestrator orchestrator) {
    this(orchestrator, null);
  }

  public ControllerFlowContext(FlowOrchestrator orchestrator, List<String> initialPhases) {
    super(orchestrator, initialPhases);
    this.killedPlayers = new ArrayList<>();
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
}
