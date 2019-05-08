package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.controller.nodes.NewTurnFlowNode;
import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.testutil.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unchecked")
public class GameControllerTest {

  GameModel status;

  @Before
  public void setUp() {
    status = TestUtils.generateModel();
  }

  @Test
  public void testControllerInstantiation() {
    GameController controller = new GameController(
      Collections.singletonList(new NewTurnFlowNode()),
      status,
      GunLoader.INSTANCE
    );

    assertThat(controller.getFlowContext().getTurnOfPlayer())
      .isEqualTo(PlayerColor.GREEN);
  }
}
