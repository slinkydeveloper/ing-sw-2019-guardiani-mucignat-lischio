package com.adrenalinici.adrenaline;

import com.adrenalinici.adrenaline.controller.GameController;
import com.adrenalinici.adrenaline.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.model.common.*;
import com.adrenalinici.adrenaline.network.client.ClientViewProxy;
import com.adrenalinici.adrenaline.network.client.socket.SocketClientNetworkAdapter;
import com.adrenalinici.adrenaline.network.server.RemoteView;
import com.adrenalinici.adrenaline.server.GameBootstrapper;
import com.adrenalinici.adrenaline.view.BaseClientGameView;
import com.adrenalinici.adrenaline.view.event.ActionChosenEvent;
import com.adrenalinici.adrenaline.view.event.NewTurnEvent;
import com.adrenalinici.adrenaline.view.event.PowerUpCardChosenEvent;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;

import static com.adrenalinici.adrenaline.controller.nodes.ControllerNodes.CHOOSE_ACTION;
import static com.adrenalinici.adrenaline.controller.nodes.ControllerNodes.START_TURN;
import static org.assertj.core.api.Assertions.assertThat;

public class CompleteIntegrationTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Spy
  BaseClientGameView player1View;
  @Spy
  BaseClientGameView player2View;
  @Spy
  BaseClientGameView player3View;

  GameBootstrapper bootstrapper = new GameBootstrapper(3000, 3001);

  @Test
  public void oneTurnForEachPlayer() throws IOException, InterruptedException {
    bootstrapper.start(); // Ready to rock

    Thread.sleep(100);

    startPlayer(player1View);
    startPlayer(player2View);
    startPlayer(player3View);

    player1View.sendStartNewMatch("test-match", DashboardChoice.SMALL, PlayersChoice.THREE, RulesChoice.SIMPLE);

    Thread.sleep(1500);

    RemoteView remoteView = bootstrapper.getServerMessageRouter().getContext().getMatches().get("test-match");
    GameController controller = bootstrapper.getServerMessageRouter().getContext().getMatchesControllersMap().get("test-match");
    assertThat(remoteView).isNotNull();
    assertThat(controller).isNotNull();

    player1View.sendChosenMatch("test-match", PlayerColor.GREEN);
    player2View.sendChosenMatch("test-match", PlayerColor.YELLOW);
    player3View.sendChosenMatch("test-match", PlayerColor.PURPLE);

    Thread.sleep(500);

    respawn(controller, player1View);
    respawn(controller, player2View);
    respawn(controller, player3View);

    Thread.sleep(100);

    assertThat(controller.getFlowContext().actualPhase()).isEqualTo(START_TURN.name());

    player1View.sendViewEvent(new NewTurnEvent()); // One should be enough, the second one should be discarded
    player2View.sendViewEvent(new NewTurnEvent());

    Thread.sleep(100);

    assertThat(controller.getFlowContext().actualPhase()).isEqualTo(CHOOSE_ACTION.name());
    player1View.sendViewEvent(new ActionChosenEvent(Action.MOVE_MOVE_MOVE));

    Thread.sleep(100);

    assertThat(controller.getFlowContext().actualPhase()).isEqualTo(ControllerNodes.movement(3));

    bootstrapper.stop();

  }

  private void startPlayer(BaseClientGameView mockedClientView) throws InterruptedException {
    ClientViewProxy proxy = new ClientViewProxy(mockedClientView);
    Thread clientNetworkAdapter = new Thread(new SocketClientNetworkAdapter(proxy, "localhost", 3001));
    clientNetworkAdapter.start();
    Thread.sleep(100);
  }

  private void respawn(GameController controller, BaseClientGameView mockedClientView) {
    PowerUpCard card = controller.getGameModel().getPlayerDashboard(mockedClientView.getMyPlayer()).getPowerUpCards().get(0);
    mockedClientView.sendViewEvent(new PowerUpCardChosenEvent(mockedClientView.getMyPlayer(), card));
  }

}
