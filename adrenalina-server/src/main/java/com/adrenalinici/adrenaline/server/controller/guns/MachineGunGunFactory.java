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

public class MachineGunGunFactory extends BaseEffectGunFactory {

  private static final TriConsumer<BaseEffectGunFlowState, GameModel, ControllerFlowContext> APPLY = (state, gameModel, context) -> {
    PlayerColor firstPlayer = (!state.getChosenPlayersToHit().isEmpty()) ?
      state.getChosenPlayersToHit().get(0) : null;
    PlayerColor secondPlayer = (state.getChosenPlayersToHit().size() >= 2) ?
      state.getChosenPlayersToHit().get(1) : null;
    PlayerColor thirdPlayer = (state.getChosenPlayersToHit().size() == 3) ?
      state.getChosenPlayersToHit().get(2) : null;
    if (firstPlayer != null) {
      state.hitPlayer(firstPlayer, state.isActivatedFirstExtraEffect() ? 2 : 1);
    }
    if (secondPlayer != null) {
      state.hitPlayer(secondPlayer, state.isActivatedSecondExtraEffect() ? 2 : 1);
    }
    if (state.isActivatedSecondExtraEffect() && thirdPlayer != null) {
      state.hitPlayer(thirdPlayer, 1);
    }
  };

  @Override
  public boolean canConsume(String key, ObjectNode config) {
    return "machine_gun".equals(key);
  }

  @Override
  public List<ControllerFlowNode> getAdditionalNodes(String key, ObjectNode config) {
    return Collections.singletonList(
      new ApplyBaseGunFlowNode(ControllerNodes.applyGunEffect("machine_gun", "base"), APPLY)
    );
  }
}
