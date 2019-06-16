package com.adrenalinici.adrenaline.server.controller.nodes;

import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.flow.FlowNode;
import com.adrenalinici.adrenaline.server.flow.FlowOrchestrator;
import com.adrenalinici.adrenaline.server.flow.impl.FlowOrchestratorImpl;
import com.adrenalinici.adrenaline.server.model.GameModel;
import com.adrenalinici.adrenaline.server.testutil.TestUtils;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class BaseNodeTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  public GameModel model;
  public FlowOrchestrator<ControllerFlowContext> orchestrator;
  public TestControllerFlowContext context;

  public AtomicBoolean endCalled;

  @Mock
  public GameView viewMock;

  @Before
  public void setUp() {
    this.model = TestUtils.generateModelWith4Players();
    this.endCalled = new AtomicBoolean(false);

    FlowNode node = nodeToTest();
    List<FlowNode> nodes = new ArrayList<>();
    nodes.add(node);
    nodes.addAll(additionalNodes());

    this.orchestrator = new FlowOrchestratorImpl<>(nodes, model, v -> endCalled.set(true));
    this.context = new TestControllerFlowContext(
      nodes.stream().map(FlowNode::id).collect(Collectors.toList()),
      this.orchestrator,
      Collections.singletonList(node.id())
    );
  }

  public abstract FlowNode nodeToTest();

  protected List<FlowNode> additionalNodes() {
    return Collections.emptyList();
  }

  public void checkEndCalled() {
    assertThat(endCalled).isTrue();
  }

}
