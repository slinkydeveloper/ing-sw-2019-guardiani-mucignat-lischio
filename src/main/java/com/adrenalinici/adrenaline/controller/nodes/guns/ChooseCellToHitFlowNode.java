package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.controller.nodes.ControllerNodes;
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
  @Override
  public String id() {
    return ControllerNodes.CHOOSE_CELL.name();
  }

  @Override
  public void onJump(GunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    TriPredicate<Position, Position, GameModel> predicate = resolveDistanceEvalPredicate(flowState);
    Position killerPosition = model.getPlayerPosition(context.getTurnOfPlayer());
    Set<Position> hittableCells = //cells at distance == 1 from killerPosition
      model.getDashboard()
        .stream()
        //.filter(cell -> model.getDashboard().calculateDistance(killerPosition, cell.getPosition()) == 1)
        .filter(cell -> predicate.test(killerPosition, cell.getPosition(), model))
        .map(DashboardCell::getPosition)
        .collect(Collectors.toSet());

    view.showAvailableCellsToHit(hittableCells);
  }

  @Override
  public void handleEvent(ViewEvent event, GunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onCellToHitChosenEvent(
      e -> {
        if (e.getCellPosition() != null) {
          model.getDashboard()
            .getDashboardCell(e.getCellPosition())
            .getPlayersInCell()
            .forEach(p -> flowState.getChosenPlayersToHit().add(p));
        }

        context.nextPhase(view, flowState);
      });
  }

  private TriPredicate<Position, Position, GameModel> resolveDistanceEvalPredicate(GunFlowState flowState) {
    return JsonUtils.parseDistanceEvalForCellsPredicate(
      flowState.resolvePhaseConfiguration(id()).get("cellsDistanceEval").asText()
    );
  }
}
