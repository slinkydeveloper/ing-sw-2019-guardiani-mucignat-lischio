package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.Position;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.stream.Collectors;

import static com.adrenalinici.adrenaline.controller.nodes.ControllerNodes.gunEnemyMovement;

/**
 * This node manages the movement of an enemy.
 * Before showing the player all the positions in which he can move the enemy,
 * it checks the "mode" config in gun's json in order to select only a certain
 * set of available positions in accordance with this.
 */
public class GunChooseEnemyMovementFlowNode implements SkippableGunFlowNode<GunFlowState> {
  int distance;
  public static final String ONLY_ONE_DIRECTION = "only_one_direction";
  public static final String ONLY_VISIBLE_SQUARES = "only_visible_squares";
  public static final String DEFAULT = "default";

  public GunChooseEnemyMovementFlowNode(int distance) {
    this.distance = distance;
  }

  @Override
  public String id() {
    return gunEnemyMovement(distance);
  }


  @Override
  public void onJump(GunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    PlayerColor enemy = flowState.getChosenPlayersToHit().get(0);
    Position actualEnemyPosition = model.getPlayerPosition(enemy);
    if (resolveMovementModeConfiguration(flowState).equals(ONLY_ONE_DIRECTION)) {
      view.showAvailableMovements(
        model.getDashboard().calculateMovementsInOneDirection(actualEnemyPosition, distance)
      );
    } else if (resolveMovementModeConfiguration(flowState).equals(ONLY_VISIBLE_SQUARES)) {
      view.showAvailableMovements(
        model.getDashboard().calculateMovements(actualEnemyPosition, distance)
          .stream()
          .filter(possibleEnemyPosition ->
            model.getDashboard().calculateIfVisible(possibleEnemyPosition, model.getPlayerPosition(context.getTurnOfPlayer()))
          )
          .collect(Collectors.toList())
      );
    } else {
      view.showAvailableMovements(
        model.getDashboard().calculateMovements(actualEnemyPosition, distance)
      );
    }
  }

  @Override
  public void handleEvent(ViewEvent event, GunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onEnemyMovementChosenEvent(
      e -> {
        if (
          e.getCoordinates() != null &&
            e.getEnemy() != null &&
            !e.getCoordinates().equals(model.getPlayerPosition(e.getEnemy()))
        ) model.movePlayerInDashboard(e.getCoordinates(), e.getEnemy());

        context.nextPhase(view, flowState);
      }
    );
  }

  private String resolveMovementModeConfiguration(GunFlowState flowState) {
    if (!flowState.resolvePhaseConfiguration(id()).has("mode"))
      return DEFAULT;

    return flowState.resolvePhaseConfiguration(id()).get("mode").asText();
  }
}
