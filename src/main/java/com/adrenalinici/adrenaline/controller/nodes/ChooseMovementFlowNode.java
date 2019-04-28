package com.adrenalinici.adrenaline.controller.nodes;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.StatelessControllerFlowNode;
import com.adrenalinici.adrenaline.flow.impl.VoidState;
import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.model.Position;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import static com.adrenalinici.adrenaline.controller.nodes.ControllerNodes.movement;

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
