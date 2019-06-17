package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.PowerUpCard;
import com.adrenalinici.adrenaline.common.model.PowerUpType;
import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.server.model.GameModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes.USE_SCOPE;

public class ApplyScopeFlowNode implements ControllerFlowNode<GunFlowState> {
  @Override
  public String id() {
    return USE_SCOPE.name();
  }

  @Override
  public void onJump(GunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    List<PowerUpCard> currentPlayerScopes =
      model.getPlayerDashboard(context.getTurnOfPlayer())
        .getPowerUpCards()
        .stream()
        .filter(puc -> puc.getPowerUpType().equals(PowerUpType.SCOPE))
        .collect(Collectors.toList());

    if (currentPlayerScopes.isEmpty() || flowState.getHitPlayers().isEmpty()) {
      context.nextPhase(view);
    } else {
      List<PlayerColor> hittablePlayers = new ArrayList<>(flowState.getHitPlayers().keySet());
      System.out.println("I'm the SCOPE, wanna give an additional damage to one of this enemy?");
      view.showChoosePlayerToHit(hittablePlayers);
    }

  }

  @Override
  public void handleEvent(ViewEvent event, GunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onPlayerChosenEvent(
      e -> {
        if (e.getPlayerColor() == null) context.nextPhase(view);
        else {
          model.hitPlayer(context.getTurnOfPlayer(), e.getPlayerColor(), 1);

          model.getPlayerDashboard(context.getTurnOfPlayer())
            .getPowerUpCards()
            .stream()
            .filter(puc -> puc.getPowerUpType().equals(PowerUpType.SCOPE))
            .findFirst()
            .ifPresent(puc -> model.removePowerUpFromPlayer(context.getTurnOfPlayer(), puc));

          context.nextPhase(view);
        }
      });
  }
}
