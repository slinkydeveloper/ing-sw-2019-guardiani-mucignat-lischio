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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
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

    sleep();

    assertThat(receivedEventsFromView)
      .hasOnlyOneElementSatisfying(d -> assertThat(d.getInnerEvent())
        .isInstanceOfSatisfying(ActionChosenEvent.class, ace -> assertThat(ace.getAction()).isEqualTo(Action.MOVE_MOVE_MOVE)));
  }

  private void doHandshakeAndStartMatch(List<DecoratedEvent<ViewEvent, GameView>> receivedEventsFromView) throws InterruptedException {
    reset(mockedClientView);

    mockedClientView.sendStartNewMatch("test-match-2", DashboardChoice.SMALL, PlayersChoice.THREE, RulesChoice.SIMPLE);

    sleep();

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

    sleep();

    assertThat(remoteView.getConnectedPlayers()).containsValue(PlayerColor.YELLOW);
    assertThat(remoteView.getAvailablePlayers()).doesNotContain(PlayerColor.YELLOW);

    assertThat(receivedEventsFromView)
      .hasOnlyOneElementSatisfying(d -> assertThat(d.getInnerEvent()).isInstanceOf(StartMatchEvent.class));
  }

}
