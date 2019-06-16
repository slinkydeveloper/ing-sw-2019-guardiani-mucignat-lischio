package com.adrenalinici.adrenaline.server.controller.guns;

import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.server.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.server.controller.GunLoader;
import com.adrenalinici.adrenaline.server.controller.nodes.TestControllerFlowContext;
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
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class BaseGunTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  GameModel model;
  FlowOrchestrator<ControllerFlowContext> orchestrator;
  TestControllerFlowContext context;

  AtomicBoolean endCalled;

  @Mock
  GameView viewMock;

  @Before
  public void setUp() {
    this.model = new GameModel(8, TestUtils.build3x3Dashboard(), TestUtils.generate4PlayerDashboards(), true);
    this.endCalled = new AtomicBoolean(false);

    List<FlowNode> nodes = new ArrayList<>();
    nodes.addAll(nodes());
    nodes.addAll(GunLoader.INSTANCE.getAdditionalNodes(gunId()));

    this.orchestrator = new FlowOrchestratorImpl<>(nodes, model, v -> endCalled.set(true));
    this.context = new TestControllerFlowContext(
      nodes.stream().map(FlowNode::id).collect(Collectors.toList()),
      this.orchestrator,
      GunLoader.INSTANCE.getDecoratedGun(gunId()).getPhases()
    );
  }

  protected abstract List<FlowNode> nodes();

  protected abstract String gunId();

  public void checkEndCalled() {
    assertThat(endCalled).isTrue();
  }

}
