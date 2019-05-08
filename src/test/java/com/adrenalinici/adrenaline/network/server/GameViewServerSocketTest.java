package com.adrenalinici.adrenaline.network.server;

import com.adrenalinici.adrenaline.model.Action;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.model.event.GameModelUpdatedEvent;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.network.inbox.ChosenMyPlayerColorMessage;
import com.adrenalinici.adrenaline.network.outbox.ChooseMyPlayerMessage;
import com.adrenalinici.adrenaline.network.outbox.ModelEventMessage;
import com.adrenalinici.adrenaline.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.util.DecoratedEvent;
import com.adrenalinici.adrenaline.view.event.ActionChosenEvent;
import com.adrenalinici.adrenaline.view.event.NewTurnEvent;
import com.adrenalinici.adrenaline.view.event.ViewEvent;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GameViewServerSocketTest extends BaseGameViewServerSocketTest {

  @Test
  public void startConnectionAndChooseColorTest() throws IOException, InterruptedException {
    assertThat(readMessage())
      .isInstanceOfSatisfying(ChooseMyPlayerMessage.class, m ->
        assertThat(m.getPlayerColors()).containsExactlyInAnyOrder(playerColorList)
      );

    sendMessage(new ChosenMyPlayerColorMessage(PlayerColor.YELLOW));

    assertThat(gameViewServer.getConnectedPlayers()).containsValue(PlayerColor.YELLOW);
    assertThat(gameViewServer.getAvailablePlayers()).doesNotContain(PlayerColor.YELLOW);
  }

  @Test
  public void startMatch() throws IOException, InterruptedException {
    List<DecoratedEvent<ViewEvent, BaseGameViewServer>> receivedEventsFromView = new ArrayList<>();
    gameViewServer.registerObserver(receivedEventsFromView::add);
    gameViewServer.getAvailablePlayers().remove(PlayerColor.GREEN);
    gameViewServer.getAvailablePlayers().remove(PlayerColor.CYAN);
    gameViewServer.getConnectedPlayers().put("aaa", PlayerColor.GREEN);
    gameViewServer.getConnectedPlayers().put("bbb", PlayerColor.CYAN);

    readMessage();

    sendMessage(new ChosenMyPlayerColorMessage(PlayerColor.YELLOW));

    assertThat(gameViewServer.getConnectedPlayers()).containsValue(PlayerColor.YELLOW);
    assertThat(gameViewServer.getAvailablePlayers()).doesNotContain(PlayerColor.YELLOW);

    assertThat(receivedEventsFromView)
      .hasOnlyOneElementSatisfying(d -> assertThat(d.getInnerEvent()).isInstanceOf(NewTurnEvent.class));
  }

  @Test
  public void disconnection() throws IOException, InterruptedException {
    // Start a match like test before

    List<DecoratedEvent<ViewEvent, BaseGameViewServer>> receivedEventsFromView = new ArrayList<>();
    gameViewServer.registerObserver(receivedEventsFromView::add);
    gameViewServer.getAvailablePlayers().remove(PlayerColor.GREEN);
    gameViewServer.getAvailablePlayers().remove(PlayerColor.CYAN);
    gameViewServer.getConnectedPlayers().put("aaa", PlayerColor.GREEN);
    gameViewServer.getConnectedPlayers().put("bbb", PlayerColor.CYAN);

    readMessage();

    sendMessage(new ChosenMyPlayerColorMessage(PlayerColor.YELLOW));

    assertThat(gameViewServer.getConnectedPlayers()).containsValue(PlayerColor.YELLOW);
    assertThat(gameViewServer.getAvailablePlayers()).doesNotContain(PlayerColor.YELLOW);

    assertThat(receivedEventsFromView)
      .hasOnlyOneElementSatisfying(d -> assertThat(d.getInnerEvent()).isInstanceOf(NewTurnEvent.class));

    receivedEventsFromView.clear();

    // Now we need to notify some event to check that after reconnection it's sended again

    ViewEvent lastSentEvent = new ActionChosenEvent(Action.MOVE_MOVE_MOVE);
    gameViewServer.onEvent(new GameModelUpdatedEvent(null));

    Thread.sleep(100);

    receivedEventsFromView.clear();

    // Now let's disconnect

    clientSocket.close();

    Thread.sleep(100);

    assertThat(clientSocket.isOpen()).isFalse();
    assertThat(gameViewServer.getConnectedPlayers()).doesNotContainValue(PlayerColor.YELLOW);
    assertThat(gameViewServer.getAvailablePlayers()).contains(PlayerColor.YELLOW);

    // Reconnect and re-log again

    clientSocket = SocketChannel.open(new InetSocketAddress("localhost", 9000));

    assertThat(readMessage())
      .isInstanceOf(ChooseMyPlayerMessage.class);

    sendMessage(new ChosenMyPlayerColorMessage(PlayerColor.YELLOW));

    assertThat(gameViewServer.getConnectedPlayers()).containsValue(PlayerColor.YELLOW);
    assertThat(gameViewServer.getAvailablePlayers()).isEmpty();

    assertThat(readMessage())
      .isInstanceOfSatisfying(ModelEventMessage.class,
        me -> assertThat(me.getModelEvent()).isInstanceOf(GameModelUpdatedEvent.class)
      );
  }

}
