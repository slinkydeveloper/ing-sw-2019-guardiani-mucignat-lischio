package com.adrenalinici.adrenaline.controller.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.controller.nodes.guns.ApplyBaseGunFlowNode;
import com.adrenalinici.adrenaline.controller.nodes.guns.BaseEffectGunFlowState;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.util.TriConsumer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collections;
import java.util.List;

public class WhisperGunFactory extends BaseEffectGunFactory {
  private static final TriConsumer<BaseEffectGunFlowState, GameModel, ControllerFlowContext> APPLY = (state, gameModel, context) -> {
    PlayerColor player = (!state.getChosenPlayersToHit().isEmpty()) ?
      state.getChosenPlayersToHit().get(0) : null;

    if (player != null) {
      state.hitPlayer(player, 3);
      state.markPlayer(player, 1);
    }
  };

  @Override
  public boolean canConsume(String key, ObjectNode config) {
    return "whisper".equals(key);
  }

  @Override
  public List<ControllerFlowNode> getAdditionalNodes(String key, ObjectNode config) {
    return Collections.singletonList(new ApplyBaseGunFlowNode(ControllerNodes.applyGunEffect("whisper", "base"), APPLY));
  }
}
