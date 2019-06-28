package com.adrenalinici.adrenaline.server.controller.nodes;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.UnavailablePlayerEvent;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.server.flow.FlowState;
import com.adrenalinici.adrenaline.server.model.GameModel;

import java.util.ArrayList;
import java.util.List;

import static com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes.RESPAWN_KILLED_PEOPLE;

public class RespawnFlowNode implements ControllerFlowNode<RespawnFlowNode.RespawnFlowState> {

  public static class RespawnFlowState implements FlowState {
    List<PlayerColor> waitingPlayers;

    public RespawnFlowState() {
      this.waitingPlayers = new ArrayList<>();
    }
  }

  @Override
  public String id() {
    return RESPAWN_KILLED_PEOPLE.name();
  }

  @Override
  public RespawnFlowState mapState(FlowState oldState) {
    return new RespawnFlowState();
  }

  @Override
  public void onJump(RespawnFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    if (context.getKilledPlayers().isEmpty()) context.nextPhase(view);
    List<PlayerColor> players = context.getKilledPlayers();
    flowState.waitingPlayers.addAll(players);
    players.forEach(p -> {
      model.acquirePowerUpCard(p);
      view.showAvailablePowerUpCardsForRespawn(
        p,
        model.getPlayerDashboard(p).getPowerUpCards()
      );
    });
  }

  @Override
  public void handleEvent(ViewEvent event, RespawnFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onPowerUpChosenEvent(powerUpCardChosenEvent -> {
      flowState.waitingPlayers.remove(powerUpCardChosenEvent.getPlayer());
      model.respawnPlayer(powerUpCardChosenEvent.getPlayer(), powerUpCardChosenEvent.getCard());
      if (flowState.waitingPlayers.isEmpty())
        context.nextPhase(view);
    });
    if (event.isUnavailablePlayerEvent()) {
      flowState.waitingPlayers.remove(((UnavailablePlayerEvent)event).getPlayerColor());
    }
  }



}
