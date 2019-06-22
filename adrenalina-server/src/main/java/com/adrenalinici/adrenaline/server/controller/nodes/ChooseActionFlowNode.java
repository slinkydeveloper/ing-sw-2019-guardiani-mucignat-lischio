package com.adrenalinici.adrenaline.server.controller.nodes;

import com.adrenalinici.adrenaline.common.model.Action;
import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.StatelessControllerFlowNode;
import com.adrenalinici.adrenaline.server.flow.impl.VoidState;
import com.adrenalinici.adrenaline.server.model.GameModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes.*;

public class ChooseActionFlowNode implements StatelessControllerFlowNode {

  private static final List<Action> FRENZY_MODE_BEFORE_FIRST_PLAYER_ACTIONS = Arrays.asList(Action.MOVE_MOVE_MOVE_MOVE, Action.MOVE_MOVE_PICKUP);
  private static final List<Action> FRENZY_MODE_AFTER_FIRST_PLAYER_ACTIONS = Collections.singletonList(Action.MOVE_MOVE_MOVE_PICKUP);
  private static final List<Action> NO_ADRENALINE_ACTIONS = Arrays.asList(Action.MOVE_MOVE_MOVE, Action.MOVE_PICKUP);
  private static final List<Action> ONE_ADRENALINE_ACTIONS = Arrays.asList(Action.MOVE_MOVE_MOVE, Action.MOVE_PICKUP, Action.MOVE_MOVE_PICKUP);
  private static final List<Action> TWO_ADRENALINE_ACTIONS = Arrays.asList(Action.MOVE_MOVE_MOVE, Action.MOVE_PICKUP, Action.MOVE_MOVE_PICKUP);

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
      view.showAvailableActions(calculateAvailableActions(model, context));
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

  private List<Action> calculateAvailableActions(GameModel model, ControllerFlowContext context) {
    List<Action> actions;
    if (context.isFrenzyMode()) {
      if (context.isFirstPlayerOrAfterFirstPlayerInFrenzyMode()) {
        actions = new ArrayList<>(FRENZY_MODE_AFTER_FIRST_PLAYER_ACTIONS);
        if (model.getPlayerDashboard(context.getTurnOfPlayer()).getLoadedGuns().isEmpty())
          return actions;
        actions.add(Action.MOVE_MOVE_RELOAD_SHOOT);
        return actions;
      } else {
        actions = new ArrayList<>(FRENZY_MODE_BEFORE_FIRST_PLAYER_ACTIONS);
        if (model.getPlayerDashboard(context.getTurnOfPlayer()).getLoadedGuns().isEmpty())
          return actions;
        actions.add(Action.MOVE_RELOAD_SHOOT);
        return actions;
      }
    }
    int thisPlayerDamages = model.getPlayerDashboard(context.getTurnOfPlayer()).getDamages().size();
    if (thisPlayerDamages < 3) {
      actions = new ArrayList<>(NO_ADRENALINE_ACTIONS);
      if (model.getPlayerDashboard(context.getTurnOfPlayer()).getLoadedGuns().isEmpty())
        return actions;
      actions.add(Action.SHOOT);
      return actions;

    } else if (thisPlayerDamages < 6) {
      actions = new ArrayList<>(ONE_ADRENALINE_ACTIONS);
      if (model.getPlayerDashboard(context.getTurnOfPlayer()).getLoadedGuns().isEmpty())
        return actions;
      actions.add(Action.SHOOT);
      return actions;
    } else {
      actions = new ArrayList<>(TWO_ADRENALINE_ACTIONS);
      if (model.getPlayerDashboard(context.getTurnOfPlayer()).getLoadedGuns().isEmpty())
        return actions;
      actions.add(Action.SHOOT);
      actions.add(Action.MOVE_SHOOT);
      return actions;
    }
  }
}
