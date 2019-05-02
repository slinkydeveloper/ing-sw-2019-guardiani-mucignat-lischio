package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.DecoratedGun;
import com.adrenalinici.adrenaline.flow.FlowState;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

public abstract class GunFlowState implements FlowState {

  DecoratedGun chosenGun;
  private List<PlayerColor> chosenPlayersToHit;
  private List<PlayerColor> hitPlayers;

  public GunFlowState(DecoratedGun chosenGun) {
    this.chosenGun = chosenGun;
    this.chosenPlayersToHit = new ArrayList<>();
    this.hitPlayers = new ArrayList<>();
  }

  public List<PlayerColor> getChosenPlayersToHit() {
    return chosenPlayersToHit;
  }

  public List<PlayerColor> getHitPlayers() {
    return hitPlayers;
  }

  public DecoratedGun getChosenGun() {
    return chosenGun;
  }

  /**
   * This function resolves a phase configuration based on chosen effect/effects. <br/>
   * It should also merge two configurations when available
   *
   * @param phaseId
   * @return
   */
  public abstract ObjectNode resolvePhaseConfiguration(String phaseId);
}
