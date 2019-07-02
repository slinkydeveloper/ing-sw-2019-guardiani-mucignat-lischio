package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.common.model.CellColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.server.model.DashboardCell;
import com.adrenalinici.adrenaline.server.model.GameModel;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * This node helps the player choosing the room he want to hit, as an effect of a gun.
 * It shows the player only the rooms that follows certain visibility and distance restrictions,
 * in order not to let him make any illegal choice.
 */
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
          !cell.getCellColor().equals(model.getDashboard().getDashboardCell(killerPosition).getCellColor())
        )
        .filter(cell -> model.getDashboard().calculateDistance(killerPosition, cell.getPosition()) == 1)
        .filter(cell -> model.getDashboard().calculateIfVisible(killerPosition, cell.getPosition()))
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
