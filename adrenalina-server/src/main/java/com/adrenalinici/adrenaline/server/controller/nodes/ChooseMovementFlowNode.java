package com.adrenalinici.adrenaline.server.controller.nodes;

import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.StatelessControllerFlowNode;
import com.adrenalinici.adrenaline.server.flow.impl.VoidState;
import com.adrenalinici.adrenaline.server.model.GameModel;

import static com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes.movement;

public class ChooseMovementFlowNode implements StatelessControllerFlowNode {

  int distance;

  public ChooseMovementFlowNode(int distance) {
    this.distance = distance;
  }

  @Override
  public String id() {
    return movement(distance);
  }

  @Override
  public void onJump(VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    Position actualPlayerPosition = model.getDashboard().getPlayersPositions().get(context.getTurnOfPlayer());
    view.showAvailableMovements(
      model.getDashboard().calculateMovements(actualPlayerPosition, distance)
    );
  }

  @Override
  public void handleEvent(ViewEvent event, VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onMovementChosenEvent(
      e -> {
        model.movePlayerInDashboard(e.getCoordinates(), context.getTurnOfPlayer());
        context.nextPhase(view);
      }
    );
  }
}
