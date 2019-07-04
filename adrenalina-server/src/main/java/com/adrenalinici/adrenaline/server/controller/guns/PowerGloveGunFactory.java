package com.adrenalinici.adrenaline.server.controller.guns;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.util.TriConsumer;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.AlternativeEffectGunFlowState;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.ApplyAlternativeGunFlowNode;
import com.adrenalinici.adrenaline.server.model.GameModel;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Arrays;
import java.util.List;

public class PowerGloveGunFactory extends AlternativeEffectGunFactory {

  private static final TriConsumer<AlternativeEffectGunFlowState, GameModel, ControllerFlowContext> BASE_EFFECT_APPLY = (state, model, context) -> {
    PlayerColor firstPlayer = (!state.getChosenPlayersToHit().isEmpty()) ?
      state.getChosenPlayersToHit().get(0) : null;

    if (firstPlayer != null) {
      state.hitPlayer(firstPlayer, 1);
      state.markPlayer(firstPlayer, 2);
      model.movePlayerInDashboard(model.getPlayerPosition(firstPlayer), context.getTurnOfPlayer());
    }
  };

  private static final TriConsumer<AlternativeEffectGunFlowState, GameModel, ControllerFlowContext> ROCKET_EFFECT_APPLY = (state, model, context) -> {
    PlayerColor firstPlayer = (!state.getChosenPlayersToHit().isEmpty()) ?
      state.getChosenPlayersToHit().get(0) : null;
    PlayerColor secondPlayer = (state.getChosenPlayersToHit().size() == 2) ?
      state.getChosenPlayersToHit().get(1) : null;

    if (firstPlayer != null) {
      state.setKillerStartingPosition(model.getPlayerPosition(context.getTurnOfPlayer()));
      state.setFirstVictimPosition(model.getPlayerPosition(firstPlayer));

      state.hitPlayer(firstPlayer, 2);
      if (secondPlayer == null)
        model.movePlayerInDashboard(state.getFirstVictimPosition(), context.getTurnOfPlayer());
    }

    if (secondPlayer != null) {
      state.setSecondVictimPosition(model.getPlayerPosition(secondPlayer));
      state.hitPlayer(secondPlayer, 2);
      model.movePlayerInDashboard(state.getSecondVictimPosition(), context.getTurnOfPlayer());
    }
  };

  @Override
  public boolean canConsume(String key, ObjectNode config) {
    return "power_glove".equals(key);
  }

  @Override
  public List<ControllerFlowNode> getAdditionalNodes(String key, ObjectNode config) {
    return Arrays.asList(
      new ApplyAlternativeGunFlowNode(ControllerNodes.applyGunEffect("power_glove", "base"), BASE_EFFECT_APPLY),
      new ApplyAlternativeGunFlowNode(ControllerNodes.applyGunEffect("power_glove", "rocket"), ROCKET_EFFECT_APPLY)
    );
  }
}
