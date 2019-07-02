package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.common.util.TriConsumer;
import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.server.model.GameModel;
import com.adrenalinici.adrenaline.server.model.PlayerDashboard;

/**
 * This class represents the flow node which effectively applies damages
 * and marks of a BaseEffectGun. It removes the required ammo according to the effect and unloads the gun.
 * It also manages the case in which the various effects
 * of the gun are dependent on each other. These restriction are represented with two flags:
 * <p>
 * SKIP_IF_EXTRA_ON -> skip the applier if an extra effect is selected, so the node is executed again,
 * when configuration is completed.
 * <p>
 * FIRST_EXTRA_IS_ON -> second extra effect needs also the first one to be applied.
 * <p>
 * It also removes the required ammo according to the effect and unloads the gun.
 */
public class ApplyBaseGunFlowNode implements ControllerFlowNode<BaseEffectGunFlowState> {
  private static final String SKIP_IF_EXTRA_ON = "skip_if_extra_on";
  private static final String FIRST_EXTRA_IS_ON = "first_extra_is_on";
  private static final String DEFAULT = "default";

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

    if (!(flowState.getHitPlayers().isEmpty() && flowState.getMarkPlayers().isEmpty())) {

      if (!(resolveEffectRestriction(flowState).equals(SKIP_IF_EXTRA_ON) && flowState.isActivatedSecondExtraEffect())) {
        flowState.applyHitAndMarkPlayers(model, context);

        // Remove ammos required for effect and unload the gun
        PlayerDashboard dashboard = model.getPlayerDashboard(context.getTurnOfPlayer());
        if (flowState.isActivatedFirstExtraEffect()) {
          model.putPowerUpsBackInDeck(
            dashboard.removeAmmosIncludingPowerups(flowState.getChosenGun().getFirstExtraEffectCost())
          );
        }
        if (flowState.isActivatedSecondExtraEffect() && !resolveEffectRestriction(flowState).equals(FIRST_EXTRA_IS_ON)) {
          model.putPowerUpsBackInDeck(
            dashboard.removeAmmosIncludingPowerups(flowState.getChosenGun().getSecondExtraEffectCost())
          );
        }

        model.unloadGun(context.getTurnOfPlayer(), flowState.getChosenGun().getId());
      }
    } else context.incrementRemainingActions();
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
