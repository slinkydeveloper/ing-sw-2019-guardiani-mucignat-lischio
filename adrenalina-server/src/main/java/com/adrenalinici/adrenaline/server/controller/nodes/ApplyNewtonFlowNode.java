package com.adrenalinici.adrenaline.server.controller.nodes;

import com.adrenalinici.adrenaline.common.model.PowerUpType;
import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.StatelessControllerFlowNode;
import com.adrenalinici.adrenaline.server.flow.impl.VoidState;
import com.adrenalinici.adrenaline.server.model.GameModel;

public class ApplyNewtonFlowNode implements StatelessControllerFlowNode {

  @Override
  public String id() {
    return ControllerNodes.APPLY_NEWTON.name();
  }

  @Override
  public void onJump(VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) { }

  @Override
  public void handleEvent(ViewEvent event, VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onUseNewtonEvent(useNewtonEvent -> {
      // This node must assume that the actual player could not have a newton powerup, so a check is needed
      if (useNewtonEvent.getChosenCard().getPowerUpType() != PowerUpType.NEWTON) return;
      if (useNewtonEvent.getPlayer() == context.getTurnOfPlayer()) return;
      if (!model.getPlayerDashboard(context.getTurnOfPlayer()).getPowerUpCards().contains(useNewtonEvent.getChosenCard())) return;
      if (!model.getPlayers().contains(useNewtonEvent.getPlayer())) return;
      if (!model.getDashboard().calculateMovements(model.getPlayerPosition(useNewtonEvent.getPlayer()), 2).contains(useNewtonEvent.getChosenPosition())) return;
      model.movePlayerInDashboard(useNewtonEvent.getChosenPosition(), useNewtonEvent.getPlayer());
      model.removePowerUpFromPlayer(context.getTurnOfPlayer(), useNewtonEvent.getChosenCard());
    });
  }

}
