package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.model.common.BaseEffectGun;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import static com.adrenalinici.adrenaline.controller.nodes.ControllerNodes.BASE_GUN_START;

public class ChooseBaseEffectForGunFlowNode implements ControllerFlowNode<BaseEffectGunFlowState> {

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
