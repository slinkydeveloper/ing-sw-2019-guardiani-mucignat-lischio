package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.common.util.TriConsumer;
import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.server.model.GameModel;
import com.adrenalinici.adrenaline.server.model.PlayerDashboard;

public class ApplyAlternativeGunFlowNode implements ControllerFlowNode<AlternativeEffectGunFlowState> {

  private String nodeId;
  private TriConsumer<AlternativeEffectGunFlowState, GameModel, ControllerFlowContext> consumer;

  public ApplyAlternativeGunFlowNode(String nodeId, TriConsumer<AlternativeEffectGunFlowState, GameModel, ControllerFlowContext> consumer) {
    this.nodeId = nodeId;
    this.consumer = consumer;
  }

  @Override
  public String id() {
    return nodeId;
  }

  @Override
  public void onJump(AlternativeEffectGunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    if (!flowState.getChosenPlayersToHit().isEmpty()) {
      consumer.accept(flowState, model, context);

      flowState.applyHitAndMarkPlayers(model, context);

      // Remove ammos required for effect and unload the gun
      PlayerDashboard dashboard = model.getPlayerDashboard(context.getTurnOfPlayer());

      dashboard.removeAmmosIncludingPowerups(flowState.getChosenEffect().getRequiredAmmos());

      dashboard.unloadGun(flowState.getChosenGun().getId());
    } else context.incrementRemainingActions();
    context.nextPhase(view, flowState);
  }

  @Override
  public void handleEvent(ViewEvent event, AlternativeEffectGunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
  }
}
