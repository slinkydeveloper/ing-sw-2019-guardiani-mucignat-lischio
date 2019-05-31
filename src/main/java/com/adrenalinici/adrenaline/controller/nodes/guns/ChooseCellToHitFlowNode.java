package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.model.common.CardinalDirection;
import com.adrenalinici.adrenaline.model.common.Position;
import com.adrenalinici.adrenaline.model.fat.DashboardCell;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.util.JsonUtils;
import com.adrenalinici.adrenaline.util.TriPredicate;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.Set;
import java.util.stream.Collectors;

public class ChooseCellToHitFlowNode implements ControllerFlowNode<GunFlowState> {

  public static final String DEFAULT = "default";
  public static final String HIT_EVERYONE = "hit_everyone";
  public static final String SAME_DIRECTION = "same_direction";

  @Override
  public String id() {
    return ControllerNodes.CHOOSE_CELL.name();
  }

  @Override
  public void onJump(GunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    if (flowState.getChosenCellsToHit().size() == resolveHittableCellsNumber(flowState)) {
      context.nextPhase(view, flowState);
    } else {
      TriPredicate<Position, Position, GameModel> predicate = resolveDistanceEvalPredicate(flowState);
      Position killerPosition = model.getPlayerPosition(context.getTurnOfPlayer());
      Set<Position> hittableCells;

      if (resolveHittableCellsRestriction(flowState).equals(SAME_DIRECTION) && !flowState.getChosenCellsToHit().isEmpty()) {
        Position previousCell = flowState.getChosenCellsToHit().get(0);
        CardinalDirection previousCellCardinalDirection = model.getDashboard().calculateCardinalDirection(killerPosition, previousCell);
        hittableCells =
          model.getDashboard()
            .stream()
            .filter(cell -> !flowState.getChosenCellsToHit().contains(cell.getPosition()))
            .filter(cell -> predicate.test(previousCell, cell.getPosition(), model))
            .filter(cell -> model.getDashboard().calculateCardinalDirection(killerPosition, cell.getPosition()) == previousCellCardinalDirection)
            .map(DashboardCell::getPosition)
            .collect(Collectors.toSet());

      } else {
        hittableCells =
          model.getDashboard()
            .stream()
            .filter(cell -> predicate.test(killerPosition, cell.getPosition(), model))
            .map(DashboardCell::getPosition)
            .collect(Collectors.toSet());
      }

      view.showAvailableCellsToHit(hittableCells);
    }
  }

  @Override
  public void handleEvent(ViewEvent event, GunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onCellToHitChosenEvent(
      e -> {
        if (e.getCellPosition() == null) {
          context.nextPhase(view, flowState);
        } else {
          flowState.getChosenCellsToHit().add(e.getCellPosition());

          if (resolveModeConfig(flowState).equals(HIT_EVERYONE)) {
            model.getDashboard()
              .getDashboardCell(e.getCellPosition())
              .getPlayersInCell()
              .forEach(p -> flowState.getChosenPlayersToHit().add(p));
          }
          context.replayNode(view);
        }
      });
  }

  private TriPredicate<Position, Position, GameModel> resolveDistanceEvalPredicate(GunFlowState flowState) {
    return JsonUtils.parseDistanceEvalForCellsPredicate(
      flowState.resolvePhaseConfiguration(id()).get("cellsDistanceEval").asText()
    );
  }

  private int resolveHittableCellsNumber(GunFlowState flowState) {
    return flowState.resolvePhaseConfiguration(id()).get("hittableCellsNumber").asInt();
  }

  private String resolveHittableCellsRestriction(GunFlowState flowState) {
    if (!flowState.resolvePhaseConfiguration(id()).has("restriction"))
      return DEFAULT;

    return flowState.resolvePhaseConfiguration(id()).get("restriction").asText();
  }

  private String resolveModeConfig(GunFlowState flowState) {
    if (!flowState.resolvePhaseConfiguration(id()).has("mode"))
      return DEFAULT;

    return flowState.resolvePhaseConfiguration(id()).get("mode").asText();
  }
}
