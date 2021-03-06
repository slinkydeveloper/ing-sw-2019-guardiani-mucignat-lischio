package com.adrenalinici.adrenaline.server.controller.guns;

import com.adrenalinici.adrenaline.common.util.TriConsumer;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.AlternativeEffectGunFlowState;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.ApplyAlternativeGunFlowNode;
import com.adrenalinici.adrenaline.server.model.GameModel;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Arrays;
import java.util.List;

import static com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes.applyGunEffect;

public class HellionGunFactory extends AlternativeEffectGunFactory {
  private static final TriConsumer<AlternativeEffectGunFlowState, GameModel, ControllerFlowContext> BASE_EFFECT_APPLY = (state, model, context) -> {
    state.getChosenPlayersToHit().forEach(victim -> {
      state.hitPlayer(victim, 1);
      state.markPlayer(victim, 1);

      model.getDashboard().getDashboardCell(model.getPlayerPosition(victim))
        .getPlayersInCell()
        .stream()
        .filter(otherPlayer -> !otherPlayer.equals(victim))
        .forEach(p -> state.markPlayer(p, 1));
    });
  };

  private static final TriConsumer<AlternativeEffectGunFlowState, GameModel, ControllerFlowContext> NANOTRACER_EFFECT_APPLY = (state, model, context) -> {
    state.getChosenPlayersToHit().forEach(victim -> {
      state.hitPlayer(victim, 1);
      state.markPlayer(victim, 2);

      model.getDashboard().getDashboardCell(model.getPlayerPosition(victim))
        .getPlayersInCell()
        .stream()
        .filter(otherPlayer -> !otherPlayer.equals(victim))
        .forEach(p -> state.markPlayer(p, 2));
    });
  };

  @Override
  public boolean canConsume(String key, ObjectNode config) {
    return "hellion".equals(key);
  }

  @Override
  public List<ControllerFlowNode> getAdditionalNodes(String key, ObjectNode config) {
    return Arrays.asList(
      new ApplyAlternativeGunFlowNode(applyGunEffect("hellion", "base"), BASE_EFFECT_APPLY),
      new ApplyAlternativeGunFlowNode(applyGunEffect("hellion", "nanoTracer"), NANOTRACER_EFFECT_APPLY)
    );

  }
}
