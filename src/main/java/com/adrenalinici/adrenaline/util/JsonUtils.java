package com.adrenalinici.adrenaline.util;

import com.adrenalinici.adrenaline.controller.nodes.guns.GunFlowState;
import com.adrenalinici.adrenaline.flow.FlowState;
import com.adrenalinici.adrenaline.model.common.*;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class JsonUtils {

  private static final Logger LOG = LogUtils.getLogger(JsonUtils.class);

  public static final ObjectMapper mapper = new ObjectMapper();

  private static ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");

  public static JsonNode getConfigurationJSONFromClasspath(String filename) {
    try {
      if (JsonUtils.class.getResourceAsStream("/" + filename) == null)
        throw new IllegalStateException("You dumb, you miss file " + filename + " in classpath");
      return mapper.readTree(JsonUtils.class.getResourceAsStream("/" + filename));
    } catch (IOException e) {
      LOG.log(Level.SEVERE, "You dumb, you miss file " + filename + " in classpath", e);
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
    return node == null || node.isNull() ? null : new Effect(
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

  public static JsonPointer pointer(JsonPointer head, String... keys) {
    return head.append(pointer(keys));
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
        LOG.log(Level.SEVERE, "You dumb! The script is wrong! " + serializedPredicate, e);
        return false;
      }
    };
  }

  public static Predicate<FlowState> parseEnabledPredicate(String serializedPredicate) {
    return (flowState) -> {
      try {
        Bindings bindings = scriptEngine.createBindings();
        bindings.put("state", flowState);
        return (boolean) scriptEngine.eval(serializedPredicate, bindings);

      } catch (ScriptException | ClassCastException e) {
        LOG.log(Level.SEVERE, "You dumb! The script is wrong! " + serializedPredicate, e);
        return false;
      }
    };
  }

  public static Predicate<FlowState> resolveEnabledPredicate(String nodeId, GunFlowState flowState) {
    JsonNode config = flowState.resolvePhaseConfiguration(nodeId);
    if (config != null && config.has("enabled"))
      return JsonUtils.parseEnabledPredicate(
        flowState.resolvePhaseConfiguration(nodeId).get("enabled").asText()
      );
    else
      return f -> false;
  }

  /**
   * This function merges two objects with following rules. When there are two conflicting keys:
   * <ul>
   * <li>If both values are numbers, the values are summed </li>
   * <li>In other cases, the child value overrides parent value</li>
   * </ul>
   *
   * @param parent
   * @param child
   * @return
   */
  public static ObjectNode mergeConfigs(ObjectNode parent, ObjectNode child) {
    ObjectNode result = JsonNodeFactory.instance.objectNode();
    if (parent != null)
      parent.fieldNames().forEachRemaining(s -> result.set(s, parent.get(s)));
    if (child != null)
      child.fieldNames().forEachRemaining(s -> {
        if (s.startsWith("+") && result.has(s.substring(1)) && result.get(s.substring(1)).isNumber()) {
          result.set(s.substring(1), JsonNodeFactory.instance.numberNode(result.get(s.substring(1)).asDouble() + child.get(s).asDouble()));
        } else {
          result.set(s, child.get(s));
        }

      });
    return result;
  }

  public static List<PowerUpCard> loadPowerUpCards() {
    ArrayNode node = (ArrayNode) getConfigurationJSONFromClasspath("powerup_cards.json");
    return StreamUtils
      .iteratorStream(node.elements())
      .map(j -> (ObjectNode)j)
      .map(j -> new PowerUpCard(
        AmmoColor.valueOf(j.get("color").asText()),
        PowerUpType.valueOf(j.get("type").asText())
      ))
      .collect(Collectors.toList());
  }

}
