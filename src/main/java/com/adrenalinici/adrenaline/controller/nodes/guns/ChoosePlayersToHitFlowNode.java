package com.adrenalinici.adrenaline.controller.nodes.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.ControllerFlowNode;
import com.adrenalinici.adrenaline.controller.GunLoader;
import com.adrenalinici.adrenaline.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.util.JsonUtils;
import com.adrenalinici.adrenaline.util.TriPredicate;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.adrenalinici.adrenaline.util.JsonUtils.pointer;
import static com.adrenalinici.adrenaline.util.ListUtils.notIn;

public class ChoosePlayersToHitFlowNode implements ControllerFlowNode<AlternativeEffectGunFlowState> {
  @Override
  public String id() {
    return ControllerNodes.CHOOSE_PLAYER_TO_HIT.name();
  }

  @Override
  public void onJump(AlternativeEffectGunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    if (flowState.getChosenPlayersToHit().size() == resolveHittablePlayers(flowState.getChosenGun().getId(), flowState.isFirstEffect())) {
      context.nextPhase(view, flowState);
    } else {
      TriPredicate<PlayerColor, PlayerColor, GameModel> predicate = resolvePlayerPredicate(flowState.getChosenGun().getId(), flowState.isFirstEffect());
      List<PlayerColor> hittable =
        model
          .getPlayers()
          .stream()
          .filter(notIn(flowState.getChosenPlayersToHit()))
          .filter(notIn(context.getKilledPlayers()))
          .filter(notIn(Collections.singletonList(context.getTurnOfPlayer())))
          .filter(p -> predicate.test(context.getTurnOfPlayer(), p, model))
          .collect(Collectors.toList());
      if (hittable.isEmpty()) context.nextPhase(view, flowState);
      else view.showChoosePlayerToHit(hittable);
    }
  }

  @Override
  public void handleEvent(ViewEvent event, AlternativeEffectGunFlowState flowState, GameView view, GameModel model, ControllerFlowContext context) {
    event.onPlayerChosenEvent(playerChosenEvent -> {
      if (playerChosenEvent.getPlayerColor() == null) context.nextPhase(view, flowState);
      else {
        flowState.getChosenPlayersToHit().add(playerChosenEvent.getPlayerColor());
        context.replayNode(view);
      }
    });
  }

  private int resolveHittablePlayers(String gunId, boolean firstEffect) {
    return GunLoader.config.at(
      pointer(gunId, firstEffect ? "firstEffect" : "secondEffect", "phases", id(), "hittablePlayersNumber")
    ).asInt();
  }

  private TriPredicate<PlayerColor, PlayerColor, GameModel> resolvePlayerPredicate(String gunId, boolean firstEffect) {
    return JsonUtils.parseDistanceEvalPredicate(GunLoader.config.at(
      pointer(gunId, firstEffect ? "firstEffect" : "secondEffect", "phases", id(), "distanceEval")
    ).asText());
  }
}
