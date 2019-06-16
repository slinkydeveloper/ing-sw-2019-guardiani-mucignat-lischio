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

public class ShotgunGunFactory extends AlternativeEffectGunFactory {
  private static final TriConsumer<AlternativeEffectGunFlowState, GameModel, ControllerFlowContext> BASE_EFFECT_APPLY = (state, model, context) -> {
    state.getChosenPlayersToHit().forEach(p -> state.hitPlayer(p, 3));
  };

  private static final TriConsumer<AlternativeEffectGunFlowState, GameModel, ControllerFlowContext> LONG_EFFECT_APPLY = (state, model, context) -> {
    state.getChosenPlayersToHit().forEach(p -> state.hitPlayer(p, 2));
  };

  @Override
  public boolean canConsume(String key, ObjectNode config) {
    return "shotgun".equals(key);
  }

  @Override
  public List<ControllerFlowNode> getAdditionalNodes(String key, ObjectNode config) {
    return Arrays.asList(
      new ApplyAlternativeGunFlowNode(applyGunEffect("shotgun", "base"), BASE_EFFECT_APPLY),
      new ApplyAlternativeGunFlowNode(applyGunEffect("shotgun", "long"), LONG_EFFECT_APPLY)
    );
  }
}
