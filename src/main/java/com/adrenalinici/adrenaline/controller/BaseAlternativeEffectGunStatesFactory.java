package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.AlternativeEffectGun;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.util.ListUtils;
import com.adrenalinici.adrenaline.view.event.AlternativeGunEffectChosenEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseAlternativeEffectGunStatesFactory implements GunStatesFactory<AlternativeEffectGun> {

  public static String EFFECT_KEY = "effect";
  public static String CHOOSEN_PLAYERS_TO_HIT_KEY = "choosen_players_to_hit";
  public static String CHOOSEN_ONE_PLAYER_TO_HIT_KEY = "choosen_one_player_to_hit";
  public static String PLAYERS_HIT = "players_hit";

  public static String CHOOSE_MULTIPLE_PLAYERS_LOOP_START_CONDITION = "choose_player_to_hit_loop_condition";
  public static String CHOOSE_MULTIPLE_PLAYERS_LOOP_START_LABEL = "choose_player_to_hit_loop_start";
  public static String CHOOSE_SINGLE_PLAYER_LABEL = "choose_player_to_hit";
  public static String AFTER_GUN_CONFIGURATION_LABEL = "after_choose_player";

  private String id;
  private GunFlowOrchestrator.Builder cachedBuilder;

  public BaseAlternativeEffectGunStatesFactory(String id) {
    this.id = id;
  }

  @Override
  public boolean canConsume(String key, JsonNode config) {
    return key.equals(id);
  }

  @Override
  public ControllerState create(AlternativeEffectGun modelGun, JsonNode gunConfig) {
    if (cachedBuilder != null) return cachedBuilder.build();
    GunFlowOrchestrator.Builder builder = GunFlowOrchestrator.newBuilder();
    pickAlternativeEffectFlow(builder, modelGun, gunConfig);
    configurationPreFlow(builder, modelGun, gunConfig);
    configurationFlow(builder, modelGun, gunConfig);
    configurationPostFlow(builder, modelGun, gunConfig);
    applyPreFlow(builder, modelGun, gunConfig);
    applyFlow(builder, modelGun, gunConfig);
    applyPostFlow(builder, modelGun, gunConfig);
    cachedBuilder = builder;
    return builder.build();
  }

  void pickAlternativeEffectFlow(GunFlowOrchestrator.Builder builder, AlternativeEffectGun modelGun, JsonNode gunConfig) {
    builder
      .withState(pickAlternativeEffect(modelGun))
      .withState(alternativeEffectChosen(modelGun, (ObjectNode) gunConfig));
  }

  abstract void configurationPreFlow(GunFlowOrchestrator.Builder builder, AlternativeEffectGun modelGun, JsonNode gunConfig);

  abstract void configurationFlow(GunFlowOrchestrator.Builder builder, AlternativeEffectGun modelGun, JsonNode gunConfig);

  abstract void configurationPostFlow(GunFlowOrchestrator.Builder builder, AlternativeEffectGun modelGun, JsonNode gunConfig);

  abstract void applyPreFlow(GunFlowOrchestrator.Builder builder, AlternativeEffectGun modelGun, JsonNode gunConfig);

  abstract void applyFlow(GunFlowOrchestrator.Builder builder, AlternativeEffectGun modelGun, JsonNode gunConfig);

  abstract void applyPostFlow(GunFlowOrchestrator.Builder builder, AlternativeEffectGun modelGun, JsonNode gunConfig);

  private static GunState pickAlternativeEffect(AlternativeEffectGun g) {
    return (event, controller, gunFlowOrchestrator) -> {
      event.onGunChosenEvent(gunChosenEvent -> {
        boolean canUseSecondEffect = false;//TODO
        if (canUseSecondEffect) {
          event.getView().showAvailableAlternativeEffectsGun(g.getFirstEffect(), g.getSecondEffect());
        } else {
          gunFlowOrchestrator.nextNow(new AlternativeGunEffectChosenEvent(event.getView(), false), controller);
        }
      });
    };
  }

  private static GunState alternativeEffectChosen(AlternativeEffectGun g, ObjectNode gunConfig) {
    return (event, controller, orchestrator) -> {
      event.onAlternativeGunEffectChosenEvent(alternativeGunEffectChosenEvent -> {
        orchestrator.setData(EFFECT_KEY, EffectConfiguration.fromJson(
          alternativeGunEffectChosenEvent.chosenFirstEffect() ?
            g.getFirstEffect() :
            g.getSecondEffect(),
          alternativeGunEffectChosenEvent.chosenFirstEffect() ?
            (ObjectNode) gunConfig.get("firstEffect") :
            (ObjectNode) gunConfig.get("secondEffect"))
        );
        orchestrator.nextNow(event, controller);
      });
    };
  }

  //TODO maybe this goes on GunStatesFactory
  static void mountPlayersToHitLoop(GunFlowOrchestrator.Builder builder, int hittablePlayers) {
    builder
      .withState((event, controller, orchestrator) -> {
        orchestrator.setData(CHOOSEN_PLAYERS_TO_HIT_KEY, new ArrayList<PlayerColor>());
        orchestrator.setData("playersToHitNumber", hittablePlayers);
        orchestrator.jumpToNow(CHOOSE_MULTIPLE_PLAYERS_LOOP_START_CONDITION, event, controller);
      }, CHOOSE_MULTIPLE_PLAYERS_LOOP_START_LABEL)
      .withState((event, controller, orchestrator) -> {
        int p = orchestrator.getData("playersToHitNumber");
        if (p > 0)
          orchestrator.nextNow(event, controller);
        else
          orchestrator.jumpToNow(AFTER_GUN_CONFIGURATION_LABEL, event, controller);
      }, CHOOSE_MULTIPLE_PLAYERS_LOOP_START_CONDITION)
      .withState((event, controller, orchestrator) -> {
        EffectConfiguration config = orchestrator.getData("effect");

        List<PlayerColor> hittable =
          ListUtils.differencePure(
            controller
              .getGameStatus()
              .getPlayers()
              .stream()
              .filter(p -> config.getDistancePredicate().test(p, controller.getGameStatus()))
              .collect(Collectors.toList()),
            orchestrator.getData(CHOOSEN_PLAYERS_TO_HIT_KEY)
          );
        event.getView().showChoosePlayerToHit(hittable);
        orchestrator.nextNow(event, controller);
      })
      .withState((event, controller, orchestrator) -> {
        event.onPlayerChosenEvent(playerChosenEvent -> {
          List<PlayerColor> playersToHit = orchestrator.getData(CHOOSEN_PLAYERS_TO_HIT_KEY);
          if (playersToHit == null) {
            orchestrator.jumpToNow(AFTER_GUN_CONFIGURATION_LABEL, event, controller);
          } else {
            playersToHit.add(playerChosenEvent.getPlayerColor());
            orchestrator.jumpToNow(CHOOSE_MULTIPLE_PLAYERS_LOOP_START_CONDITION, event, controller);
          }
        });
      });
  }

  //TODO maybe this goes on GunStatesFactory
  static void mountOnePlayerToHitLoop(GunFlowOrchestrator.Builder builder) {
    builder
      .withState((event, controller, orchestrator) -> {
        EffectConfiguration config = orchestrator.getData("effect");
        List<PlayerColor> hittable =
          controller
            .getGameStatus()
            .getPlayers()
            .stream()
            .filter(p -> config.getDistancePredicate().test(p, controller.getGameStatus()))
            .collect(Collectors.toList());

        event.getView().showChoosePlayerToHit(hittable);
        orchestrator.nextNow(event, controller);
      }, CHOOSE_SINGLE_PLAYER_LABEL)
      .withState((event, controller, orchestrator) -> {
        event.onPlayerChosenEvent(playerChosenEvent -> {
          if (playerChosenEvent.getPlayerColor() == null) {
            orchestrator.jumpToNow(CHOOSE_SINGLE_PLAYER_LABEL, event, controller);
          } else {
            orchestrator.setData(CHOOSEN_ONE_PLAYER_TO_HIT_KEY, playerChosenEvent.getPlayerColor());
            orchestrator.jumpToNow(AFTER_GUN_CONFIGURATION_LABEL, event, controller);
          }
        });
      });
  }

  static void mountVenomGrenadeFLow(GunFlowOrchestrator.Builder builder) {
    builder
      .withState((event, controller, orchestrator) -> {
        List<PlayerColor> players = orchestrator.computeIfAbsent(PLAYERS_HIT, ArrayList::new);
        if (players.isEmpty()) {

        }
      });
  }

}
