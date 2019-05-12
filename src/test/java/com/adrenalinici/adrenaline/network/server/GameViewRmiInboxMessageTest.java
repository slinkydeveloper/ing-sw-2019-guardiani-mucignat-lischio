package com.adrenalinici.adrenaline.network.server;

import com.adrenalinici.adrenaline.model.common.Action;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.util.DecoratedEvent;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ActionChosenEvent;
import com.adrenalinici.adrenaline.view.event.NewTurnEvent;
import com.adrenalinici.adrenaline.view.event.ViewEvent;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class GameViewRmiInboxMessageTest extends BaseGameViewRmiIntegrationTest {

  @Test
  public void handshakeAndStartMatch() throws IOException, InterruptedException {
    List<DecoratedEvent<ViewEvent, GameView>> receivedEventsFromView = new ArrayList<>();
    doHandshakeAndStartMatch(receivedEventsFromView);
  }

  @Test
  public void testSendViewEvent() throws IOException, InterruptedException {
    List<DecoratedEvent<ViewEvent, GameView>> receivedEventsFromView = new ArrayList<>();
    doHandshakeAndStartMatch(receivedEventsFromView);
    receivedEventsFromView.clear();

    // Now the match is started, I can send a ViewEvent
    mockedClientView.sendViewEvent(new ActionChosenEvent(Action.MOVE_MOVE_MOVE));

    Thread.sleep(500);

    assertThat(receivedEventsFromView)
      .hasOnlyOneElementSatisfying(d -> assertThat(d.getInnerEvent())
        .isInstanceOfSatisfying(ActionChosenEvent.class, ace -> assertThat(ace.getAction()).isEqualTo(Action.MOVE_MOVE_MOVE)));
  }

  private void doHandshakeAndStartMatch(List<DecoratedEvent<ViewEvent, GameView>> receivedEventsFromView) throws InterruptedException {
    serverGameView.registerObserver(receivedEventsFromView::add);
    serverGameView.getAvailablePlayers().remove(PlayerColor.GREEN);
    serverGameView.getAvailablePlayers().remove(PlayerColor.CYAN);
    serverGameView.getConnectedPlayers().put("aaa", PlayerColor.GREEN);
    serverGameView.getConnectedPlayers().put("bbb", PlayerColor.CYAN);

    Thread.sleep(500);

    ArgumentCaptor<List<PlayerColor>> availablePlayers = ArgumentCaptor.forClass(List.class);
    verify(mockedClientView, times(1)).showChooseMyPlayer(availablePlayers.capture());

    Thread.sleep(500);

    mockedClientView.sendChosenMyPlayer(PlayerColor.YELLOW);

    Thread.sleep(500);

    assertThat(serverGameView.getConnectedPlayers()).containsValue(PlayerColor.YELLOW);
    assertThat(serverGameView.getAvailablePlayers()).doesNotContain(PlayerColor.YELLOW);

    assertThat(receivedEventsFromView)
      .hasOnlyOneElementSatisfying(d -> assertThat(d.getInnerEvent()).isInstanceOf(NewTurnEvent.class));
  }

}
