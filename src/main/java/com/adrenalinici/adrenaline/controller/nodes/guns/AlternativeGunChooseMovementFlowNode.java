package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.model.common.CardinalDirection;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.Position;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This node manage the movement of the killer as an effect of an AlternativeEffectGun.
 * The only available movements shown to the player are filtered according to the last
 * chosen victim and its cardinal direction as compared to the killer.
 */
public class AlternativeGunChooseMovementFlowNode implements SkippableGunFlowNode<AlternativeEffectGunFlowState> {

  @Override
  public String id() {
    return ControllerNodes.ALTERNATIVE_GUN_MOVEMENT.name();
  }

  @Override
  public void onJump(AlternativeEffectGunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    List<Position> availableMovements;
    Position actualPlayerPosition = model.getDashboard().getPlayersPositions().get(context.getTurnOfPlayer());

    PlayerColor previousEnemy = flowState.getChosenPlayersToHit().get(flowState.getChosenPlayersToHit().size() - 1);
    Position previousEnemyPosition = model.getPlayerPosition(previousEnemy);
    CardinalDirection previousEnemyCardinalDirection =
      model
        .getDashboard()
        .calculateCardinalDirection(model.getPlayerPosition(context.getTurnOfPlayer()), model.getPlayerPosition(previousEnemy));

    availableMovements =
      model.getDashboard().calculateMovements(previousEnemyPosition, 1)
        .stream()
        .filter(position ->
          model.getDashboard().calculateCardinalDirection(actualPlayerPosition, position) == previousEnemyCardinalDirection
        ).collect(Collectors.toList());

    view.showAvailableMovements(availableMovements);
  }

  @Override
  public void handleEvent(ViewEvent event, AlternativeEffectGunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onMovementChosenEvent(
      e -> {
        if (e.getCoordinates() != null && !e.getCoordinates().equals(model.getPlayerPosition(context.getTurnOfPlayer())))
          model.movePlayerInDashboard(e.getCoordinates(), context.getTurnOfPlayer());

        context.nextPhase(view, flowState);
      }
    );
  }
}
