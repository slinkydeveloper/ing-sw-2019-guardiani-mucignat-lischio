package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.*;
import com.adrenalinici.adrenaline.flow.FlowState;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.Position;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;
import java.util.stream.Collectors;

import static com.adrenalinici.adrenaline.controller.nodes.ControllerNodes.gunEnemyMovement;

public class GunChooseEnemyMovementFlowNode implements SkippableGunFlowNode<GunFlowState> {
  int distance;

  public GunChooseEnemyMovementFlowNode(int distance) {
    this.distance = distance;
  }

  @Override
  public String id() {
    return gunEnemyMovement(distance);
  }


  @Override
  public void onJump(GunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    PlayerColor enemy = new ArrayList<>(flowState.getHitPlayers().keySet()).get(0);
    Position actualEnemyPosition = model.getPlayerPosition(enemy);
    if (resolveMovementModeConfiguration(flowState).equals("only one direction")) {
      view.showAvailableMovements(
        model.getDashboard().calculateMovementsInOneDirection(actualEnemyPosition, distance)
      );
    } else if (resolveMovementModeConfiguration(flowState).equals("only visible squares")) {
      view.showAvailableMovements(
        model.getDashboard().calculateMovements(actualEnemyPosition, distance)
          .stream()
          .filter(enemyPosition ->
            model.getDashboard().calculateIfVisible(enemyPosition, model.getPlayerPosition(context.getTurnOfPlayer()))
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
    return flowState.resolvePhaseConfiguration(id()).get("mode").asText();
  }
}
