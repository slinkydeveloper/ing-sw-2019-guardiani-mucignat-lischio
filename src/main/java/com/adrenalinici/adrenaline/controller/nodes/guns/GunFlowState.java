package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.DecoratedGun;
import com.adrenalinici.adrenaline.flow.FlowState;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GunFlowState implements FlowState {

  DecoratedGun chosenGun;
  private List<PlayerColor> chosenPlayersToHit;
  private Map<PlayerColor, Integer> hitPlayers;
  private Map<PlayerColor, Integer> markPlayers;

  public GunFlowState(DecoratedGun chosenGun) {
    this.chosenGun = chosenGun;
    this.chosenPlayersToHit = new ArrayList<>();
    this.hitPlayers = new HashMap<>();
    this.markPlayers = new HashMap<>();
  }

  public List<PlayerColor> getChosenPlayersToHit() {
    return chosenPlayersToHit;
  }

  public Map<PlayerColor, Integer> getHitPlayers() {
    return hitPlayers;
  }

  public Map<PlayerColor, Integer> getMarkPlayers() {
    return markPlayers;
  }

  /**
   * This method add damages to the cached value mapped by the specified colour
   * in hitPlayers map
   *
   * @param color   of the victim
   * @param damages to add
   */
  public void hitPlayer(PlayerColor color, int damages) {
    this.hitPlayers.merge(color, damages, (oldValue, value) -> oldValue + damages);
  }

  /**
   * This method add marks to the cached value mapped by the specified colour
   * in markPayers map
   *
   * @param color of the victim
   * @param marks to add
   */
  public void markPlayer(PlayerColor color, int marks) {
    this.markPlayers.merge(color, marks, (oldValue, value) -> oldValue + marks);
  }

  public DecoratedGun getChosenGun() {
    return chosenGun;
  }


  /**
   * This method uses the cached values of hit and mark to add to each player
   * in order to modify the model of every victim and also the flow context
   * in case anyone is killed
   *
   * @param model
   * @param context
   */
  public void applyHitAndMarkPlayers(GameModel model, ControllerFlowContext context) {
    List<PlayerColor> bothHittedAndMarked = new ArrayList<>();

    hitPlayers.forEach(
      (victim, damages) -> {
        if (markPlayers.containsKey(victim)) {
          bothHittedAndMarked.add(victim);
          int marks = markPlayers.get(victim);
          boolean killed = model
            .hitAndMarkPlayer(context.getTurnOfPlayer(), victim, damages, marks);
          if (killed) context.getKilledPlayers().add(victim);
        } else {
          boolean killed = model
            .hitPlayer(context.getTurnOfPlayer(), victim, damages);
          if (killed) context.getKilledPlayers().add(victim);
        }
      });

    markPlayers.forEach(
      (victim, marks) -> {
        if (!bothHittedAndMarked.contains(victim)) {
          model.markPlayer(context.getTurnOfPlayer(), victim, marks);
        }
      });
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
