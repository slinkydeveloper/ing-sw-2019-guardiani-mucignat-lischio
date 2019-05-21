package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.model.common.Position;
import com.adrenalinici.adrenaline.model.fat.DashboardCell;
import com.adrenalinici.adrenaline.model.fat.GameModel;
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
    Position killerPosition = model.getPlayerPosition(context.getTurnOfPlayer());
    Set<Position> hittableCells = //cells at distance == 1 from killerPosition
      model.getDashboard()
        .stream()
        .filter(cell -> model.getDashboard().calculateDistance(killerPosition, cell.getPosition()) == 1)
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
}
