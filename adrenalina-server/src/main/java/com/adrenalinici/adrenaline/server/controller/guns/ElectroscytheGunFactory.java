package com.adrenalinici.adrenaline.server.controller.guns;

import com.adrenalinici.adrenaline.common.util.TriConsumer;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.AlternativeEffectGunFlowState;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.ApplyAlternativeGunFlowNode;
import com.adrenalinici.adrenaline.server.model.GameModel;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes.applyGunEffect;

public class ElectroscytheGunFactory extends AlternativeEffectGunFactory {
  private static final TriConsumer<AlternativeEffectGunFlowState, GameModel, ControllerFlowContext> BASE_EFFECT_APPLY = (state, model, context) -> {
    new ArrayList<>(model.getDashboard()
      .getDashboardCell(model.getPlayerPosition(context.getTurnOfPlayer()))
      .getPlayersInCell())
      .stream()
      .filter(playerColor -> !context.getTurnOfPlayer().equals(playerColor))
      .forEach(victim -> {
        state.hitPlayer(victim, 1);
        }
      );
  };

  private static final TriConsumer<AlternativeEffectGunFlowState, GameModel, ControllerFlowContext> REAPER_EFFECT_APPLY = (state, model, context) -> {
    new ArrayList<>(model.getDashboard()
      .getDashboardCell(model.getPlayerPosition(context.getTurnOfPlayer()))
      .getPlayersInCell())
      .stream()
      .filter(playerColor -> !context.getTurnOfPlayer().equals(playerColor))
      .forEach(victim -> {
        state.hitPlayer(victim, 2);
        }
      );
  };

  @Override
  public boolean canConsume(String key, ObjectNode config) {
    return "electroscythe".equals(key);
  }

  @Override
  public List<ControllerFlowNode> getAdditionalNodes(String key, ObjectNode config) {
    return Arrays.asList(
      new ApplyAlternativeGunFlowNode(applyGunEffect("electroscythe", "base"), BASE_EFFECT_APPLY),
      new ApplyAlternativeGunFlowNode(applyGunEffect("electroscythe", "reaper"), REAPER_EFFECT_APPLY)
    );
  }
}
