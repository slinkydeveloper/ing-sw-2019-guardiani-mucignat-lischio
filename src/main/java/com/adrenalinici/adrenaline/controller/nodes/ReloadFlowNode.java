package com.adrenalinici.adrenaline.controller.nodes;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.GunLoader;
import com.adrenalinici.adrenaline.controller.StatelessControllerFlowNode;
import com.adrenalinici.adrenaline.flow.impl.VoidState;
import com.adrenalinici.adrenaline.model.common.AmmoColor;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.model.common.Gun;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.util.Bag;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.Set;
import java.util.stream.Collectors;

import static com.adrenalinici.adrenaline.controller.nodes.ControllerNodes.RELOAD;

public class ReloadFlowNode implements StatelessControllerFlowNode {

  @Override
  public String id() {
    return RELOAD.name();
  }

  @Override
  public void onJump(VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    Set<String> reloadableGuns = calculateReloadableGuns(model, context.getTurnOfPlayer());
    if (reloadableGuns.isEmpty()) context.nextPhase(view);
    else view.showReloadableGuns(reloadableGuns);
  }

  @Override
  public void handleEvent(ViewEvent event, VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onGunChosenEvent(gunToReloadChosenEvent -> {
      Gun chosenGun = GunLoader.INSTANCE.getModelGun(gunToReloadChosenEvent.getGunid());
      model.reloadGun(context.getTurnOfPlayer(), chosenGun);
      context.replayNode(view);
    });
  }

  protected Set<String> calculateReloadableGuns(GameModel model, PlayerColor player) {
    Bag<AmmoColor> playerAmmoBag = model.getPlayerDashboard(player).getAllAmmosIncludingPowerups();
    return model.getPlayerDashboard(player)
      .getUnloadedGuns()
      .stream()
      .map(GunLoader.INSTANCE::getModelGun)
      .filter(
        gun -> playerAmmoBag.contains(Bag.from(gun.getRequiredAmmoToReload()))
      )
      .map(Gun::getId)
      .collect(Collectors.toSet());
  }
}
