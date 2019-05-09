package com.adrenalinici.adrenaline.controller.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.controller.nodes.guns.AlternativeEffectGunFlowState;
import com.adrenalinici.adrenaline.controller.nodes.guns.ApplyAlternativeGunFlowNode;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.util.TriConsumer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Arrays;
import java.util.List;

import static com.adrenalinici.adrenaline.controller.nodes.ControllerNodes.applyGunEffect;

public class HellionGunFactory extends AlternativeEffectGunFactory {
  private static final TriConsumer<AlternativeEffectGunFlowState, GameModel, ControllerFlowContext> BASE_EFFECT_APPLY = (state, model, context) -> {
    state.getChosenPlayersToHit().forEach(victim -> {
      boolean killed = model.hitAndMarkPlayer(context.getTurnOfPlayer(), victim, 1, 1);
      if (killed) context.getKilledPlayers().add(victim);
      state.getHitPlayers().add(victim);

      model.getDashboard().getDashboardCell(model.getPlayerPosition(victim))
        .getPlayersInCell()
        .stream()
        .filter(otherPlayer -> !otherPlayer.equals(victim))
        .forEach(p -> model.markPlayer(context.getTurnOfPlayer(), p, 1));
    });
  };

  private static final TriConsumer<AlternativeEffectGunFlowState, GameModel, ControllerFlowContext> NANOTRACER_EFFECT_APPLY = (state, model, context) -> {
    state.getChosenPlayersToHit().forEach(victim -> {
      boolean killed = model.hitAndMarkPlayer(context.getTurnOfPlayer(), victim, 1, 2);
      if (killed) context.getKilledPlayers().add(victim);
      state.getHitPlayers().add(victim);

      model.getDashboard().getDashboardCell(model.getPlayerPosition(victim))
        .getPlayersInCell()
        .stream()
        .filter(otherPlayer -> !otherPlayer.equals(victim))
        .forEach(p -> model.markPlayer(context.getTurnOfPlayer(), p, 2));
    });
  };

  @Override
  public boolean canConsume(String key, ObjectNode config) {
    return "hellion".equals(key);
  }

  @Override
  public List<ControllerFlowNode> getAdditionalNodes(String key, ObjectNode config) {
    return Arrays.asList(
      new ApplyAlternativeGunFlowNode(applyGunEffect("hellion", "base"), BASE_EFFECT_APPLY),
      new ApplyAlternativeGunFlowNode(applyGunEffect("hellion", "nanoTracer"), NANOTRACER_EFFECT_APPLY)
    );

  }
}
