package com.adrenalinici.adrenaline.server.controller.nodes;

import com.adrenalinici.adrenaline.common.model.AmmoColor;
import com.adrenalinici.adrenaline.common.model.Gun;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.util.Bag;
import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.GunLoader;
import com.adrenalinici.adrenaline.server.controller.StatelessControllerFlowNode;
import com.adrenalinici.adrenaline.server.flow.impl.VoidState;
import com.adrenalinici.adrenaline.server.model.DashboardCell;
import com.adrenalinici.adrenaline.server.model.GameModel;
import com.adrenalinici.adrenaline.server.model.RespawnDashboardCell;

import java.util.Set;
import java.util.stream.Collectors;

import static com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes.PICKUP;

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
      Set<String> guns = calculateAvailableGunsToPickup(model, respawnDashboardCell, context.getTurnOfPlayer());
      if (guns.isEmpty()) context.nextPhase(view);
      view.showAvailableGunsToPickup(guns);
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
      if (gunToPickupChosenEvent.getGunid() != null) {
        Gun chosenGun = GunLoader.INSTANCE.getModelGun(gunToPickupChosenEvent.getGunid());
        model.acquireGun(context.getTurnOfPlayer(), chosenGun);
      }
      context.nextPhase(view);
    });
  }

  protected Set<String> calculateAvailableGunsToPickup(GameModel model, RespawnDashboardCell respawnCell, PlayerColor player) {
    Bag<AmmoColor> playerAmmosBag = model.getPlayerDashboard(player).getAllAmmosIncludingPowerups();
    return respawnCell
      .getAvailableGuns()
      .stream()
      .map(GunLoader.INSTANCE::getModelGun)
      .filter(
        gun -> playerAmmosBag.contains(Bag.from(gun.getRequiredAmmoToPickup()))
      )
      .map(Gun::getId)
      .collect(Collectors.toSet());
  }
}
