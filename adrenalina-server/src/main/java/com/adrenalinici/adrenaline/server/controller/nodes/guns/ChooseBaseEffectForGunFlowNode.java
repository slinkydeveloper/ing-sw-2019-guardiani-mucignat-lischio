package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.common.model.BaseEffectGun;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.util.LogUtils;
import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.server.controller.GunLoader;
import com.adrenalinici.adrenaline.server.model.GameModel;

import java.util.logging.Logger;

import static com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes.BASE_GUN_START;

/**
 * This node helps the player choosing the BaseEffectGun effect he want to apply.
 * It displays to the player only the effects he could effectively apply, according to the ammo he has.
 * After the player has chosen the effect, it sets the current state and
 * load the additional flow nodes required from the effect itself.
 */
public class ChooseBaseEffectForGunFlowNode implements ControllerFlowNode<BaseEffectGunFlowState> {
  private static final Logger LOG = LogUtils.getLogger(GunLoader.class);

  @Override
  public String id() {
    return BASE_GUN_START.name();
  }

  @Override
  public void onJump(BaseEffectGunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    boolean canUseFirstExtraEffect = canUseFirstExtraEffect(model, context.getTurnOfPlayer(), flowState.getChosenGun().get());
    boolean canUseSecondExtraEffect = canUseSecondExtraEffect(model, context.getTurnOfPlayer(), flowState.getChosenGun().get());
    if (canUseFirstExtraEffect || canUseSecondExtraEffect) {
      view.showAvailableExtraEffects(
        canUseFirstExtraEffect ? flowState.getChosenGun().get().getFirstExtraEffect() : null,
        canUseSecondExtraEffect ? flowState.getChosenGun().get().getSecondExtraEffect() : null
      );
    } else {
      context.nextPhase(view, flowState.setActivatedFirstExtraEffect(false).setActivatedSecondExtraEffect(false));
    }
  }

  @Override
  public void handleEvent(ViewEvent event, BaseEffectGunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onBaseGunEffectChosenEvent(baseGunEffectChosenEvent -> {
      if (baseGunEffectChosenEvent.isChosenFirstExtraEffect()) {
        flowState.setActivatedFirstExtraEffect(true);
        context.addPhases(flowState.getChosenGun().getFirstExtraEffect().getAdditionalPhases().toArray(new String[0]));
      }
      if (baseGunEffectChosenEvent.isChosenSecondExtraEffect()) {
        flowState.setActivatedSecondExtraEffect(true);
        context.addPhases(flowState.getChosenGun().getSecondExtraEffect().getAdditionalPhases().toArray(new String[0]));
      }

      LOG.info("Loaded phases:" + context.getPhasesQueue().toString());

      context.nextPhase(view, flowState);
    });
  }

  private boolean canUseFirstExtraEffect(GameModel model, PlayerColor playerColor, BaseEffectGun gun) {
    if (!gun.hasFirstExtraEffect()) return false;
    if (gun.getFirstExtraEffectCost() == null || gun.getFirstExtraEffectCost().isEmpty())
      return true;
    return model
      .getPlayerDashboard(playerColor)
      .getAllAmmosIncludingPowerups()
      .contains(gun.getFirstExtraEffectCost());
  }

  private boolean canUseSecondExtraEffect(GameModel model, PlayerColor playerColor, BaseEffectGun gun) {
    if (!gun.hasSecondExtraEffect()) return false;
    if (gun.getSecondExtraEffectCost() == null || gun.getSecondExtraEffectCost().isEmpty())
      return true;
    return model
      .getPlayerDashboard(playerColor)
      .getAllAmmosIncludingPowerups()
      .contains(gun.getSecondExtraEffectCost());
  }

}
