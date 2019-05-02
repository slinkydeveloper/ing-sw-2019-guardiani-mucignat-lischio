package com.adrenalinici.adrenaline.controller.nodes;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.StatelessControllerFlowNode;
import com.adrenalinici.adrenaline.flow.impl.VoidState;
import com.adrenalinici.adrenaline.model.Action;
import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.Arrays;
import java.util.List;

import static com.adrenalinici.adrenaline.controller.nodes.ControllerNodes.*;

public class ChooseActionFlowNode implements StatelessControllerFlowNode {

  @Override
  public String id() {
    return CHOOSE_ACTION.name();
  }

  @Override
  public void onJump(VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    if (context.getRemainingActions() == 0)
      context.nextPhase(view);
    else
      view.showAvailableActions(calculateAvailableActions());
  }

  @Override
  public void handleEvent(ViewEvent event, VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onActionChosenEvent(
      e -> {
        context.decrementRemainingActions();
        switch (e.getAction()) {
          case MOVE_MOVE_MOVE:
            context.addPhases(movement(3));
            break;
          case MOVE_PICKUP:
            context.addPhases(movement(1), PICKUP.name());
            break;
        }
        context.nextPhase(view);
      }
    );
  }

  private List<Action> calculateAvailableActions() {
    //TODO P2 based on turn of player
    return Arrays.asList(Action.MOVE_MOVE_MOVE, Action.MOVE_PICKUP, Action.SHOOT);
  }
}
