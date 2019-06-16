package com.adrenalinici.adrenaline.server.controller.guns;

import com.adrenalinici.adrenaline.common.model.Position;
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

public class FlamethrowerGunFactory extends AlternativeEffectGunFactory {

  private static final TriConsumer<AlternativeEffectGunFlowState, GameModel, ControllerFlowContext> BASE_EFFECT_APPLY = (state, model, context) -> {
    state.getChosenPlayersToHit().forEach(p -> {
      state.hitPlayer(p, 1);
    });
  };

  private static final TriConsumer<AlternativeEffectGunFlowState, GameModel, ControllerFlowContext> BARBECUE_EFFECT_APPLY = (state, model, context) -> {
    Position cell1 = !state.getChosenCellsToHit().isEmpty() ?
      state.getChosenCellsToHit().get(0) : null;

    Position cell2 = state.getChosenCellsToHit().size() == 2 ?
      state.getChosenCellsToHit().get(1) : null;

    if (cell1 != null) {
      model.
        getDashboard()
        .getDashboardCell(cell1)
        .getPlayersInCell()
        .forEach(p -> state.hitPlayer(p, 2));
    }

    if (cell1 != null && cell2 != null) {
      model.
        getDashboard()
        .getDashboardCell(cell2)
        .getPlayersInCell()
        .forEach(p -> state.hitPlayer(p, 1));
    }
  };

  @Override
  public boolean canConsume(String key, ObjectNode config) {
    return "flamethrower".equals(key);
  }

  @Override
  public List<ControllerFlowNode> getAdditionalNodes(String key, ObjectNode config) {
    return Arrays.asList(
      new ApplyAlternativeGunFlowNode(applyGunEffect("flamethrower", "base"), BASE_EFFECT_APPLY),
      new ApplyAlternativeGunFlowNode(applyGunEffect("flamethrower", "barbecue"), BARBECUE_EFFECT_APPLY)
    );
  }
}
