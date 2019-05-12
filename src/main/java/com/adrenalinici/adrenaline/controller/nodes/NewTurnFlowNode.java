package com.adrenalinici.adrenaline.controller.nodes;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.StatelessControllerFlowNode;
import com.adrenalinici.adrenaline.flow.impl.VoidState;
import com.adrenalinici.adrenaline.model.fat.GameModel;
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

        context.addPhases(CHOOSE_ACTION.name());
        context.nextPhase(view);
      }
    );
  }

  private int calculateRemainingActions() {
    //TODO based on turn of player
    return 2;
  }
}
