package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.*;
import com.adrenalinici.adrenaline.flow.FlowState;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.Position;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;
import java.util.stream.Collectors;

import static com.adrenalinici.adrenaline.controller.nodes.ControllerNodes.gunEnemyMovement;

public class GunChooseEnemyMovementFlowNode implements SkippableGunFlowNode<GunChooseEnemyMovementFlowNode.GunChooseEnemyMovementFlowState> {

  public static class GunChooseEnemyMovementFlowState implements AlternativeEffectGunFlowState {
    PlayerColor enemyToMove;
    AlternativeEffectGunFlowState originalFlowState;

    public GunChooseEnemyMovementFlowState(AlternativeEffectGunFlowState originalFlowState, PlayerColor enemyToMove) {
      this.originalFlowState = originalFlowState;
      this.enemyToMove = enemyToMove;
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
    public DecoratedAlternativeEffectGun getChosenGun() {
      return originalFlowState.getChosenGun();
    }

    @Override
    public void applyHitAndMarkPlayers(GameModel model, ControllerFlowContext context) {
      originalFlowState.applyHitAndMarkPlayers(model, context);
    }

    @Override
    public DecoratedEffect getChosenEffect() {
      return originalFlowState.getChosenEffect();
    }

    @Override
    public AlternativeEffectGunFlowState setChosenEffect(DecoratedEffect chosenEffect, boolean chosenEffectIsFirstEffect) {
      return originalFlowState.setChosenEffect(chosenEffect, chosenEffectIsFirstEffect);
    }

    @Override
    public boolean isFirstEffect() {
      return originalFlowState.isFirstEffect();
    }

    @Override
    public ObjectNode resolvePhaseConfiguration(String phaseId) {
      return originalFlowState.resolvePhaseConfiguration(phaseId);
    }
  }

  int distance;

  public GunChooseEnemyMovementFlowNode(int distance) {
    this.distance = distance;
  }

  @Override
  public String id() {
    return gunEnemyMovement(distance);
  }

  @Override
  public GunChooseEnemyMovementFlowState mapState(FlowState oldState) {
    if (oldState instanceof GunChooseEnemyMovementFlowNode.GunChooseEnemyMovementFlowState)
      return (GunChooseEnemyMovementFlowNode.GunChooseEnemyMovementFlowState) oldState;

    return new GunChooseEnemyMovementFlowNode.GunChooseEnemyMovementFlowState(
      (AlternativeEffectGunFlowState) oldState,
      new ArrayList<>(((AlternativeEffectGunFlowState) oldState).getHitPlayers().keySet()).get(0)
    );
  }

  @Override
  public void onJump(GunChooseEnemyMovementFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    Position actualEnemyPosition = model.getPlayerPosition(flowState.enemyToMove);
    view.showAvailableMovements(
      model.getDashboard().calculateMovements(actualEnemyPosition, distance)
    );

  }

  @Override
  public void handleEvent(ViewEvent event, GunChooseEnemyMovementFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onEnemyMovementChosenEvent(
      e -> {
        if (
          e.getCoordinates() != null &&
            e.getEnemy() != null &&
            !e.getCoordinates().equals(model.getPlayerPosition(e.getEnemy()))
        ) model.movePlayerInDashboard(e.getCoordinates(), e.getEnemy());

        context.nextPhase(view, flowState);
      }
    );
  }
}
