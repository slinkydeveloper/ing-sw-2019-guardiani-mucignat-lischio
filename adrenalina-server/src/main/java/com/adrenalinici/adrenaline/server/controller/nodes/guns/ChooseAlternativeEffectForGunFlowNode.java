package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.common.model.AlternativeEffectGun;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.util.LogUtils;
import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.server.controller.GunLoader;
import com.adrenalinici.adrenaline.server.model.GameModel;

import java.util.logging.Logger;

import static com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes.ALTERNATIVE_GUN_START;

/**
 * This node helps the player choosing the AlternativeGun effect he want to apply.
 * It displays to the player only the effects he could effectively apply, according to the ammo he has.
 * After the player has chosen the effect, it sets the current state and
 * load the additional flow nodes required from the effect itself.
 */
public class ChooseAlternativeEffectForGunFlowNode implements ControllerFlowNode<AlternativeEffectGunFlowState> {
  private static final Logger LOG = LogUtils.getLogger(GunLoader.class);

  @Override
  public String id() {
    return ALTERNATIVE_GUN_START.name();
  }

  @Override
  public void onJump(AlternativeEffectGunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    if (canUseSecondEffect(model, context.getTurnOfPlayer(), flowState.getChosenGun().get())) {
      view.showAvailableAlternativeEffectsGun(flowState.getChosenGun().getFirstEffect().get(), flowState.getChosenGun().getSecondEffect().get());
    } else {
      flowState.setChosenEffect(flowState.getChosenGun().getFirstEffect(), true);
      context.addPhases(flowState.getChosenGun().getFirstEffect().getAdditionalPhases().toArray(new String[0]));
      context.nextPhase(view, flowState);
    }
  }

  @Override
  public void handleEvent(ViewEvent event, AlternativeEffectGunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onAlternativeGunEffectChosenEvent(alternativeGunEffectChosenEvent -> {
      flowState.setChosenEffect(
        alternativeGunEffectChosenEvent.chosenFirstEffect() ?
          flowState.getChosenGun().getFirstEffect() :
          flowState.getChosenGun().getSecondEffect(),
        alternativeGunEffectChosenEvent.chosenFirstEffect()
      );
      context.addPhases(flowState.getChosenEffect().getAdditionalPhases().toArray(new String[0]));

      LOG.info("Loaded phases:" + context.getPhasesQueue().toString());

      context.nextPhase(view, flowState);
    });
  }

  private boolean canUseSecondEffect(GameModel model, PlayerColor playerColor, AlternativeEffectGun gun) {
    if (gun.getSecondEffectCost() == null || gun.getSecondEffectCost().isEmpty()) return true;
    return model
      .getPlayerDashboard(playerColor)
      .getAllAmmosIncludingPowerups()
      .contains(gun.getSecondEffectCost());
  }

}
