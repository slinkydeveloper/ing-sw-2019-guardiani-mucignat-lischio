package com.adrenalinici.adrenaline.controller.nodes;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.StatelessControllerFlowNode;
import com.adrenalinici.adrenaline.flow.impl.VoidState;
import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.stream.IntStream;

import static com.adrenalinici.adrenaline.controller.nodes.ControllerNodes.*;

public class NewTurnFlowNode implements StatelessControllerFlowNode {

  @Override
  public String id() {
    return START_TURN.name();
  }

  @Override
  public void onJump(VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) {
  }

  @Override
  public void handleEvent(ViewEvent event, VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onNewTurnEvent(
      e -> {
        int remainingActions = calculateRemainingActions();
        context.setRemainingActions(remainingActions);

        // I add one CHOOSE_ACTION for each remaining actions
        IntStream.range(0, remainingActions).forEach(i -> context.addPhasesToHead(CHOOSE_ACTION.name()));

        context.addPhasesToEnd(RELOAD.name(), RESPAWN_KILLED_PEOPLE.name());

        context.nextPhase(view);
      }
    );
  }

  private int calculateRemainingActions() {
    //TODO based on turn of player
    return 2;
  }
}
