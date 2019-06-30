package com.adrenalinici.adrenaline.server.controller.nodes;

import com.adrenalinici.adrenaline.common.util.LogUtils;
import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.controller.*;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.AlternativeEffectGunFlowStateImpl;
import com.adrenalinici.adrenaline.server.controller.nodes.guns.BaseEffectGunFlowStateImpl;
import com.adrenalinici.adrenaline.server.flow.impl.VoidState;
import com.adrenalinici.adrenaline.server.model.GameModel;

import java.util.logging.Logger;

import static com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes.*;

public class ChooseGunFlowNode implements StatelessControllerFlowNode {

  private static final Logger LOG = LogUtils.getLogger(GunLoader.class);

  @Override
  public String id() {
    return ControllerNodes.CHOOSE_GUN.name();
  }

  @Override
  public void onJump(VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    view.showAvailableGuns(model.getPlayerDashboard(context.getTurnOfPlayer()).getLoadedGuns());
  }

  @Override
  public void handleEvent(ViewEvent event, VoidState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onGunChosenEvent(gunChosenEvent -> {
      if (gunChosenEvent.getGunid() != null) {
        GunLoader.INSTANCE
          .getModelGun(gunChosenEvent.getGunid())
          .visit(alternativeEffectGun -> {
            DecoratedAlternativeEffectGun decorated = (DecoratedAlternativeEffectGun) GunLoader.INSTANCE
              .getDecoratedGun(alternativeEffectGun.getId());

            context.addPhases(decorated.getPhases().toArray(new String[0]));
            context.addPhasesToEnd(USE_SCOPE.name(), USE_TAGBACK_GRENADE.name(), CHOOSE_ACTION.name());

            LOG.info("Loaded phases" + context.getPhasesQueue().toString());

            context.nextPhase(view, new AlternativeEffectGunFlowStateImpl(decorated));
          }, baseEffectGun -> {
            DecoratedBaseEffectGun decorated = (DecoratedBaseEffectGun) GunLoader.INSTANCE
              .getDecoratedGun(baseEffectGun.getId());

            context.addPhases(decorated.getPhases().toArray(new String[0]));
            context.addPhases(USE_SCOPE.name());
            context.addPhasesToEnd(USE_TAGBACK_GRENADE.name(), CHOOSE_ACTION.name());

            LOG.info("Loaded phases" + context.getPhasesQueue().toString());

            context.nextPhase(view, new BaseEffectGunFlowStateImpl(decorated));
          });
      } else context.nextPhase(view);
    });
  }
}
