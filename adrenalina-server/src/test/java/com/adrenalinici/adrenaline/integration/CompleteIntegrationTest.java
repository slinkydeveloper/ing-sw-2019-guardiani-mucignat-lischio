package com.adrenalinici.adrenaline.integration;

import com.adrenalinici.adrenaline.cli.BaseCliGameView;
import com.adrenalinici.adrenaline.cli.CliGameViewProxy;
import com.adrenalinici.adrenaline.client.socket.SocketClientNetworkAdapter;
import com.adrenalinici.adrenaline.common.model.*;
import com.adrenalinici.adrenaline.common.view.ActionChosenEvent;
import com.adrenalinici.adrenaline.common.view.NewTurnEvent;
import com.adrenalinici.adrenaline.common.view.PowerUpCardChosenEvent;
import com.adrenalinici.adrenaline.server.GameBootstrapper;
import com.adrenalinici.adrenaline.server.controller.GameController;
import com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.server.network.RemoteView;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;

import static com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes.CHOOSE_ACTION;
import static com.adrenalinici.adrenaline.server.controller.nodes.ControllerNodes.START_TURN;
import static org.assertj.core.api.Assertions.assertThat;

public class CompleteIntegrationTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Spy
  BaseCliGameView player1View;
  @Spy
  BaseCliGameView player2View;
  @Spy
  BaseCliGameView player3View;

  @Test
  public void oneTurnForEachPlayer() throws IOException, InterruptedException {
    GameBootstrapper bootstrapper = new GameBootstrapper(3000, 3001, 3000);

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

  private void startPlayer(BaseCliGameView mockedClientView) throws InterruptedException {
    CliGameViewProxy proxy = new CliGameViewProxy(mockedClientView);
    Thread clientNetworkAdapter = new Thread(new SocketClientNetworkAdapter(proxy, "localhost", 3001));
    clientNetworkAdapter.start();
    Thread.sleep(100);
  }

  private void respawn(GameController controller, BaseCliGameView mockedClientView) {
    PowerUpCard card = controller.getGameModel().getPlayerDashboard(mockedClientView.getMyPlayer()).getPowerUpCards().get(0);
    mockedClientView.sendViewEvent(new PowerUpCardChosenEvent(mockedClientView.getMyPlayer(), card));
  }

  @Test
  public void firstTurnAndTurnTimeout() throws IOException, InterruptedException {
    GameBootstrapper bootstrapper = new GameBootstrapper(3000, 3001, 1);

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

    Thread.sleep(100);

    assertThat(controller.getFlowContext().actualPhase()).isEqualTo(CHOOSE_ACTION.name());
    player1View.sendViewEvent(new ActionChosenEvent(Action.MOVE_MOVE_MOVE));

    Thread.sleep(1000);

    assertThat(controller.getFlowContext().actualPhase()).isEqualTo(START_TURN.name());
    assertThat(controller.getFlowContext().getTurnOfPlayer()).isEqualTo(PlayerColor.YELLOW);

    bootstrapper.stop();

  }

}
