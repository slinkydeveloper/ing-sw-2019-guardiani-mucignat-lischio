package com.adrenalinici.adrenaline.controller.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.controller.nodes.guns.AlternativeEffectGunFlowState;
import com.adrenalinici.adrenaline.controller.nodes.guns.ApplyAlternativeGunFlowNode;
import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.util.TriConsumer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Arrays;
import java.util.List;

import static com.adrenalinici.adrenaline.controller.nodes.ControllerNodes.applyGunEffect;

public class ZX2GunFactory extends AlternativeEffectGunFactory {

  private static final TriConsumer<AlternativeEffectGunFlowState, GameModel, ControllerFlowContext> BASE_EFFECT_APPLY = (state, model, context) -> {
    state.getChosenPlayersToHit().forEach(p -> {
      boolean killed = model.hitAndMarkPlayer(context.getTurnOfPlayer(), p, 1, 2);
      if (killed) context.getKilledPlayers().add(p);
      state.getHitPlayers().add(p);
    });
  };

  private static final TriConsumer<AlternativeEffectGunFlowState, GameModel, ControllerFlowContext> SCANNER_EFFECT_APPLY = (state, model, context) -> {
    state.getChosenPlayersToHit().forEach(p -> model.markPlayer(context.getTurnOfPlayer(), p, 1));
  };

  @Override
  public boolean canConsume(String key, ObjectNode config) {
    return "zx2".equals(key);
  }

  @Override
  public List<ControllerFlowNode> getAdditionalNodes(String key, ObjectNode config) {
    return Arrays.asList(
      new ApplyAlternativeGunFlowNode(applyGunEffect("zx2", "base"), BASE_EFFECT_APPLY),
      new ApplyAlternativeGunFlowNode(applyGunEffect("zx2", "scanner"), SCANNER_EFFECT_APPLY)
    );
  }
}
