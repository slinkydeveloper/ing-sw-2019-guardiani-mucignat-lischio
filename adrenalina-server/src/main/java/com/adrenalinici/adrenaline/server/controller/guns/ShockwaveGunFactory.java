package com.adrenalinici.adrenaline.server.controller.guns;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.util.TriConsumer;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.AlternativeEffectGunFlowState;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.ApplyAlternativeGunFlowNode;
import com.adrenalinici.adrenaline.server.model.DashboardCell;
import com.adrenalinici.adrenaline.server.model.GameModel;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes.applyGunEffect;

public class ShockwaveGunFactory extends AlternativeEffectGunFactory {
  private static final TriConsumer<AlternativeEffectGunFlowState, GameModel, ControllerFlowContext> BASE_EFFECT_APPLY = (state, model, context) -> {
    PlayerColor firstPlayer = (!state.getChosenPlayersToHit().isEmpty()) ?
      state.getChosenPlayersToHit().get(0) : null;
    PlayerColor secondPlayer = (state.getChosenPlayersToHit().size() >= 2) ?
      state.getChosenPlayersToHit().get(1) : null;
    PlayerColor thirdPlayer = (state.getChosenPlayersToHit().size() == 3) ?
      state.getChosenPlayersToHit().get(2) : null;
    if (firstPlayer != null) {
      state.hitPlayer(firstPlayer, 1);
    }
    if (secondPlayer != null) {
      state.hitPlayer(secondPlayer, 1);
    }
    if (thirdPlayer != null) {
      state.hitPlayer(thirdPlayer, 1);
    }
  };

  private static final TriConsumer<AlternativeEffectGunFlowState, GameModel, ControllerFlowContext> TSUNAMI_EFFECT_APPLY = (state, model, context) -> {
    List<DashboardCell> adjacentCells = model.getDashboard()
      .stream()
      .filter(dashboardCell ->
        model.getDashboard()
          .calculateDistance(model.getPlayerPosition(context.getTurnOfPlayer()), dashboardCell.getPosition()) == 1
      ).collect(Collectors.toList());

    adjacentCells
      .forEach(cell ->
        cell.getPlayersInCell().forEach(p -> state.hitPlayer(p, 1))
      );
  };

  @Override
  public boolean canConsume(String key, ObjectNode config) {
    return "shockwave".equals(key);
  }

  @Override
  public List<ControllerFlowNode> getAdditionalNodes(String key, ObjectNode config) {
    return Arrays.asList(
      new ApplyAlternativeGunFlowNode(applyGunEffect("shockwave", "base"), BASE_EFFECT_APPLY),
      new ApplyAlternativeGunFlowNode(applyGunEffect("shockwave", "tsunami"), TSUNAMI_EFFECT_APPLY)
    );
  }
}
