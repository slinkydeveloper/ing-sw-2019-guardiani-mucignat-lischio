package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.DecoratedGun;
import com.adrenalinici.adrenaline.server.model.GameModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GunFlowStateImpl implements GunFlowState {

  DecoratedGun chosenGun;
  private List<Position> chosenCellsToHit;
  private List<PlayerColor> chosenPlayersToHit;
  private Map<PlayerColor, Integer> hitPlayers;
  private Map<PlayerColor, Integer> markPlayers;

  public GunFlowStateImpl(DecoratedGun chosenGun) {
    this.chosenGun = chosenGun;
    this.chosenCellsToHit = new ArrayList<>();
    this.chosenPlayersToHit = new ArrayList<>();
    this.hitPlayers = new HashMap<>();
    this.markPlayers = new HashMap<>();
  }

  @Override
  public List<Position> getChosenCellsToHit() {
    return chosenCellsToHit;
  }

  @Override
  public List<PlayerColor> getChosenPlayersToHit() {
    return chosenPlayersToHit;
  }

  @Override
  public Map<PlayerColor, Integer> getHitPlayers() {
    return hitPlayers;
  }

  @Override
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
  @Override
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
  @Override
  public void markPlayer(PlayerColor color, int marks) {
    this.markPlayers.merge(color, marks, (oldValue, value) -> oldValue + marks);
  }

  @Override
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
  @Override
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

}
