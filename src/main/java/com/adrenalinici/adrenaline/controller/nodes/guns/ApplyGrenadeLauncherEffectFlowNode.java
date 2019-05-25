package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.model.fat.PlayerDashboard;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

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
    context.nextPhase(view, flowState);
  }

  @Override
  public void handleEvent(ViewEvent event, BaseEffectGunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {

  }
}
