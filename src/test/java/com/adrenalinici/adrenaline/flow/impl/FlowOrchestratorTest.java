package com.adrenalinici.adrenaline.flow.impl;

import com.adrenalinici.adrenaline.flow.FlowContext;
import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.flow.FlowOrchestrator;
import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.view.GameView;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class FlowOrchestratorTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Mock
  GameModel model;
  @Mock
  GameView view;

  @Test
  public void testJumpInOnNextAndEnd() {
    FlowNode<VoidState, FlowContextImpl> nodeOne = mock(FlowNode.class);
    when(nodeOne.id()).thenReturn("one");
    doAnswer(invocation -> {
      FlowContext context = invocation.getArgument(3);
      context.jump("two", view, null);
      return null;
    }).when(nodeOne).onJump(any(), any(), any(), any());

    FlowNode<VoidState, FlowContextImpl> nodeTwo = mock(FlowNode.class);
    when(nodeTwo.id()).thenReturn("two");
    doAnswer(invocation -> {
      FlowContext context = invocation.getArgument(3);
      context.end(view);
      return null;
    }).when(nodeTwo).onJump(any(), any(), any(), any());

    AtomicBoolean bool = new AtomicBoolean(false);

    FlowOrchestrator<FlowContextImpl> orchestrator = new FlowOrchestratorImpl<>(
      Arrays.asList(nodeOne, nodeTwo), model, context -> bool.set(true)
    );

    orchestrator.startNewFlow(view, new FlowContextImpl(orchestrator, Collections.singletonList("one")));

    assertThat(bool).isTrue();
  }

  @Test
  public void testNextPhaseAndEnd() {
    FlowNode<VoidState, FlowContextImpl> nodeOne = mock(FlowNode.class);
    when(nodeOne.id()).thenReturn("one");
    doAnswer(invocation -> {
      FlowContext context = invocation.getArgument(3);
      context.addPhases("two");
      context.nextPhase(view);
      return null;
    }).when(nodeOne).onJump(any(), any(), any(), any());

    FlowNode<VoidState, FlowContextImpl> nodeTwo = mock(FlowNode.class);
    when(nodeTwo.id()).thenReturn("two");
    doAnswer(invocation -> {
      FlowContext context = invocation.getArgument(3);
      context.end(view);
      return null;
    }).when(nodeTwo).onJump(any(), any(), any(), any());

    AtomicBoolean bool = new AtomicBoolean(false);

    FlowOrchestrator<FlowContextImpl> orchestrator = new FlowOrchestratorImpl<>(
      Arrays.asList(nodeOne, nodeTwo), model, context -> bool.set(true)
    );

    orchestrator.startNewFlow(view, new FlowContextImpl(orchestrator, Collections.singletonList("one")));

    assertThat(bool).isTrue();
  }

  @Test
  public void testReplayPhase() {
    AtomicInteger passes = new AtomicInteger(2);

    FlowNode<VoidState, FlowContextImpl> nodeOne = mock(FlowNode.class);
    when(nodeOne.id()).thenReturn("one");
    doAnswer(invocation -> {
      FlowContext context = invocation.getArgument(3);
      context.jump("two", view, null);
      return null;
    }).when(nodeOne).onJump(any(), any(), any(), any());

    FlowNode<VoidState, FlowContextImpl> nodeTwo = mock(FlowNode.class);
    when(nodeTwo.id()).thenReturn("two");
    doAnswer(invocation -> {
      FlowContext context = invocation.getArgument(3);
      if (passes.getAndDecrement() > 0)
        context.replayPhase(view);
      else
        context.end(view);
      return null;
    }).when(nodeTwo).onJump(any(), any(), any(), any());

    AtomicBoolean bool = new AtomicBoolean(false);

    FlowOrchestrator<FlowContextImpl> orchestrator = new FlowOrchestratorImpl<>(
      Arrays.asList(nodeOne, nodeTwo), model, context -> bool.set(true)
    );

    orchestrator.startNewFlow(view, new FlowContextImpl(orchestrator, Collections.singletonList("one")));

    assertThat(bool).isTrue();
    assertThat(passes).hasValue(-1);
  }

  @Test
  public void testReplayState() {
    AtomicInteger passes = new AtomicInteger(2);

    FlowNode<VoidState, FlowContextImpl> nodeOne = mock(FlowNode.class);
    when(nodeOne.id()).thenReturn("one");
    doAnswer(invocation -> {
      FlowContext context = invocation.getArgument(3);
      context.jump("two", view, null);
      return null;
    }).when(nodeOne).onJump(any(), any(), any(), any());

    FlowNode<VoidState, FlowContextImpl> nodeTwo = mock(FlowNode.class);
    when(nodeTwo.id()).thenReturn("two");
    doAnswer(invocation -> {
      FlowContext context = invocation.getArgument(3);
      if (passes.getAndDecrement() > 0)
        context.replayNode(view);
      else
        context.end(view);
      return null;
    }).when(nodeTwo).onJump(any(), any(), any(), any());

    AtomicBoolean bool = new AtomicBoolean(false);

    FlowOrchestrator<FlowContextImpl> orchestrator = new FlowOrchestratorImpl<>(
      Arrays.asList(nodeOne, nodeTwo), model, context -> bool.set(true)
    );

    orchestrator.startNewFlow(view, new FlowContextImpl(orchestrator, Collections.singletonList("one")));

    assertThat(bool).isTrue();
    assertThat(passes).hasValue(-1);
  }

  @Test
  public void testAddPhases() {
    FlowOrchestrator<FlowContextImpl> orchestrator = new FlowOrchestratorImpl<>(Collections.emptyList(), model, context -> {
    });
    FlowContextImpl context = new FlowContextImpl(orchestrator, Collections.singletonList("z"));

    context.addPhases("a", "b");
    assertThat(context.getPhasesQueue())
      .containsExactly("z", "a", "b");
    context.addPhasesToEnd("e", "f");
    assertThat(context.getPhasesQueue())
      .containsExactly("z", "a", "b", "e", "f");
    context.addPhases("c", "d");
    assertThat(context.getPhasesQueue())
      .containsExactly("z", "a", "b", "c", "d", "e", "f");
  }

}
