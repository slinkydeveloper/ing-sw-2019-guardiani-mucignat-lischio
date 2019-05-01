package com.adrenalinici.adrenaline.network.server;

import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.network.inbox.ChosenMyPlayerColorMessage;
import com.adrenalinici.adrenaline.network.outbox.ChooseMyPlayerMessage;
import com.adrenalinici.adrenaline.util.DecoratedEvent;
import com.adrenalinici.adrenaline.view.event.NewTurnEvent;
import com.adrenalinici.adrenaline.view.event.ViewEvent;
import org.junit.Test;

import java.io.IOException;
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

  //TODO test disconnection while match started

}
