package com.adrenalinici.adrenaline;

import com.adrenalinici.adrenaline.controller.nodes.ControllerNodes;
import com.adrenalinici.adrenaline.model.common.Action;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.Position;
import com.adrenalinici.adrenaline.model.common.PowerUpCard;
import com.adrenalinici.adrenaline.network.client.ClientViewProxy;
import com.adrenalinici.adrenaline.network.client.socket.SocketClientNetworkAdapter;
import com.adrenalinici.adrenaline.network.inbox.ChosenMyPlayerColorMessage;
import com.adrenalinici.adrenaline.server.DashboardChoice;
import com.adrenalinici.adrenaline.server.GameBootstrapper;
import com.adrenalinici.adrenaline.server.PlayersChoice;
import com.adrenalinici.adrenaline.server.RulesChoice;
import com.adrenalinici.adrenaline.util.DecoratedEvent;
import com.adrenalinici.adrenaline.view.BaseClientGameView;
import com.adrenalinici.adrenaline.view.GameView;
import com.adrenalinici.adrenaline.view.event.*;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.adrenalinici.adrenaline.controller.nodes.ControllerNodes.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CompleteIntegrationTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Spy
  BaseClientGameView player1View;
  @Spy
  BaseClientGameView player2View;
  @Spy
  BaseClientGameView player3View;

  GameBootstrapper bootstrapper = new GameBootstrapper(DashboardChoice.SMALL, PlayersChoice.THREE, RulesChoice.COMPLETE, 9001, 9000);

  @Test
  public void oneTurnForEachPlayer() throws IOException, InterruptedException {
    bootstrapper.start(); // Ready to rock

    Thread.sleep(100);

    startPlayer(player1View);
    startPlayer(player2View);
    startPlayer(player3View);

    player1View.sendChosenMyPlayer(PlayerColor.GREEN);
    player2View.sendChosenMyPlayer(PlayerColor.YELLOW);
    player3View.sendChosenMyPlayer(PlayerColor.PURPLE);

    Thread.sleep(500);

    respawn(player1View);
    respawn(player2View);
    respawn(player3View);

    Thread.sleep(100);

    assertThat(bootstrapper.getController().getFlowContext().actualPhase()).isEqualTo(START_TURN.name());

    player1View.sendViewEvent(new NewTurnEvent()); // One should be enough, the second one should be discarded
    player2View.sendViewEvent(new NewTurnEvent());

    Thread.sleep(100);

    assertThat(bootstrapper.getController().getFlowContext().actualPhase()).isEqualTo(CHOOSE_ACTION.name());
    player1View.sendViewEvent(new ActionChosenEvent(Action.MOVE_MOVE_MOVE));

    Thread.sleep(100);

    assertThat(bootstrapper.getController().getFlowContext().actualPhase()).isEqualTo(ControllerNodes.movement(3));

    bootstrapper.stop();

  }

  private void startPlayer(BaseClientGameView mockedClientView) throws InterruptedException {
    ClientViewProxy proxy = new ClientViewProxy(mockedClientView);
    Thread clientNetworkAdapter = new Thread(new SocketClientNetworkAdapter(proxy, "localhost", 9000));
    clientNetworkAdapter.start();
    Thread.sleep(100);
  }

  private void respawn(BaseClientGameView mockedClientView) {
    PowerUpCard card = bootstrapper.getModel().getPlayerDashboard(mockedClientView.getMyPlayer()).getPowerUpCards().get(0);
    mockedClientView.sendViewEvent(new PowerUpCardChosenEvent(mockedClientView.getMyPlayer(), card));
  }

}
