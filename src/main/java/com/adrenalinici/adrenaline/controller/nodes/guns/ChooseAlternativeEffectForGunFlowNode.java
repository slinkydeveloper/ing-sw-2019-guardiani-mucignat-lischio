package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import static com.adrenalinici.adrenaline.controller.nodes.ControllerNodes.ALTERNATIVE_GUN_START;

public class ChooseAlternativeEffectForGunFlowNode implements ControllerFlowNode<AlternativeEffectGunFlowState> {

  @Override
  public String id() {
    return ALTERNATIVE_GUN_START.name();
  }

  @Override
  public void onJump(AlternativeEffectGunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    boolean canUseSecondEffect = false; //TODO
    if (canUseSecondEffect) {
      view.showAvailableAlternativeEffectsGun(flowState.getChosenGun().getFirstEffect().get(), flowState.getChosenGun().getSecondEffect().get());
    } else {
      context.nextPhase(view, flowState.setChosenEffect(flowState.getChosenGun().getFirstEffect()));
    }
  }

  @Override
  public void handleEvent(ViewEvent event, AlternativeEffectGunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onAlternativeGunEffectChosenEvent(alternativeGunEffectChosenEvent -> {
      flowState.setChosenEffect(
        alternativeGunEffectChosenEvent.chosenFirstEffect() ?
          flowState.getChosenGun().getFirstEffect() :
          flowState.getChosenGun().getSecondEffect()
      );
      context.addPhasesToHead(flowState.getChosenEffect().getAdditionalPhases().toArray(new String[0]));
      context.nextPhase(view, flowState);
    });
  }

}
