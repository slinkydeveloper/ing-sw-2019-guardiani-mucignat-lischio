package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.model.fat.PlayerDashboard;
import com.adrenalinici.adrenaline.util.TriConsumer;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

public class ApplyBaseGunFlowNode implements ControllerFlowNode<BaseEffectGunFlowState> {
  public static String SKIP_IF_EXTRA_ON = "skip_if_extra_on";
  public static String FIRST_EXTRA_IS_ON = "first_extra_is_on";
  public static final String DEFAULT = "default";

  private String nodeId;
  private TriConsumer<BaseEffectGunFlowState, GameModel, ControllerFlowContext> consumer;

  public ApplyBaseGunFlowNode(String nodeId, TriConsumer<BaseEffectGunFlowState, GameModel, ControllerFlowContext> consumer) {
    this.nodeId = nodeId;
    this.consumer = consumer;
  }

  @Override
  public String id() {
    return nodeId;
  }

  @Override
  public void onJump(BaseEffectGunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    consumer.accept(flowState, model, context);

    if (!(resolveEffectRestriction(flowState).equals(SKIP_IF_EXTRA_ON) && flowState.isActivatedSecondExtraEffect())) {
      flowState.applyHitAndMarkPlayers(model, context);

      // Remove ammos required for effect and unload the gun
      PlayerDashboard dashboard = model.getPlayerDashboard(context.getTurnOfPlayer());
      if (flowState.isActivatedFirstExtraEffect())
        dashboard.removeAmmosIncludingPowerups(flowState.getChosenGun().getFirstExtraEffectCost());
      if (flowState.isActivatedSecondExtraEffect() && !resolveEffectRestriction(flowState).equals(FIRST_EXTRA_IS_ON)) {
        dashboard.removeAmmosIncludingPowerups(flowState.getChosenGun().getSecondExtraEffectCost());
      }

      dashboard.unloadGun(flowState.getChosenGun().getId());
    }
    context.nextPhase(view, flowState);
  }

  @Override
  public void handleEvent(ViewEvent event, BaseEffectGunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) { }

  private String resolveEffectRestriction(GunFlowState flowState) {
    if (!flowState.resolvePhaseConfiguration(id()).has("applying_restriction"))
      return DEFAULT;

    return flowState.resolvePhaseConfiguration(id()).get("applying_restriction").asText();
  }
}
