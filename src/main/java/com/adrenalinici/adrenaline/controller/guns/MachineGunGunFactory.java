package com.adrenalinici.adrenaline.controller.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.controller.nodes.guns.ApplyBaseGunFlowNode;
import com.adrenalinici.adrenaline.controller.nodes.guns.BaseEffectGunFlowState;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.util.TriConsumer;
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
      gameModel.hitPlayer(
        context.getTurnOfPlayer(),
        firstPlayer,
        state.isActivatedFirstExtraEffect() ? 2 : 1
      );
    }
    if (secondPlayer != null) {
      gameModel.hitPlayer(
        context.getTurnOfPlayer(),
        secondPlayer,
        state.isActivatedSecondExtraEffect() ? 2 : 1
      );
    }
    if (state.isActivatedSecondExtraEffect() && thirdPlayer != null) {
      gameModel.hitPlayer(context.getTurnOfPlayer(), thirdPlayer, 1);
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
