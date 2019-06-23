package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.PowerUpType;
import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.server.flow.FlowState;
import com.adrenalinici.adrenaline.server.model.GameModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TagbackGrenadeFlowNode implements ControllerFlowNode<TagbackGrenadeFlowNode.TagbackGrenadeFlowState> {

  public static class TagbackGrenadeFlowState implements FlowState {
    List<PlayerColor> waitingPlayers;
    GunFlowState gunFlowState;

    public TagbackGrenadeFlowState(GunFlowState gunFlowState) {
      this.gunFlowState = gunFlowState;
      this.waitingPlayers = new ArrayList<>();
    }
  }

  @Override
  public String id() {
    return ControllerNodes.USE_TAGBACK_GRENADE.name();
  }

  @Override
  public TagbackGrenadeFlowState mapState(FlowState oldState) {
    return new TagbackGrenadeFlowState((GunFlowState) oldState);
  }

  @Override
  public void onJump(TagbackGrenadeFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    List<PlayerColor> playersThatCanUseVenomGrenade = calculatePlayersThatCanUseVenomGrenade(flowState.gunFlowState, model, context);
    if (playersThatCanUseVenomGrenade.isEmpty()) {
      context.nextPhase(view, flowState.gunFlowState);
    } else {
      playersThatCanUseVenomGrenade.forEach(p -> {
        flowState.waitingPlayers.add(p);
        view.showAvailableTagbackGrenade(p,
          model.getPlayerDashboard(p)
            .getPowerUpCards()
            .stream()
            .filter(puc -> puc.getPowerUpType().equals(PowerUpType.TAGBACK_GRENADE))
            .collect(Collectors.toList())
        );
      });
    }
  }

  @Override
  public void handleEvent(ViewEvent event, TagbackGrenadeFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onUseVenomGrenadeEvent(useVenomGrenadeEvent -> {
      flowState.waitingPlayers.remove(useVenomGrenadeEvent.getPlayerColor());
      if (useVenomGrenadeEvent.getChosenCard() != null) {
        model.removePowerUpFromPlayer(useVenomGrenadeEvent.getPlayerColor(), useVenomGrenadeEvent.getChosenCard());
        model.markPlayer(useVenomGrenadeEvent.getPlayerColor(), context.getTurnOfPlayer(), 1);
      }
      if (flowState.waitingPlayers.isEmpty())
        context.nextPhase(view, flowState.gunFlowState);
    });
  }

  protected List<PlayerColor> calculatePlayersThatCanUseVenomGrenade(GunFlowState gunFlowState, GameModel model, ControllerFlowContext context) {
    return gunFlowState == null ? Collections.emptyList() :
      gunFlowState
        .getHitPlayers()
        .keySet()
        .stream()
        .filter(f -> model.getPlayerDashboard(f).hasVenomGrenade())
        .filter(f -> 3 - model.calculateKillerMarksOnVictimPlayerDashboard(f, context.getTurnOfPlayer()) >= 1)
        .filter(f -> model.getDashboard().calculateIfVisible(
          model.getPlayerPosition(f),
          model.getPlayerPosition(context.getTurnOfPlayer())
        ))
        .collect(Collectors.toList());
  }

}
