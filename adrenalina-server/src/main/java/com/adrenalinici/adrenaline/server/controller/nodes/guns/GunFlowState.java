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

public interface GunFlowState extends FlowState {
  List<Position> getChosenCellsToHit();

  List<PlayerColor> getChosenPlayersToHit();

  Map<PlayerColor, Integer> getHitPlayers();

  Map<PlayerColor, Integer> getMarkPlayers();

  void hitPlayer(PlayerColor color, int damages);

  void markPlayer(PlayerColor color, int marks);

  DecoratedGun getChosenGun();

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
