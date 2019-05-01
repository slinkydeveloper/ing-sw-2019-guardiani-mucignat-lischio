package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.flow.FlowState;
import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.model.PlayerDashboard;
import com.adrenalinici.adrenaline.model.PowerUpType;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.adrenalinici.adrenaline.util.ListUtils.notIn;

public class VenomGrenadeFlowNode implements ControllerFlowNode<VenomGrenadeFlowNode.VenomGrenadeFlowState> {

  public static class VenomGrenadeFlowState implements FlowState {
    List<PlayerColor> waitingPlayers;
    GunFlowState gunFlowState;

    public VenomGrenadeFlowState(GunFlowState gunFlowState) {
      this.gunFlowState = gunFlowState;
      this.waitingPlayers = new ArrayList<>();
    }
  }

  @Override
  public String id() {
    return ControllerNodes.VENOM_GRENADE.name();
  }

  @Override
  public VenomGrenadeFlowState mapState(FlowState oldState) {
    return new VenomGrenadeFlowState((GunFlowState) oldState);
  }

  @Override
  public void onJump(VenomGrenadeFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    Map<PlayerDashboard, Integer> playersWithVenomGrenade =
      calculatePlayersThatCanUseVenomGrenade(flowState.gunFlowState, model, context);
    if (playersWithVenomGrenade.isEmpty()) {
      context.nextPhase(view, flowState.gunFlowState);
    } else {
      playersWithVenomGrenade.forEach((pd, i) -> {
        flowState.waitingPlayers.add(pd.getPlayer());
        view.showAvailableVenomGrenade(pd.getPlayer(), i, pd
          .getPowerUpCards()
          .stream()
          .filter(puc -> puc.getPowerUpType().equals(PowerUpType.VENON_GRANADE))
          .collect(Collectors.toList()));
      });
    }
  }

  @Override
  public void handleEvent(ViewEvent event, VenomGrenadeFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onUseVenomGrenadeEvent(useVenomGrenadeEvent -> {
      flowState.waitingPlayers.remove(useVenomGrenadeEvent.getPlayerColor());
      useVenomGrenadeEvent.getChosenCards().forEach(puc ->
        model.getPlayerDashboard(useVenomGrenadeEvent.getPlayerColor()).removePowerUpCard(puc)
      );
      model.markPlayer(useVenomGrenadeEvent.getPlayerColor(), context.getTurnOfPlayer(), useVenomGrenadeEvent.getChosenCards().size());
      if (flowState.waitingPlayers.isEmpty())
        context.nextPhase(view, flowState.gunFlowState);
    });
  }

  protected Map<PlayerDashboard, Integer> calculatePlayersThatCanUseVenomGrenade(GunFlowState gunFlowState, GameModel model, ControllerFlowContext context) {
    return gunFlowState
      .getHitPlayers()
      .stream()
      .filter(notIn(context.getKilledPlayers())) //TODO also manage mark limit!
      .map(model::getPlayerDashboard)
      .filter(PlayerDashboard::hasVenomGrenadePowerUp)
      .collect(Collectors.toMap(Function.identity(), p -> 1));
  }

}
