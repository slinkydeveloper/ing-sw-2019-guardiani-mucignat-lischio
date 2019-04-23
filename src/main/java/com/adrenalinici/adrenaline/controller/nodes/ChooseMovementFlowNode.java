package com.adrenalinici.adrenaline.controller.nodes;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.flow.impl.VoidState;
import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.model.Position;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import static com.adrenalinici.adrenaline.controller.nodes.ControllerNodes.MOVEMENT;

public class ChooseMovementFlowNode implements ControllerFlowNode<VoidState> {

  @Override
  public String id() {
    return MOVEMENT.name();
  }

  @Override
  public void onJump(VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    Position actualPlayerPosition = model.getDashboard().getPlayersPositions().get(context.getTurnOfPlayer());
    view.showAvailableMovements(
      model.getDashboard().calculateMovements(actualPlayerPosition, 3)
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
