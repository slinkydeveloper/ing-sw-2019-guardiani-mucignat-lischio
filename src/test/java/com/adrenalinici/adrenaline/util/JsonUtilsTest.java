package com.adrenalinici.adrenaline.util;

import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.model.Position;
import com.adrenalinici.adrenaline.testutil.TestUtils;
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

    assertThat(predicate.test(PlayerColor.GREEN, PlayerColor.YELLOW, model)).isTrue();
    assertThat(predicate.test(PlayerColor.GREEN, PlayerColor.GRAY, model)).isFalse();
  }
}
