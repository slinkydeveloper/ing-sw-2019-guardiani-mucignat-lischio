package com.adrenalinici.adrenaline.controller.nodes;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.StatelessControllerFlowNode;
import com.adrenalinici.adrenaline.flow.impl.VoidState;
import com.adrenalinici.adrenaline.model.common.PowerUpType;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

public class ApplyNewtonFlowNode implements StatelessControllerFlowNode {

  @Override
  public String id() {
    return ControllerNodes.APPLY_TELEPORT.name();
  }

  @Override
  public void onJump(VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) { }

  @Override
  public void handleEvent(ViewEvent event, VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onUseNewtonEvent(useNewtonEvent -> {
      // This node must assume that the actual player could not have a newton powerup, so a check is needed
      if (useNewtonEvent.getChosenCard().getPowerUpType() != PowerUpType.NEWTON) return;
      if (!model.getPlayerDashboard(context.getTurnOfPlayer()).getPowerUpCards().contains(useNewtonEvent.getChosenCard())) return;
      if (!model.getPlayers().contains(useNewtonEvent.getPlayer())) return;
      if (!model.getDashboard().calculateMovements(model.getPlayerPosition(useNewtonEvent.getPlayer()), 2).contains(useNewtonEvent.getChosenPosition())) return;
      model.movePlayerInDashboard(useNewtonEvent.getChosenPosition(), useNewtonEvent.getPlayer());
      model.removePowerUpFromPlayer(context.getTurnOfPlayer(), useNewtonEvent.getChosenCard());
    });
  }

}
