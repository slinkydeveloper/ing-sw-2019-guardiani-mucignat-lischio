package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.model.common.Position;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.util.JsonUtils;
import com.adrenalinici.adrenaline.util.TriPredicate;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.*;
import java.util.stream.Collectors;

import static com.adrenalinici.adrenaline.util.ListUtils.notIn;

//TODO javadoc
public class ChoosePlayersToHitFlowNode implements ControllerFlowNode<GunFlowState> {
  public static String DIFFERENT_CELL = "cell_different_from_previous";
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
        flowState.getChosenPlayersToHit()
          .forEach(player -> positionsToFilter.add(model.getPlayerPosition(player)));

        hittable =
          model
            .getPlayers()
            .stream()
            .filter(notIn(flowState.getChosenPlayersToHit()))
            .filter(notIn(context.getKilledPlayers()))
            .filter(notIn(Collections.singletonList(context.getTurnOfPlayer())))
            .filter(player -> !positionsToFilter.contains(model.getPlayerPosition(player)))
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

  private String resolveHittablePlayersRestriction(GunFlowState flowState) {
    if (!flowState.resolvePhaseConfiguration(id()).has("restriction"))
      return DEFAULT;

    return flowState.resolvePhaseConfiguration(id()).get("restriction").asText();
  }
}
