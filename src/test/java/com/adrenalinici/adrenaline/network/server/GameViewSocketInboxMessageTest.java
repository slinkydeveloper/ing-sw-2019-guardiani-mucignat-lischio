package com.adrenalinici.adrenaline.network.server;

import com.adrenalinici.adrenaline.model.common.Action;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.util.DecoratedEvent;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.ActionChosenEvent;
import com.adrenalinici.adrenaline.view.event.NewTurnEvent;
import com.adrenalinici.adrenaline.view.event.StartMatchEvent;
import com.adrenalinici.adrenaline.view.event.ViewEvent;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class GameViewSocketInboxMessageTest extends BaseGameViewSocketIntegrationTest {

  @Test
  public void handshakeAndStartMatch() throws InterruptedException {
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

    Thread.sleep(100);

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

    Thread.sleep(100);

    ArgumentCaptor<List<PlayerColor>> availablePlayers = ArgumentCaptor.forClass(List.class);
    verify(mockedClientView, times(1)).showChooseMyPlayer(availablePlayers.capture());

    Thread.sleep(100);

    mockedClientView.sendChosenMyPlayer(PlayerColor.YELLOW);

    Thread.sleep(100);

    assertThat(serverGameView.getConnectedPlayers()).containsValue(PlayerColor.YELLOW);
    assertThat(serverGameView.getAvailablePlayers()).doesNotContain(PlayerColor.YELLOW);

    assertThat(receivedEventsFromView)
      .hasOnlyOneElementSatisfying(d -> assertThat(d.getInnerEvent()).isInstanceOf(StartMatchEvent.class));
  }

//TODO this test was moved from old GameViewServerSocketTest, removed for creating more damages than coverage
//   It should be adapted at some point (still don't know how)
//
//  @Test
//  public void disconnection() throws IOException, InterruptedException {
//    // Start a match like test before
//
//    List<DecoratedEvent<ViewEvent, GameView>> receivedEventsFromView = new ArrayList<>();
//    gameViewServer.registerObserver(receivedEventsFromView::add);
//    gameViewServer.getAvailablePlayers().remove(PlayerColor.GREEN);
//    gameViewServer.getAvailablePlayers().remove(PlayerColor.CYAN);
//    gameViewServer.getConnectedPlayers().put("aaa", PlayerColor.GREEN);
//    gameViewServer.getConnectedPlayers().put("bbb", PlayerColor.CYAN);
//
//    readMessage();
//
//    sendMessage(new ChosenMyPlayerColorMessage(PlayerColor.YELLOW));
//
//    assertThat(gameViewServer.getConnectedPlayers()).containsValue(PlayerColor.YELLOW);
//    assertThat(gameViewServer.getAvailablePlayers()).doesNotContain(PlayerColor.YELLOW);
//
//    assertThat(receivedEventsFromView)
//      .hasOnlyOneElementSatisfying(d -> assertThat(d.getInnerEvent()).isInstanceOf(NewTurnEvent.class));
//
//    receivedEventsFromView.clear();
//
//    // Now we need to notify some event to check that after reconnection it's sended again
//
//    ViewEvent lastSentEvent = new ActionChosenEvent(Action.MOVE_MOVE_MOVE);
//    gameViewServer.onEvent(new GameModelUpdatedEvent(TestUtils.generateModel()));
//
//    Thread.sleep(100);
//
//    receivedEventsFromView.clear();
//
//    // Now let's disconnect
//
//    clientSocket.close();
//
//    Thread.sleep(100);
//
//    assertThat(clientSocket.isOpen()).isFalse();
//    assertThat(gameViewServer.getConnectedPlayers()).doesNotContainValue(PlayerColor.YELLOW);
//    assertThat(gameViewServer.getAvailablePlayers()).contains(PlayerColor.YELLOW);
//
//    // Reconnect and re-log again
//
//    clientSocket = SocketChannel.open(new InetSocketAddress("localhost", 9000));
//
//    assertThat(readMessage())
//      .isInstanceOf(ChooseMyPlayerMessage.class);
//
//    sendMessage(new ChosenMyPlayerColorMessage(PlayerColor.YELLOW));
//
//    assertThat(gameViewServer.getConnectedPlayers()).containsValue(PlayerColor.YELLOW);
//    assertThat(gameViewServer.getAvailablePlayers()).isEmpty();
//
//    assertThat(readMessage())
//      .isInstanceOfSatisfying(ModelEventMessage.class,
//        me -> assertThat(me.getModelEvent()).isInstanceOf(GameModelUpdatedEvent.class)
//      );
//  }

}
