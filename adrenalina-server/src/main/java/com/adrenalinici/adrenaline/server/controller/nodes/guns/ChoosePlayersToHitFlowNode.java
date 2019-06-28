package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.common.model.CardinalDirection;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.util.TriPredicate;
import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.JsonUtils;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.server.model.GameModel;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.adrenalinici.adrenaline.common.util.CollectionUtils.notIn;

/**
 * This node helps the player choosing the enemies he want to/could hit.
 * There are many limitations in showing the player only the enemies he could effectively hit,
 * depending on the used gun's config.
 * By default (and in all cases), enemies are filtered according to the loaded distance predicate.
 * Many other limitations are represented with various flags, which are:
 * <p>
 * DIFFERENT_CELL --> shows only enemies that are in different cells
 * as compared to the enemies already chosen
 * <p>
 * VISIBLE_FROM_PREVIOUS_ENEMY --> shows only enemies that are visible from the last chosen enemy
 * <p>
 * SAME_DIRECTION --> shows only enemies that are (as compared to the killer)
 * in the same direction in which the last chosen enemy is
 *
 * ON_CHOSEN_CELL --> filters enemies according to a predicate loaded from
 * gun's config. This predicate depends on a previous chosen cell.
 */
public class ChoosePlayersToHitFlowNode implements ControllerFlowNode<GunFlowState> {
  public static final String DIFFERENT_CELL = "cell_different_from_previous";
  public static final String VISIBLE_FROM_PREVIOUS_ENEMY = "visible_from_previous_enemy";
  public static final String SAME_DIRECTION = "same_direction";
  public static final String ON_CHOSEN_CELL = "on_chosen_cell";
  public static final String DEFAULT = "default";

  @Override
  public String id() {
    return ControllerNodes.CHOOSE_PLAYER_TO_HIT.name();
  }

  @Override
  public void onJump(GunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    if (flowState.getChosenPlayersToHit().size() == resolveHittablePlayersNumber(flowState)) {
      context.nextPhase(view, flowState);
    } else {
      TriPredicate<PlayerColor, PlayerColor, GameModel> predicate = resolveDistanceEvalPredicate(flowState);
      List<PlayerColor> hittable;

      if (resolveHittablePlayersRestriction(flowState).equals(DIFFERENT_CELL)) {
        Set<Position> positionsToFilter = new HashSet<>();

        if (!flowState.getChosenPlayersToHit().isEmpty()) {
          flowState.getChosenPlayersToHit()
            .forEach(player -> positionsToFilter.add(model.getPlayerPosition(player)));
        }

        hittable =
          model
            .getPlayers()
            .stream()
            .filter(notIn(flowState.getChosenPlayersToHit()))
            .filter(notIn(context.getKilledPlayers()))
            .filter(notIn(Collections.singletonList(context.getTurnOfPlayer())))
            .filter(p -> predicate.test(context.getTurnOfPlayer(), p, model))
            .filter(player -> !positionsToFilter.contains(model.getPlayerPosition(player)))
            .collect(Collectors.toList());

      } else if (resolveHittablePlayersRestriction(flowState).equals(VISIBLE_FROM_PREVIOUS_ENEMY) && !flowState.getChosenPlayersToHit().isEmpty()) {
        PlayerColor previousEnemy = flowState.getChosenPlayersToHit().get(flowState.getChosenPlayersToHit().size() - 1);
        hittable = //players that are visible from last chosen enemy position
          model
            .getPlayers()
            .stream()
            .filter(notIn(flowState.getChosenPlayersToHit()))
            .filter(notIn(context.getKilledPlayers()))
            .filter(notIn(Collections.singletonList(context.getTurnOfPlayer())))
            .filter(newEnemy ->
              model.getDashboard().calculateIfVisible(model.getPlayerPosition(previousEnemy), model.getPlayerPosition(newEnemy))
            )
            .collect(Collectors.toList());

      } else if (resolveHittablePlayersRestriction(flowState).equals(SAME_DIRECTION) && !flowState.getChosenPlayersToHit().isEmpty()) {
        PlayerColor previousEnemy = flowState.getChosenPlayersToHit().get(flowState.getChosenPlayersToHit().size() - 1);
        CardinalDirection previousEnemyCardinalDirection =
          model
            .getDashboard()
            .calculateCardinalDirection(model.getPlayerPosition(context.getTurnOfPlayer()), model.getPlayerPosition(previousEnemy));

        hittable =
          model
            .getPlayers()
            .stream()
            .filter(notIn(flowState.getChosenPlayersToHit()))
            .filter(notIn(context.getKilledPlayers()))
            .filter(notIn(Collections.singletonList(context.getTurnOfPlayer())))
            .filter(enemy -> predicate.test(previousEnemy, enemy, model))
            .filter(enemy ->
              previousEnemyCardinalDirection == CardinalDirection.SAME_CELL ?
                model.getDashboard().calculateCardinalDirection(model.getPlayerPosition(context.getTurnOfPlayer()), model.getPlayerPosition(enemy)) != CardinalDirection.NONE :
                model.getDashboard().calculateCardinalDirection(model.getPlayerPosition(context.getTurnOfPlayer()), model.getPlayerPosition(enemy)) == previousEnemyCardinalDirection
            )
            .collect(Collectors.toList());

      } else if (resolveHittablePlayersRestriction(flowState).equals(SAME_DIRECTION)) {
        hittable =
          model
            .getPlayers()
            .stream()
            .filter(notIn(context.getKilledPlayers()))
            .filter(notIn(Collections.singletonList(context.getTurnOfPlayer())))
            .filter(enemy -> predicate.test(context.getTurnOfPlayer(), enemy, model))
            .filter(enemy ->
              model.getDashboard().calculateCardinalDirection(model.getPlayerPosition(context.getTurnOfPlayer()), model.getPlayerPosition(enemy)) != CardinalDirection.NONE
            )
            .collect(Collectors.toList());

      } else if (resolveHittablePlayersRestriction(flowState).equals(ON_CHOSEN_CELL)) {
        TriPredicate<Position, Position, GameModel> predicateForCells = resolveDistanceEvalForCellsPredicate(flowState);
        Position chosenCellPosition = flowState.getChosenCellsToHit().get(0);
        hittable =
          model
            .getPlayers()
            .stream()
            .filter(notIn(flowState.getChosenPlayersToHit()))
            .filter(notIn(context.getKilledPlayers()))
            .filter(notIn(Collections.singletonList(context.getTurnOfPlayer())))
            .filter(enemy -> predicateForCells.test(chosenCellPosition, model.getPlayerPosition(enemy), model))
            .collect(Collectors.toList());

      } else {
        hittable =
          model
            .getPlayers()
            .stream()
            .filter(notIn(flowState.getChosenPlayersToHit()))
            .filter(notIn(context.getKilledPlayers()))
            .filter(notIn(Collections.singletonList(context.getTurnOfPlayer())))
            .filter(p -> predicate.test(context.getTurnOfPlayer(), p, model))
            .collect(Collectors.toList());
      }

      if (hittable.isEmpty()) context.nextPhase(view, flowState);
      else view.showChoosePlayerToHit(hittable);
    }
  }

  @Override
  public void handleEvent(ViewEvent event, GunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onPlayerChosenEvent(playerChosenEvent -> {
      if (playerChosenEvent.getPlayerColor() == null) context.nextPhase(view, flowState);
      else {
        flowState.getChosenPlayersToHit().add(playerChosenEvent.getPlayerColor());
        context.replayNode(view);
      }
    });
  }

  private int resolveHittablePlayersNumber(GunFlowState flowState) {
    return flowState.resolvePhaseConfiguration(id()).get("hittablePlayersNumber").asInt();
  }

  private TriPredicate<PlayerColor, PlayerColor, GameModel> resolveDistanceEvalPredicate(GunFlowState flowState) {
    return JsonUtils.parseDistanceEvalPredicate(
      flowState.resolvePhaseConfiguration(id()).get("distanceEval").asText()
    );
  }

  private TriPredicate<Position, Position, GameModel> resolveDistanceEvalForCellsPredicate(GunFlowState flowState) {
    return JsonUtils.parseDistanceEvalForCellsPredicate(
      flowState.resolvePhaseConfiguration(id()).get("distanceEval").asText()
    );
  }

  private String resolveHittablePlayersRestriction(GunFlowState flowState) {
    if (!flowState.resolvePhaseConfiguration(id()).has("restriction"))
      return DEFAULT;

    return flowState.resolvePhaseConfiguration(id()).get("restriction").asText();
  }
}
