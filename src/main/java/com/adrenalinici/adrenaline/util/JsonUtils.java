package com.adrenalinici.adrenaline.util;

import com.adrenalinici.adrenaline.model.AmmoColor;
import com.adrenalinici.adrenaline.model.Effect;
import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.script.Bindings;
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
      return mapper.readTree(JsonUtils.class.getResourceAsStream("/" + filename));
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
    return node == null ? null : StreamSupport.stream(node.spliterator(), false).map(JsonUtils::parseAmmoColor).collect(Collectors.toList());
  }

  public static Effect parseEffect(JsonNode node) {
    return node == null ? null : new Effect(
      node.get("id").asText(),
      node.get("name").asText(),
      node.get("description").asText()
    );
  }

  public static List<AmmoColor> parseEffectCost(JsonNode node) {
    return node == null ? null : parseListAmmoColor(node.get("cost"));
  }

  public static List<String> parseListString(JsonNode node, String key) {
    return node == null || !node.has(key) ? null : StreamSupport
      .stream(node.get(key).spliterator(), false)
      .map(JsonNode::asText)
      .collect(Collectors.toList());
  }

  public static List<String> parseObjectKeys(JsonNode node, String key) {
    return node == null || !node.has(key) ? null :
      StreamUtils.iteratorStream(node.get(key).fieldNames()).collect(Collectors.toList());
  }

  public static JsonPointer pointer(String... keys) {
    return JsonPointer.compile("/" + String.join("/", keys));
  }

  public static TriPredicate<PlayerColor, PlayerColor, GameModel> parseDistanceEvalPredicate(String serializedPredicate) {
    return (playerColor1, playerColor2, gameStatus) -> {
      int distance = gameStatus.getDashboard()
        .calculateDistance(
          gameStatus.getPlayerPosition(playerColor1),
          gameStatus.getPlayerPosition(playerColor2)
        );
      boolean visible = gameStatus.getDashboard()
        .calculateIfVisible(
          gameStatus.getPlayerPosition(playerColor1),
          gameStatus.getPlayerPosition(playerColor2)
        );
      try {
        Bindings bindings = scriptEngine.createBindings();
        bindings.put("distance", distance);
        bindings.put("visible", visible);
        return (boolean) scriptEngine.eval(serializedPredicate, bindings);

      } catch (ScriptException | ClassCastException e) {
        System.err.println("You dumb!");
        e.printStackTrace();
        return false;
      }
    };
  }

}
