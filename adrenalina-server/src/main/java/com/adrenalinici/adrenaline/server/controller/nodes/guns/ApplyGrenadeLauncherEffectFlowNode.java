package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.server.model.GameModel;
import com.adrenalinici.adrenaline.server.model.PlayerDashboard;

public class ApplyGrenadeLauncherEffectFlowNode implements ControllerFlowNode<BaseEffectGunFlowState> {
  private boolean beenHere;

  public ApplyGrenadeLauncherEffectFlowNode() {
    this.beenHere = false;
  }

  @Override
  public String id() {
    return ControllerNodes.APPLY_GRENADE_LAUNCHER.name();
  }

  @Override
  public void onJump(BaseEffectGunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    if (!flowState.getChosenPlayersToHit().isEmpty()) {
      if (!flowState.isActivatedFirstExtraEffect() || beenHere) {
        flowState.getChosenPlayersToHit().forEach(
          p -> flowState.hitPlayer(p, 1)
        );

        flowState.applyHitAndMarkPlayers(model, context);

        // Remove ammos required for effect and unload the gun
        PlayerDashboard dashboard = model.getPlayerDashboard(context.getTurnOfPlayer());
        if (flowState.isActivatedFirstExtraEffect())
          dashboard.removeAmmosIncludingPowerups(flowState.getChosenGun().getFirstExtraEffectCost());

        dashboard.unloadGun(flowState.getChosenGun().getId());
        beenHere = false;
      } else beenHere = true;
    } else context.incrementRemainingActions();
    context.nextPhase(view, flowState);
  }

  @Override
  public void handleEvent(ViewEvent event, BaseEffectGunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {

  }
}
