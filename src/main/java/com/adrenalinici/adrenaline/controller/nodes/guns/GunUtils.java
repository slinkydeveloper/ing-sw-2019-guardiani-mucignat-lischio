package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.model.PlayerColor;

public class GunUtils {

  public static void hitPlayer(GunFlowState state, GameModel model, ControllerFlowContext context, PlayerColor victim, int damages) {
    if (model.hitPlayer(context.getTurnOfPlayer(), victim, damages)) {
      context.getKilledPlayers().add(victim);
    }
    state.getHitPlayers().add(victim);
  }

  public static void hitAndMarkPlayer(GunFlowState state, GameModel model, ControllerFlowContext context, PlayerColor victim, int damages, int marks) {
    if (model.hitAndMarkPlayer(context.getTurnOfPlayer(), victim, damages, marks)) {
      context.getKilledPlayers().add(victim);
    }
    state.getHitPlayers().add(victim);
  }

}
