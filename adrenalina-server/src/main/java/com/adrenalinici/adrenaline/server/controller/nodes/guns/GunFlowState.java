package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.DecoratedGun;
import com.adrenalinici.adrenaline.server.flow.FlowState;
import com.adrenalinici.adrenaline.server.model.GameModel;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.Map;

/**
 * It represents the state of the flow during the use of a gun.
 * It collects information about the effect of the gun in use and players to hit and mark.
 */
public interface GunFlowState extends FlowState {
  List<Position> getChosenCellsToHit();

  List<PlayerColor> getChosenPlayersToHit();

  Map<PlayerColor, Integer> getHitPlayers();

  Map<PlayerColor, Integer> getMarkPlayers();

  /**
   * Stores values of the damages to be added to a certain player
   *
   * @param color
   * @param damages
   */
  void hitPlayer(PlayerColor color, int damages);

  /**
   * Stores values of the marks to be added to a certain player
   *
   * @param color
   * @param marks
   */
  void markPlayer(PlayerColor color, int marks);

  DecoratedGun getChosenGun();

  /**
   * Applies damages and marks to the players, according to values
   * previously cached in the state
   *
   * @param model
   * @param context
   */
  void applyHitAndMarkPlayers(GameModel model, ControllerFlowContext context);

  /**
   * This function resolves a phase configuration based on chosen effect/effects. <br/>
   * It should also merge two configurations when available
   *
   * @param phaseId
   * @return
   */
  ObjectNode resolvePhaseConfiguration(String phaseId);
}
