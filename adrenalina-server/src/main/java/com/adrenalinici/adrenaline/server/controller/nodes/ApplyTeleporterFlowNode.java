package com.adrenalinici.adrenaline.server.controller.nodes;

import com.adrenalinici.adrenaline.common.model.PowerUpType;
import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.StatelessControllerFlowNode;
import com.adrenalinici.adrenaline.server.flow.impl.VoidState;
import com.adrenalinici.adrenaline.server.model.GameModel;

public class ApplyTeleporterFlowNode implements StatelessControllerFlowNode {

  @Override
  public String id() {
    return ControllerNodes.APPLY_TELEPORT.name();
  }

  @Override
  public void onJump(VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) { }

  @Override
  public void handleEvent(ViewEvent event, VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    // This node must assume that the actual player could not have a teleport powerup, so a check is needed
    event.onUseTeleporterEvent(useTeleporterEvent -> {
      if (useTeleporterEvent.getChosenCard().getPowerUpType() != PowerUpType.TELEPORTER ||
        useTeleporterEvent.getChosenCard() == null) return;
      if (!model.getPlayerDashboard(context.getTurnOfPlayer()).getPowerUpCards().contains(useTeleporterEvent.getChosenCard())) return;
      model.removePowerUpFromPlayer(context.getTurnOfPlayer(), useTeleporterEvent.getChosenCard());
      model.movePlayerInDashboard(useTeleporterEvent.getChosenPosition(), context.getTurnOfPlayer());
    });
  }

}
