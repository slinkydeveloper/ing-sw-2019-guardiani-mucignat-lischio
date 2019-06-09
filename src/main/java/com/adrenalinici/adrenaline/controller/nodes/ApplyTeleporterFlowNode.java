package com.adrenalinici.adrenaline.controller.nodes;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.StatelessControllerFlowNode;
import com.adrenalinici.adrenaline.flow.impl.VoidState;
import com.adrenalinici.adrenaline.model.common.PowerUpType;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

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
      if (useTeleporterEvent.getChosenCard().getPowerUpType() != PowerUpType.TELEPORTER) return;
      if (!model.getPlayerDashboard(context.getTurnOfPlayer()).getPowerUpCards().contains(useTeleporterEvent.getChosenCard())) return;
      model.removePowerUpFromPlayer(context.getTurnOfPlayer(), useTeleporterEvent.getChosenCard());
      model.movePlayerInDashboard(useTeleporterEvent.getChosenPosition(), context.getTurnOfPlayer());
    });
  }

}
