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

public class TractorBeamGunFactory extends AlternativeEffectGunFactory {
  private static final TriConsumer<AlternativeEffectGunFlowState, GameModel, ControllerFlowContext> BASE_EFFECT_APPLY = (state, model, context) -> {
    state.getChosenPlayersToHit().forEach(p -> {
      state.hitPlayer(p, 1);
    });
  };

  private static final TriConsumer<AlternativeEffectGunFlowState, GameModel, ControllerFlowContext> PUNISHER_EFFECT_APPLY = (state, model, context) -> {
    state.getChosenPlayersToHit().forEach(p -> {
      state.hitPlayer(p, 3);
      model.movePlayerInDashboard(model.getPlayerPosition(context.getTurnOfPlayer()), p);
    });
  };

  @Override
  public boolean canConsume(String key, ObjectNode config) {
    return "tractor_beam".equals(key);
  }

  @Override
  public List<ControllerFlowNode> getAdditionalNodes(String key, ObjectNode config) {
    return Arrays.asList(
      new ApplyAlternativeGunFlowNode(applyGunEffect("tractor_beam", "base"), BASE_EFFECT_APPLY),
      new ApplyAlternativeGunFlowNode(applyGunEffect("tractor_beam", "punisher"), PUNISHER_EFFECT_APPLY)
    );
  }
}
