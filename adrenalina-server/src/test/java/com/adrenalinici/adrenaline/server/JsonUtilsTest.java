package com.adrenalinici.adrenaline.server;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.util.TriPredicate;
import com.adrenalinici.adrenaline.server.model.GameModel;
import com.adrenalinici.adrenaline.server.testutil.TestUtils;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonUtilsTest {

  public GameModel model;

  @Before
  public void setUp() {
    this.model = TestUtils.generateModel();
    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 2)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(1, 2)).addPlayer(PlayerColor.GRAY);
  }

  @Test
  public void testParseDistanceEvalPredicate() {
    TriPredicate<PlayerColor, PlayerColor, GameModel> predicate =
      JsonUtils.parseDistanceEvalPredicate("distance <= 2");

    Assertions.assertThat(predicate.test(PlayerColor.GREEN, PlayerColor.YELLOW, model)).isTrue();
    Assertions.assertThat(predicate.test(PlayerColor.GREEN, PlayerColor.GRAY, model)).isFalse();
  }

  @Test
  public void testMergeConfigs() {
    ObjectNode parent = JsonNodeFactory.instance
      .objectNode()
      .put("bla", "a")
      .put("blaOverride", "a")
      .put("number1", 1)
      .put("sum", 2);
    ObjectNode child = JsonNodeFactory.instance
      .objectNode()
      .put("specificBla", "c")
      .put("blaOverride", "b")
      .put("number2", 2)
      .put("+sum", 2);

    ObjectNode result = JsonUtils.mergeConfigs(parent, child);

    assertThat(result.get("bla").asText()).isEqualTo("a");
    assertThat(result.get("specificBla").asText()).isEqualTo("c");
    assertThat(result.get("blaOverride").asText()).isEqualTo("b");
    assertThat(result.get("number1").asInt()).isEqualTo(1);
    assertThat(result.get("number2").asInt()).isEqualTo(2);
    assertThat(result.get("sum").asInt()).isEqualTo(4);
  }

  @Test
  public void testMergeConfigsOneOfTwoNull() {
    ObjectNode object = JsonNodeFactory.instance
      .objectNode()
      .put("bla", "a")
      .put("blaOverride", "a")
      .put("number1", 1)
      .put("sum", 2);

    assertThat(JsonUtils.mergeConfigs(object, null)).isEqualTo(object);
    assertThat(JsonUtils.mergeConfigs(null, object)).isEqualTo(object);
  }

  @Test
  public void testMergeConfigsBothNull() {
    assertThat(JsonUtils.mergeConfigs(null, null))
      .isNotNull();
  }

}
