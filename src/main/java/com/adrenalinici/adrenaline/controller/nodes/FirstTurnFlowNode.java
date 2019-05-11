package com.adrenalinici.adrenaline.controller.nodes;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.StatelessControllerFlowNode;
import com.adrenalinici.adrenaline.flow.impl.VoidState;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.stream.IntStream;

import static com.adrenalinici.adrenaline.controller.nodes.ControllerNodes.*;

public class FirstTurnFlowNode implements StatelessControllerFlowNode {

  @Override
  public String id() {
    return FIRST_TURN.name();
  }

  @Override
  public void onJump(VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) { }

  @Override
  public void handleEvent(ViewEvent event, VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onNewTurnEvent(
      e -> {
        model.getPlayers().forEach(model::acquirePowerUpCard);

        context.getKilledPlayers().addAll(model.getPlayers());

        context.addPhases(RESPAWN_KILLED_PEOPLE.name());
        context.nextPhase(view);
      }
    );
  }
}
