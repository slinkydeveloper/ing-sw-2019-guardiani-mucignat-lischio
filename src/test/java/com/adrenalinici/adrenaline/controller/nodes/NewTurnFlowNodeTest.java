package com.adrenalinici.adrenaline.controller.nodes;

import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.view.event.NewTurnEvent;
import org.junit.Test;

import static com.adrenalinici.adrenaline.controller.nodes.ControllerNodes.*;
import static org.assertj.core.api.Assertions.assertThat;

public class NewTurnFlowNodeTest extends BaseNodeTest {

  @Override
  public FlowNode nodeToTest() {
    return new NewTurnFlowNode();
  }

  @Test
  public void testNewTurn() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    orchestrator.startNewFlow(viewMock, context);

    orchestrator.handleEvent(new NewTurnEvent(), viewMock);

    assertThat(context.getPhasesQueue())
      .containsExactly(CHOOSE_ACTION.name());
  }

}
