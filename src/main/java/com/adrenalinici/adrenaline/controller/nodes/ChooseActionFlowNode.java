package com.adrenalinici.adrenaline.controller.nodes;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.StatelessControllerFlowNode;
import com.adrenalinici.adrenaline.flow.impl.VoidState;
import com.adrenalinici.adrenaline.model.common.Action;
import com.adrenalinici.adrenaline.model.fat.GameModel;
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
    if (context.getRemainingActions() == 0) {
      context.addPhases(RELOAD.name(), RESPAWN_KILLED_PEOPLE.name());
      context.nextPhase(view);
    } else
      view.showAvailableActions(calculateAvailableActions(context));
  }

  @Override
  public void handleEvent(ViewEvent event, VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onActionChosenEvent(
      e -> {
        context.decrementRemainingActions();
        switch (e.getAction()) {
          case MOVE_MOVE_MOVE:
            context.addPhases(movement(3), id());
            break;
          case MOVE_PICKUP:
            context.addPhases(movement(1), PICKUP.name(), id());
            break;
          case SHOOT:
            context.addPhases(CHOOSE_GUN.name(), USE_SCOPE.name());
            context.addPhasesToEnd(USE_TAGBACK_GRENADE.name(), id());
            break;
          case MOVE_MOVE_PICKUP:
            context.addPhases(movement(2), PICKUP.name(), id());
            break;
          case MOVE_SHOOT:
            context.addPhases(movement(1), CHOOSE_GUN.name(), USE_SCOPE.name());
            context.addPhasesToEnd(USE_TAGBACK_GRENADE.name(), id());
            break;
          case MOVE_RELOAD_SHOOT:
            context.addPhases(movement(1), RELOAD.name(), CHOOSE_GUN.name(), USE_SCOPE.name());
            context.addPhasesToEnd(USE_TAGBACK_GRENADE.name(), id());
            break;
          case MOVE_MOVE_MOVE_MOVE:
            context.addPhases(movement(4), id());
            break;
          case MOVE_MOVE_RELOAD_SHOOT:
            context.addPhases(movement(2), RELOAD.name(), CHOOSE_GUN.name(), USE_SCOPE.name());
            context.addPhasesToEnd(USE_TAGBACK_GRENADE.name(), id());
            break;
          case MOVE_MOVE_MOVE_PICKUP:
            context.addPhases(movement(3), PICKUP.name(), id());
            break;
        }
        context.nextPhase(view);
      }
    );
  }

  private List<Action> calculateAvailableActions(ControllerFlowContext context) {
    if (context.isFrenzyMode()) {
      if (context.isFirstPlayerOrAfterFirstPlayerInFrenzyMode()) {
        return Arrays.asList(Action.MOVE_MOVE_RELOAD_SHOOT, Action.MOVE_MOVE_MOVE_PICKUP);
      } else {
        return Arrays.asList(Action.MOVE_RELOAD_SHOOT, Action.MOVE_MOVE_MOVE_MOVE, Action.MOVE_MOVE_PICKUP);
      }
    }
    return Arrays.asList(Action.MOVE_MOVE_MOVE, Action.MOVE_PICKUP, Action.SHOOT);
  }
}
