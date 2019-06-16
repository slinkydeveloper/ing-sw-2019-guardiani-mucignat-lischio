package com.adrenalinici.adrenaline.server.controller.nodes;

import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.StatelessControllerFlowNode;
import com.adrenalinici.adrenaline.server.flow.impl.VoidState;
import com.adrenalinici.adrenaline.server.model.GameModel;

import static com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes.FIRST_TURN;
import static com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes.RESPAWN_KILLED_PEOPLE;

public class FirstTurnFlowNode implements StatelessControllerFlowNode {

  @Override
  public String id() {
    return FIRST_TURN.name();
  }

  @Override
  public void onJump(VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    model.getPlayers().forEach(model::acquirePowerUpCard);
    context.getKilledPlayers().addAll(model.getPlayers());
    context.addPhases(RESPAWN_KILLED_PEOPLE.name());
    context.nextPhase(view);
  }

  @Override
  public void handleEvent(ViewEvent event, VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) { }
}
