package com.adrenalinici.adrenaline.server.controller.guns;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.util.TriConsumer;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.ApplyBaseGunFlowNode;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.BaseEffectGunFlowState;
import com.adrenalinici.adrenaline.server.model.GameModel;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collections;
import java.util.List;

public class PlasmaGunGunFactory extends BaseEffectGunFactory {
  private static final TriConsumer<BaseEffectGunFlowState, GameModel, ControllerFlowContext> APPLY = (state, gameModel, context) -> {
    PlayerColor player = (!state.getChosenPlayersToHit().isEmpty()) ?
      state.getChosenPlayersToHit().get(0) : null;

    if (player != null) {
      state.hitPlayer(player, state.isActivatedSecondExtraEffect() ? 3 : 2);
    }
  };

  @Override
  public boolean canConsume(String key, ObjectNode config) {
    return "plasma_gun".equals(key);
  }

  @Override
  public List<ControllerFlowNode> getAdditionalNodes(String key, ObjectNode config) {
    return Collections.singletonList(new ApplyBaseGunFlowNode(ControllerNodes.applyGunEffect("plasma_gun", "base"), APPLY));
  }
}
