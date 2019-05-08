package com.adrenalinici.adrenaline.controller.nodes;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.GunLoader;
import com.adrenalinici.adrenaline.controller.StatelessControllerFlowNode;
import com.adrenalinici.adrenaline.flow.impl.VoidState;
import com.adrenalinici.adrenaline.model.*;
import com.adrenalinici.adrenaline.util.Bag;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.List;
import java.util.stream.Collectors;

import static com.adrenalinici.adrenaline.controller.nodes.ControllerNodes.PICKUP;

public class PickupFlowNode implements StatelessControllerFlowNode {

  @Override
  public String id() {
    return PICKUP.name();
  }

  @Override
  public void onJump(VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    Position actualPlayerPosition = model.getDashboard().getPlayersPositions().get(context.getTurnOfPlayer());
    DashboardCell cell = model.getDashboard().getDashboardCell(actualPlayerPosition);

    cell.visit(respawnDashboardCell -> {
      view.showAvailableGunsToPickup(calculateAvailableGunsToPickup(model, respawnDashboardCell, context.getTurnOfPlayer()));
    }, pickupDashboardCell -> {
      // No user interaction required
      model.acquireAmmoCard(pickupDashboardCell, context.getTurnOfPlayer());
      context.nextPhase(view);
    });
  }

  @Override
  public void handleEvent(ViewEvent event, VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onGunChosenEvent(gunToPickupChosenEvent -> {
      //TODO P2 ci sarebbe anche da gestire il caso in cui si supera il limite delle tre armi
      Gun chosenGun = GunLoader.INSTANCE.getModelGun(gunToPickupChosenEvent.getGunid());
      model.acquireGun(context.getTurnOfPlayer(), chosenGun);
      context.nextPhase(view);
    });
  }

  protected List<Gun> calculateAvailableGunsToPickup(GameModel model, RespawnDashboardCell respawnCell, PlayerColor player) {
    Bag<AmmoColor> playerAmmosBag = model.getPlayerDashboard(player).getAllAmmosIncludingPowerups();
    return respawnCell.getAvailableGuns().stream()
      .filter(
        gun -> playerAmmosBag.contains(Bag.from(gun.getRequiredAmmoToPickup()))
      )
      .collect(Collectors.toList());
  }
}
