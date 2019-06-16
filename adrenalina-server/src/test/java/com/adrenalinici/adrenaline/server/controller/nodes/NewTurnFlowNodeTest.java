package com.adrenalinici.adrenaline.server.controller.nodes;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.view.NewTurnEvent;
import com.adrenalinici.adrenaline.server.flow.FlowNode;
import org.junit.Test;

import static com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes.CHOOSE_ACTION;
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
