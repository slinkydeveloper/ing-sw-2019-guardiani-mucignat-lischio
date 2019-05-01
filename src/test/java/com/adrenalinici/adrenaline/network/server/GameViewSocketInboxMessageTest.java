package com.adrenalinici.adrenaline.network.server;

import com.adrenalinici.adrenaline.model.*;
import com.adrenalinici.adrenaline.util.DecoratedEvent;
import com.adrenalinici.adrenaline.view.event.NewTurnEvent;
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
  public void handshakeAndStartMatch() throws IOException, InterruptedException {
    List<DecoratedEvent<ViewEvent, BaseGameViewServer>> receivedEventsFromView = new ArrayList<>();
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
      .hasOnlyOneElementSatisfying(d -> assertThat(d.getInnerEvent()).isInstanceOf(NewTurnEvent.class));
  }

  //TODO test event message

}
