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

import java.util.Arrays;
import java.util.List;

public class CyberbladeGunFactory extends BaseEffectGunFactory {
  private static final TriConsumer<BaseEffectGunFlowState, GameModel, ControllerFlowContext> APPLY_BASE = (state, gameModel, context) -> {
    PlayerColor player = (!state.getChosenPlayersToHit().isEmpty()) ?
      state.getChosenPlayersToHit().get(0) : null;

    if (player != null)
      state.hitPlayer(player, 2);
  };

  private static final TriConsumer<BaseEffectGunFlowState, GameModel, ControllerFlowContext> APPLY_SLICE = (state, gameModel, context) -> {
    PlayerColor player2 = (state.getChosenPlayersToHit().size() == 2) ?
      state.getChosenPlayersToHit().get(1) : null;

    if (player2 != null && state.isActivatedSecondExtraEffect()) {
      state.hitPlayer(player2, 2);
    }
  };

  @Override
  public boolean canConsume(String key, ObjectNode config) {
    return "cyberblade".equals(key);
  }

  @Override
  public List<ControllerFlowNode> getAdditionalNodes(String key, ObjectNode config) {
    return Arrays.asList(
      new ApplyBaseGunFlowNode(ControllerNodes.applyGunEffect("cyberblade", "base"), APPLY_BASE),
      new ApplyBaseGunFlowNode(ControllerNodes.applyGunEffect("cyberblade", "slice"), APPLY_SLICE));
  }

}
