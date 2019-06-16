package com.adrenalinici.adrenaline.integration.network.server;

import com.adrenalinici.adrenaline.common.model.*;
import com.adrenalinici.adrenaline.common.util.DecoratedEvent;
import com.adrenalinici.adrenaline.common.view.ActionChosenEvent;
import com.adrenalinici.adrenaline.common.view.GameView;
import com.adrenalinici.adrenaline.common.view.StartMatchEvent;
import com.adrenalinici.adrenaline.common.view.ViewEvent;
import com.adrenalinici.adrenaline.server.network.RemoteView;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class GameViewSocketInboxMessageTest extends BaseGameViewSocketIntegrationTest {

  @Test
  public void handshakeAndStartMatch() throws InterruptedException {
    List<DecoratedEvent<ViewEvent, GameView>> receivedEventsFromView = new ArrayList<>();
    createAndConnectAndStartMatch(receivedEventsFromView);
  }

  @Test
  public void testSendViewEvent() throws InterruptedException {
    List<DecoratedEvent<ViewEvent, GameView>> receivedEventsFromView = new ArrayList<>();
    createAndConnectAndStartMatch(receivedEventsFromView);
    receivedEventsFromView.clear();

    // Now the match is started, I can send a ViewEvent
    mockedClientView.sendViewEvent(new ActionChosenEvent(Action.MOVE_MOVE_MOVE));

    sleep();

    assertThat(receivedEventsFromView)
      .hasOnlyOneElementSatisfying(d -> assertThat(d.getInnerEvent())
        .isInstanceOfSatisfying(ActionChosenEvent.class, ace -> assertThat(ace.getAction()).isEqualTo(Action.MOVE_MOVE_MOVE)));
  }

  private void createAndConnectAndStartMatch(List<DecoratedEvent<ViewEvent, GameView>> receivedEventsFromView) throws InterruptedException {
    reset(mockedClientView);

    mockedClientView.sendStartNewMatch("test-match-2", DashboardChoice.SMALL, PlayersChoice.THREE, RulesChoice.SIMPLE);

    sleep(5);

    ArgumentCaptor<Map<String, Set<PlayerColor>>> availableMatches = ArgumentCaptor.forClass(Map.class);
    verify(mockedClientView).showAvailableMatchesAndPlayers(availableMatches.capture());

    assertThat(serverMessageRouter.getContext().getMatches().get("test-match-2"))
      .isNotNull();

    RemoteView remoteView = serverMessageRouter.getContext().getMatches().get("test-match-2");

    remoteView.registerObserver(receivedEventsFromView::add);
    remoteView.getAvailablePlayers().remove(PlayerColor.GREEN);
    remoteView.getAvailablePlayers().remove(PlayerColor.PURPLE);
    remoteView.getConnectedPlayers().put("aaa", PlayerColor.GREEN);
    remoteView.getConnectedPlayers().put("bbb", PlayerColor.PURPLE);

    mockedClientView.sendChosenMatch("test-match-2", PlayerColor.YELLOW);

    sleep(2);

    assertThat(remoteView.getConnectedPlayers()).containsValue(PlayerColor.YELLOW);
    assertThat(remoteView.getAvailablePlayers()).doesNotContain(PlayerColor.YELLOW);

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
//    sleep();
//
//    receivedEventsFromView.clear();
//
//    // Now let's disconnect
//
//    clientSocket.close();
//
//    sleep();
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
