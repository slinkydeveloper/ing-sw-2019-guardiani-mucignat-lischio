package com.adrenalinici.adrenaline.server.controller.nodes;

import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.StatelessControllerFlowNode;
import com.adrenalinici.adrenaline.server.flow.impl.VoidState;
import com.adrenalinici.adrenaline.server.model.GameModel;

import static com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes.CHOOSE_ACTION;
import static com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes.START_TURN;

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
        int remainingActions = calculateRemainingActions(context);
        context.setRemainingActions(remainingActions);

        context.addPhases(CHOOSE_ACTION.name());
        context.nextPhase(view);
      }
    );
  }

  private int calculateRemainingActions(ControllerFlowContext context) {
    return context.isFrenzyMode() && context.isFirstPlayerOrAfterFirstPlayerInFrenzyMode() ? 1 : 2;
  }
}
