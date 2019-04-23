package com.adrenalinici.adrenaline.controller.nodes;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.flow.FlowOrchestrator;
import com.adrenalinici.adrenaline.flow.impl.FlowOrchestratorImpl;
import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.testutil.TestUtils;
import com.adrenalinici.adrenaline.view.GameView;
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

  GameModel model;
  FlowOrchestrator<ControllerFlowContext> orchestrator;
  ControllerFlowContext context;

  AtomicBoolean endCalled;

  @Mock
  GameView viewMock;

  public static class NoNextPhaseControllerFlowContext extends ControllerFlowContext {

    List<String> loadedPhases;

    public NoNextPhaseControllerFlowContext(List<String> loadedPhases, FlowOrchestrator orchestrator, List<String> initialPhases) {
      super(orchestrator, initialPhases);
      this.loadedPhases = loadedPhases;
    }

    @Override
    public void nextPhase(GameView view) {
      if (!getPhasesQueue().isEmpty() && loadedPhases.contains(getPhasesQueue().get(0)))
        super.nextPhase(view);
      else
        super.end(view);
    }
  }

  @Before
  public void setUp() {
    this.model = TestUtils.generateModel();
    this.endCalled = new AtomicBoolean(false);

    FlowNode node = nodeToTest();
    List<FlowNode> nodes = new ArrayList<>();
    nodes.add(node);
    nodes.addAll(additionalNodes());

    this.orchestrator = new FlowOrchestratorImpl<>(nodes, model, v -> endCalled.set(true));
    this.context = new NoNextPhaseControllerFlowContext(
      nodes.stream().map(FlowNode::id).collect(Collectors.toList()),
      this.orchestrator,
      Collections.singletonList(node.id())
    );
  }

  public abstract FlowNode nodeToTest();

  public List<FlowNode> additionalNodes() {
    return Collections.emptyList();
  }

  public void checkEndCalled() {
    assertThat(endCalled).isTrue();
  }

}
