package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.model.common.CellColor;
import com.adrenalinici.adrenaline.model.common.Position;
import com.adrenalinici.adrenaline.model.fat.DashboardCell;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.Set;
import java.util.stream.Collectors;

public class ChooseRoomToHitFlowNode implements ControllerFlowNode<GunFlowState> {
  @Override
  public String id() {
    return ControllerNodes.CHOOSE_ROOM.name();
  }

  @Override
  public void onJump(GunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    Position killerPosition = model.getPlayerPosition(context.getTurnOfPlayer());
    Set<CellColor> hittableRooms = //visible rooms excluding the one in which the player is
      model
        .getDashboard()
        .stream()
        .filter(cell ->
          !cell.getCellColor().equals(model.getDashboard().getDashboardCell(killerPosition).getCellColor()))
        .filter(cell -> model.getDashboard().calculateIfVisible(cell.getPosition(), killerPosition))
        .map(DashboardCell::getCellColor)
        .collect(Collectors.toSet());

    view.showAvailableRooms(hittableRooms);
  }

  @Override
  public void handleEvent(ViewEvent event, GunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    Position killerPosition = model.getPlayerPosition(context.getTurnOfPlayer());
    event.onRoomChosenEvent(
      e -> {
        if (e.getRoomColor() != null &&
          !e.getRoomColor().equals(model.getDashboard().getDashboardCell(killerPosition).getCellColor())
        ) {
          model.getDashboard()
            .stream()
            .filter(cell -> cell.getCellColor().equals(e.getRoomColor()))
            .forEach(cell -> cell.getPlayersInCell().forEach(p -> flowState.getChosenPlayersToHit().add(p)));
        }

        context.nextPhase(view, flowState);
      });
  }
}
