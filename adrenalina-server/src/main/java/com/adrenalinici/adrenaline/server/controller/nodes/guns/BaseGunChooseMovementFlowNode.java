package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.DecoratedBaseEffectGun;
import com.adrenalinici.adrenaline.server.flow.FlowState;
import com.adrenalinici.adrenaline.server.model.GameModel;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.Map;

import static com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes.gunMovement;

/**
 * This class manages player movement as an effect of some guns.
 * It has an inner class that works as a state, in which is stored a value
 * called "alreadyMoved" that let the flow knows if the movement is already done or not.
 * This value is modified only in case the player chooses to move in a Position that
 * id different from the one in which he already is.
 */
public class BaseGunChooseMovementFlowNode implements SkippableGunFlowNode<BaseGunChooseMovementFlowNode.GunChooseMovementFlowState> {

  public static class GunChooseMovementFlowState implements BaseEffectGunFlowState {
    boolean alreadyMoved = false;
    BaseEffectGunFlowState originalFlowState;

    public GunChooseMovementFlowState(BaseEffectGunFlowState originalFlowState) {
      this.originalFlowState = originalFlowState;
    }

    @Override
    public List<Position> getChosenCellsToHit() {
      return originalFlowState.getChosenCellsToHit();
    }

    @Override
    public List<PlayerColor> getChosenPlayersToHit() {
      return originalFlowState.getChosenPlayersToHit();
    }

    @Override
    public Map<PlayerColor, Integer> getHitPlayers() {
      return originalFlowState.getHitPlayers();
    }

    @Override
    public Map<PlayerColor, Integer> getMarkPlayers() {
      return originalFlowState.getMarkPlayers();
    }

    @Override
    public void hitPlayer(PlayerColor color, int damages) {
      originalFlowState.hitPlayer(color, damages);
    }

    @Override
    public void markPlayer(PlayerColor color, int marks) {
      originalFlowState.markPlayer(color, marks);
    }

    @Override
    public DecoratedBaseEffectGun getChosenGun() {
      return originalFlowState.getChosenGun();
    }

    @Override
    public void applyHitAndMarkPlayers(GameModel model, ControllerFlowContext context) {
      originalFlowState.applyHitAndMarkPlayers(model, context);
    }

    @Override
    public ObjectNode resolvePhaseConfiguration(String phaseId) {
      return originalFlowState.resolvePhaseConfiguration(phaseId);
    }

    @Override
    public boolean isActivatedFirstExtraEffect() {
      return originalFlowState.isActivatedFirstExtraEffect();
    }

    @Override
    public BaseEffectGunFlowState setActivatedFirstExtraEffect(boolean activatedFirstExtraEffect) {
      return originalFlowState.setActivatedFirstExtraEffect(activatedFirstExtraEffect);
    }

    @Override
    public boolean isActivatedSecondExtraEffect() {
      return originalFlowState.isActivatedSecondExtraEffect();
    }

    @Override
    public BaseEffectGunFlowState setActivatedSecondExtraEffect(boolean activatedSecondExtraEffect) {
      return originalFlowState.setActivatedSecondExtraEffect(activatedSecondExtraEffect);
    }
  }

  int distance;

  public BaseGunChooseMovementFlowNode(int distance) {
    this.distance = distance;
  }

  @Override
  public String id() {
    return gunMovement(distance);
  }

  @Override
  public GunChooseMovementFlowState mapState(FlowState oldState) {
    if (oldState instanceof GunChooseMovementFlowState) return (GunChooseMovementFlowState) oldState;
    return new GunChooseMovementFlowState((BaseEffectGunFlowState) oldState);
  }

  @Override
  public void onJump(GunChooseMovementFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    if (!flowState.alreadyMoved) {
      Position actualPlayerPosition = model.getDashboard().getPlayersPositions().get(context.getTurnOfPlayer());
      List<Position> availableMovements = model.getDashboard().calculateMovements(actualPlayerPosition, distance);

      view.showAvailableMovements(availableMovements);
    } else context.nextPhase(view, flowState);
  }

  @Override
  public void handleEvent(ViewEvent event, GunChooseMovementFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onMovementChosenEvent(
      e -> {
        if (e.getCoordinates() != null && !e.getCoordinates().equals(model.getPlayerPosition(context.getTurnOfPlayer())) && !flowState.alreadyMoved) {
          flowState.alreadyMoved = true;
          model.movePlayerInDashboard(e.getCoordinates(), context.getTurnOfPlayer());
        }
        context.nextPhase(view, flowState);
      }
    );
  }
}
