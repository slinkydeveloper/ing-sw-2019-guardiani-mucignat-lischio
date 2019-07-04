package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.common.model.CardinalDirection;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.util.TriPredicate;
import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.JsonUtils;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.server.model.DashboardCell;
import com.adrenalinici.adrenaline.server.model.GameModel;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * This node helps the player choosing a cell to hit.
 * It uses couple of flag -loaded from gun config- to determine the cells
 * that could really be hit and how the effect should be applied.
 * <p>
 * HIT_EVERYONE --> every player on chosen cell will be hit
 * <p>
 * SAME_DIRECTION --> available cells will be filtered according to the cardinal direction
 * of another cell previously chosen
 */
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

      if (hittableCells.isEmpty()) context.nextPhase(view, flowState);
      else view.showAvailableCellsToHit(hittableCells);
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
