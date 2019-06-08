package com.adrenalinici.adrenaline.controller.nodes;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.StatelessControllerFlowNode;
import com.adrenalinici.adrenaline.flow.impl.VoidState;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.Position;
import com.adrenalinici.adrenaline.model.common.PowerUpCard;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NewtonFlowNode implements StatelessControllerFlowNode {

  public PowerUpCard chosenOne;

  @Override
  public String id() {
    return ControllerNodes.APPLY_TELEPORT.name();
  }

  @Override
  public void onJump(VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) { }

  @Override
  public void handleEvent(ViewEvent event, VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onUseNewtonEvent(useNewtonEvent -> {
      // This node must assume that the actual player could not have a newton powerup, so a check is needed
      if (!model.getPlayerDashboard(context.getTurnOfPlayer()).getPowerUpCards().contains(useNewtonEvent.getChosenCard())) return;
      this.chosenOne = useNewtonEvent.getChosenCard();
      Map<PlayerColor, List<Position>> movements = model
        .getPlayers()
        .stream()
        .filter(p -> p != context.getTurnOfPlayer())
        .collect(Collectors.toMap(Function.identity(), p -> model.getDashboard().calculateMovements(model.getPlayerPosition(p), 2)));
      view.showChoosePlayerToMoveNewton(movements);
    });
    event.onUseNewtonPositionEvent(useNewtonPositionEvent -> {
      model.getPlayerDashboard(context.getTurnOfPlayer()).removePowerUpCard(chosenOne);
      // Reset internal state!
      chosenOne = null;
      model.movePlayerInDashboard(useNewtonPositionEvent.getChosenPosition(), useNewtonPositionEvent.getPlayer());
    });
  }

}
