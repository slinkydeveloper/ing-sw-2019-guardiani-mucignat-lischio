package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.flow.FlowState;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.PowerUpType;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.ArrayList;
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
    return gunFlowState
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
