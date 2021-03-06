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

public class RocketLauncherGunFactory extends BaseEffectGunFactory {
  private static final TriConsumer<BaseEffectGunFlowState, GameModel, ControllerFlowContext> APPLY = (state, gameModel, context) -> {
    PlayerColor player = (!state.getChosenPlayersToHit().isEmpty()) ?
      state.getChosenPlayersToHit().get(0) : null;

    if (player != null) {
      state.hitPlayer(player, 2);
      if (state.isActivatedSecondExtraEffect()) {
        gameModel.getDashboard()
          .getDashboardCell(gameModel.getPlayerPosition(player))
          .getPlayersInCell()
          .forEach(p -> state.hitPlayer(p, 1));
      }
    }
  };

  @Override
  public boolean canConsume(String key, ObjectNode config) {
    return "rocket_launcher".equals(key);
  }

  @Override
  public List<ControllerFlowNode> getAdditionalNodes(String key, ObjectNode config) {
    return Collections.singletonList(new ApplyBaseGunFlowNode(ControllerNodes.applyGunEffect("rocket_launcher", "base"), APPLY));
  }
}
