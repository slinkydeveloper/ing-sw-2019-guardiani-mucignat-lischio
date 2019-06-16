package com.adrenalinici.adrenaline.server.controller;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.PowerUpCard;
import com.adrenalinici.adrenaline.common.util.DecoratedEvent;
import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.PowerUpCardChosenEvent;
import com.adrenalinici.adrenaline.common.view.StartMatchEvent;
import com.adrenalinici.adrenaline.server.controller.nodes.FirstTurnFlowNode;
import com.adrenalinici.adrenaline.server.controller.nodes.NewTurnFlowNode;
import com.adrenalinici.adrenaline.server.controller.nodes.RespawnFlowNode;
import com.adrenalinici.adrenaline.server.model.GameModel;
import com.adrenalinici.adrenaline.server.testutil.TestUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.List;

import static com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes.RESPAWN_KILLED_PEOPLE;
import static com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes.START_TURN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class GameControllerTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  GameView viewMock;

  GameModel status;

  @Before
  public void setUp() {
    status = TestUtils.generateModel();
  }

  @Test
  public void testFirstTurn() {
    NewTurnFlowNode newTurnFlowNode = new NewTurnFlowNode();

    GameController controller = new GameController(
      Arrays.asList(newTurnFlowNode, new FirstTurnFlowNode(), new RespawnFlowNode()),
      status
    );

    controller.onEvent(new DecoratedEvent<>(new StartMatchEvent(), viewMock));

    assertThat(controller.getFlowContext().actualPhase()).isEqualTo(RESPAWN_KILLED_PEOPLE.name());

    ArgumentCaptor<PlayerColor> playerColorCaptor = ArgumentCaptor.forClass(PlayerColor.class);
    ArgumentCaptor<List<PowerUpCard>> powerUpCardCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(3)).showAvailablePowerUpCardsForRespawn(playerColorCaptor.capture(), powerUpCardCaptor.capture());
    assertThat(playerColorCaptor.getAllValues())
      .containsExactly(PlayerColor.GREEN, PlayerColor.GRAY, PlayerColor.YELLOW);
    powerUpCardCaptor.getAllValues().forEach(l -> assertThat(l).hasSize(2));

    PlayerColor p1 = playerColorCaptor.getAllValues().get(0);
    PowerUpCard p1Card = powerUpCardCaptor.getAllValues().get(0).get(0);
    PlayerColor p2 = playerColorCaptor.getAllValues().get(1);
    PowerUpCard p2Card = powerUpCardCaptor.getAllValues().get(1).get(0);
    PlayerColor p3 = playerColorCaptor.getAllValues().get(2);
    PowerUpCard p3Card = powerUpCardCaptor.getAllValues().get(2).get(0);

    controller.onEvent(new DecoratedEvent<>(new PowerUpCardChosenEvent(p1, p1Card), viewMock));
    controller.onEvent(new DecoratedEvent<>(new PowerUpCardChosenEvent(p2, p2Card), viewMock));
    controller.onEvent(new DecoratedEvent<>(new PowerUpCardChosenEvent(p3, p3Card), viewMock));

    assertThat(controller.getFlowContext().getTurnOfPlayer()).isEqualTo(PlayerColor.GREEN);
    assertThat(controller.getFlowContext().actualPhase()).isEqualTo(START_TURN.name());
    assertThat(controller.getFlowContext().actualNode()).isSameAs(newTurnFlowNode);
  }
}
