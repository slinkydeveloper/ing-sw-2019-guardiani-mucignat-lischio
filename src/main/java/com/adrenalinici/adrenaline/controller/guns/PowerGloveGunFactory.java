package com.adrenalinici.adrenaline.controller.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.controller.nodes.guns.AlternativeEffectGunFlowState;
import com.adrenalinici.adrenaline.controller.nodes.guns.ApplyAlternativeGunFlowNode;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.util.TriConsumer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Arrays;
import java.util.List;

import static com.adrenalinici.adrenaline.controller.nodes.ControllerNodes.applyGunEffect;

public class PowerGloveGunFactory extends AlternativeEffectGunFactory {

  private static final TriConsumer<AlternativeEffectGunFlowState, GameModel, ControllerFlowContext> BASE_EFFECT_APPLY = (state, model, context) -> {
    PlayerColor firstPlayer = (!state.getChosenPlayersToHit().isEmpty()) ?
      state.getChosenPlayersToHit().get(0) : null;

    if (firstPlayer != null) {
      state.hitPlayer(firstPlayer, 1);
      state.markPlayer(firstPlayer, 2);
    }
    model.movePlayerInDashboard(model.getPlayerPosition(firstPlayer), context.getTurnOfPlayer());
  };

  private static final TriConsumer<AlternativeEffectGunFlowState, GameModel, ControllerFlowContext> ROCKET_EFFECT_APPLY = (state, model, context) -> {
    PlayerColor firstPlayer = (!state.getChosenPlayersToHit().isEmpty()) ?
      state.getChosenPlayersToHit().get(0) : null;
    PlayerColor secondPlayer = (state.getChosenPlayersToHit().size() == 2) ?
      state.getChosenPlayersToHit().get(1) : null;

    if (firstPlayer != null) {
      state.hitPlayer(firstPlayer, 2);
      if (secondPlayer == null)
        model.movePlayerInDashboard(model.getPlayerPosition(firstPlayer), context.getTurnOfPlayer());
    }

    if (secondPlayer != null) {
      state.hitPlayer(secondPlayer, 2);
      model.movePlayerInDashboard(model.getPlayerPosition(secondPlayer), context.getTurnOfPlayer());
    }
  };

  @Override
  public boolean canConsume(String key, ObjectNode config) {
    return "power_glove".equals(key);
  }

  @Override
  public List<ControllerFlowNode> getAdditionalNodes(String key, ObjectNode config) {
    return Arrays.asList(
      new ApplyAlternativeGunFlowNode(applyGunEffect("power_glove", "base"), BASE_EFFECT_APPLY),
      new ApplyAlternativeGunFlowNode(applyGunEffect("power_glove", "rocket"), ROCKET_EFFECT_APPLY)
    );
  }
}
