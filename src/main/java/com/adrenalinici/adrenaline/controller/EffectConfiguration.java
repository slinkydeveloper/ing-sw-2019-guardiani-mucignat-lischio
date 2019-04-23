package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.Effect;
import com.adrenalinici.adrenaline.model.GameStatus;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.function.BiPredicate;

public class EffectConfiguration {

  private Effect effect;
  private BiPredicate<PlayerColor, GameStatus> distancePredicate;
  /**
   * Null means infinite (managed by effect application)
   */
  private Integer hittablePlayers;
  private Integer distanceMovementAfterHit;

  public EffectConfiguration(Effect effect, BiPredicate<PlayerColor, GameStatus> distancePredicate, Integer hittablePlayers, Integer distanceMovementAfterHit) {
    this.effect = effect;
    this.distancePredicate = distancePredicate;
    this.hittablePlayers = hittablePlayers;
    this.distanceMovementAfterHit = distanceMovementAfterHit;
  }

  public static EffectConfiguration fromJson(Effect effect, ObjectNode node) {
    JsonNode hittablePlayersNode = node.get("hittablePlayers");
    JsonNode distanceMovementAfterHit = node.get("distanceMovementAfterHit");
    return new EffectConfiguration(
      effect,
      createDistanceEvalPredicate(node),
      hittablePlayersNode != null && hittablePlayersNode.asInt() != 0 ? hittablePlayersNode.asInt() : null,
      distanceMovementAfterHit != null && distanceMovementAfterHit.asInt() != 0 ? distanceMovementAfterHit.asInt() : null
    );
  }

  public Effect getEffect() {
    return effect;
  }

  //TODO awwww it was so beautiful but shit to remove
  // https://media.giphy.com/media/ZyPbHP9qk86GY/giphy.gif
  public BiPredicate<PlayerColor, GameStatus> getDistancePredicate() {
    return distancePredicate;
  }

  //TODO shit to remove
  public Integer getHittablePlayers() {
    return hittablePlayers;
  }

  //TODO shit to remove
  public Integer getDistanceMovementAfterHit() {
    return distanceMovementAfterHit;
  }

  private static ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");

  public static BiPredicate<PlayerColor, GameStatus> createDistanceEvalPredicate(ObjectNode config) {
    if (config.has("distanceEval"))
      return (playerColor, gameStatus) -> {
        int distance = 1;//TODO
        boolean visible = true;//TODO
        boolean throughWall = true;//TODO
        try {
          return (boolean) scriptEngine.eval(String.format(
            "var distance=%d;\nvar visible=%s;var throughWall=%s;%s",
            distance,
            Boolean.toString(visible),
            Boolean.toString(throughWall),
            config.get("distanceEval").toString()
          ));
        } catch (ScriptException | ClassCastException e) {
          System.err.println("You dumb!");
          e.printStackTrace();
          return false;
        }
      };
    else return null;
  }
}
