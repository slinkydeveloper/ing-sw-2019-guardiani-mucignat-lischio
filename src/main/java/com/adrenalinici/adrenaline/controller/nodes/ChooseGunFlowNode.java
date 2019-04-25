package com.adrenalinici.adrenaline.controller.nodes;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.DecoratedAlternativeEffectGun;
import com.adrenalinici.adrenaline.controller.StatelessControllerFlowNode;
import com.adrenalinici.adrenaline.controller.nodes.guns.AlternativeEffectGunFlowState;
import com.adrenalinici.adrenaline.flow.impl.VoidState;
import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

public class ChooseGunFlowNode implements StatelessControllerFlowNode {
  @Override
  public String id() {
    return ControllerNodes.CHOOSE_GUN.name();
  }

  @Override
  public void onJump(VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    view.showAvailableGunsToPickup(model.getPlayerDashboard(context.getTurnOfPlayer()).getLoadedGuns());
  }

  @Override
  public void handleEvent(ViewEvent event, VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onGunChosenEvent(gunChosenEvent -> {
      context
        .getGunLoader()
        .getModelGun(gunChosenEvent.getGunid())
        .visit(alternativeEffectGun -> {
        DecoratedAlternativeEffectGun decorated = (DecoratedAlternativeEffectGun) context
          .getGunLoader()
          .getDecoratedGun(alternativeEffectGun.getId());
        context.addPhasesToHead(decorated.getPhases().toArray(new String[0]));
        context.nextPhase(view, new AlternativeEffectGunFlowState(decorated));
      }, baseEffectGun -> {
        //TODO P1
      });
    });
  }
}
