package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.model.PlayerDashboard;
import com.adrenalinici.adrenaline.util.TriConsumer;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

public class ApplyBaseGunFlowNode implements ControllerFlowNode<BaseEffectGunFlowState> {

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
    // Remove ammos required for effect and unload the gun
    PlayerDashboard dashboard = model.getPlayerDashboard(context.getTurnOfPlayer());
    if (flowState.isActivatedFirstExtraEffect())
      dashboard.removeAmmosIncludingPowerups(flowState.getChosenGun().getFirstExtraEffectCost());
    if (flowState.isActivatedSecondExtraEffect())
      dashboard.removeAmmosIncludingPowerups(flowState.getChosenGun().getSecondExtraEffectCost());
    dashboard.removeLoadedGun(flowState.getChosenGun().get());
    dashboard.addUnloadedGun(flowState.getChosenGun().get());
    context.nextPhase(view, flowState);
  }

  @Override
  public void handleEvent(ViewEvent event, BaseEffectGunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
  }
}
