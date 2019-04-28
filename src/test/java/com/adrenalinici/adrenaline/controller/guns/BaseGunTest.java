package com.adrenalinici.adrenaline.controller.guns;

import com.adrenalinici.adrenaline.controller.ControllerFlowContext;
import com.adrenalinici.adrenaline.controller.GunFactory;
import com.adrenalinici.adrenaline.controller.GunLoader;
import com.adrenalinici.adrenaline.controller.nodes.TestControllerFlowContext;
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

public abstract class BaseGunTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  GameModel model;
  FlowOrchestrator<ControllerFlowContext> orchestrator;
  TestControllerFlowContext context;
  GunLoader gunLoader;

  AtomicBoolean endCalled;

  @Mock
  GameView viewMock;

  @Before
  public void setUp() {
    this.model = new GameModel(8, TestUtils.build3x3Dashboard(), TestUtils.generate4PlayerDashboards());
    this.endCalled = new AtomicBoolean(false);
    this.gunLoader = createGunLoader();

    GunLoader loader = createGunLoader();
    List<FlowNode> nodes = new ArrayList<>();
    nodes.addAll(nodes());
    nodes.addAll(loader.getAdditionalNodes(gunId()));

    this.orchestrator = new FlowOrchestratorImpl<>(nodes, model, v -> endCalled.set(true));
    this.context = new TestControllerFlowContext(
      nodes.stream().map(FlowNode::id).collect(Collectors.toList()),
      this.orchestrator,
      loader.getDecoratedGun(gunId()).getPhases(),
      gunLoader
    );
  }

  protected GunLoader createGunLoader() {
    return new GunLoader(Collections.singletonList(gunFactory()));
  }

  protected abstract GunFactory gunFactory();

  protected abstract List<FlowNode> nodes();

  protected abstract String gunId();

  public void checkEndCalled() {
    assertThat(endCalled).isTrue();
  }

}
