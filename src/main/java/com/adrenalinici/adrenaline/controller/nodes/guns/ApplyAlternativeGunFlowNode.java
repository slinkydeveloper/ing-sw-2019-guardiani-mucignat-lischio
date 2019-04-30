package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.model.AmmoColor;
import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.model.PlayerDashboard;
import com.adrenalinici.adrenaline.util.Bag;
import com.adrenalinici.adrenaline.util.TriConsumer;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

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
    consumer.accept(flowState, model, context);
    // Remove ammos required for effect and unload the gun
    PlayerDashboard dashboard = model.getPlayerDashboard(context.getTurnOfPlayer());
    Bag<AmmoColor> playerAmmosBag = Bag.from(dashboard.getAmmos());
    Bag<AmmoColor> requiredAmmosBag = Bag.from(flowState.getChosenEffect().getRequiredAmmos());

    dashboard.removeAmmosIncludingPowerups(flowState.getChosenEffect().getRequiredAmmos());

    dashboard.removeLoadedGun(flowState.getChosenGun().get());
    dashboard.addUnloadedGun(flowState.getChosenGun().get());
    context.nextPhase(view, flowState);
  }

  @Override
  public void handleEvent(ViewEvent event, AlternativeEffectGunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
  }
}
