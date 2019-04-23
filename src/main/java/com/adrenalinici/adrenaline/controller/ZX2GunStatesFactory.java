package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.AlternativeEffectGun;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class ZX2GunStatesFactory extends BaseAlternativeEffectGunStatesFactory {

  private static final String BASIC_EFFECT_ID = "basic";
  private static final String SCANNER_EFFECT_ID = "scanner";

  public ZX2GunStatesFactory() {
    super("zx2");
  }

  @Override
  void configurationPreFlow(GunFlowOrchestrator.Builder builder, AlternativeEffectGun modelGun, JsonNode gunConfig) {
    builder
      .withState((event, controller, orchestrator) -> {
        EffectConfiguration effect = orchestrator.getData(EFFECT_KEY);
        if (effect.getEffect().getId().equals(BASIC_EFFECT_ID)) {
          orchestrator.jumpToNow(CHOOSE_MULTIPLE_PLAYERS_LOOP_START_LABEL, event, controller);
        } else {
          orchestrator.jumpToNow(CHOOSE_SINGLE_PLAYER_LABEL, event, controller);
        }
      });
  }

  @Override
  void configurationFlow(GunFlowOrchestrator.Builder builder, AlternativeEffectGun modelGun, JsonNode gunConfig) {
    mountPlayersToHitLoop(builder, 3);
    mountOnePlayerToHitLoop(builder);
  }

  @Override
  void configurationPostFlow(GunFlowOrchestrator.Builder builder, AlternativeEffectGun modelGun, JsonNode gunConfig) {
    builder.withState(GunStatesHelpers.NOOP_GUN_STATE, AFTER_GUN_CONFIGURATION_LABEL);
  }

  @Override
  void applyPreFlow(GunFlowOrchestrator.Builder builder, AlternativeEffectGun modelGun, JsonNode gunConfig) {
  }

  @Override
  void applyFlow(GunFlowOrchestrator.Builder builder, AlternativeEffectGun modelGun, JsonNode gunConfig) {
    builder.withState((event, controller, orchestrator) -> {
      if (orchestrator.hasData(CHOOSEN_PLAYERS_TO_HIT_KEY)) {
        List<PlayerColor> players = orchestrator.getData(CHOOSEN_PLAYERS_TO_HIT_KEY);
        players.forEach(p -> controller.getGameStatus().markPlayer(p, 1));
        orchestrator.nextNow(event, controller);
      } else {
        PlayerColor player = orchestrator.getData(CHOOSEN_ONE_PLAYER_TO_HIT_KEY);
        controller.getGameStatus().markPlayer(player, 2);
        controller.getGameStatus().hitPlayer(player, 1);
        List<PlayerColor> hitPlayers = orchestrator.computeIfAbsent(PLAYERS_HIT, ArrayList::new);
        hitPlayers.add(player);
        orchestrator.nextNow(event, controller);
      }
    });
  }

  @Override
  void applyPostFlow(GunFlowOrchestrator.Builder builder, AlternativeEffectGun modelGun, JsonNode gunConfig) {
    mountVenomGrenadeFLow(builder);
  }
}
