package com.adrenalinici.adrenaline.util;

import com.adrenalinici.adrenaline.model.AmmoColor;
import com.adrenalinici.adrenaline.model.Effect;
import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class JsonUtils {

  public static final ObjectMapper mapper = new ObjectMapper();

  private static ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");

  public static JsonNode getConfigurationJSONFromClasspath(String filename) {
    try {
      return mapper.readTree(JsonUtils.class.getResourceAsStream(filename));
    } catch (IOException e) {
      System.err.println("You dumb, you miss file " + filename + " in classpath");
      e.printStackTrace();
      return null;
    }
  }

  public static AmmoColor parseAmmoColor(JsonNode node) {
    return AmmoColor.valueOf(node.asText().toUpperCase());
  }

  public static List<AmmoColor> parseListAmmoColor(JsonNode node) {
    return StreamSupport.stream(node.spliterator(), false).map(JsonUtils::parseAmmoColor).collect(Collectors.toList());
  }

  public static Effect parseEffect(JsonNode node) {
    return node == null ? null : new Effect(
      node.get("id").asText(),
      node.get("name").asText(),
      node.get("description").asText()
    );
  }

  public static List<AmmoColor> parseEffectCost(JsonNode node) {
    return node == null || !node.has("cost") ? null : parseListAmmoColor(node.get("cost"));
  }

  public static BiPredicate<PlayerColor, GameModel> parseDistanceEvalPredicate(ObjectNode config) {
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
