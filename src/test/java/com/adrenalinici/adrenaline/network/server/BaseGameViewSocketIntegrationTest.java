package com.adrenalinici.adrenaline.network.server;

import com.adrenalinici.adrenaline.model.common.DashboardChoice;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.PlayersChoice;
import com.adrenalinici.adrenaline.model.common.RulesChoice;
import com.adrenalinici.adrenaline.network.client.ClientViewProxy;
import com.adrenalinici.adrenaline.network.client.socket.SocketClientNetworkAdapter;
import com.adrenalinici.adrenaline.network.inbox.InboxEntry;
import com.adrenalinici.adrenaline.network.outbox.OutboxEntry;
import com.adrenalinici.adrenaline.network.server.socket.SocketServerNetworkAdapter;
import com.adrenalinici.adrenaline.view.BaseClientGameView;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;

public class BaseGameViewSocketIntegrationTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Spy
  BaseClientGameView mockedClientView;
  ClientViewProxy proxy;
  Thread clientNetworkAdapter;

  SocketServerNetworkAdapter serverNetworkAdapter;
  ServerMessageRouter serverMessageRouter;
  Thread serverMessageRouterThread;
  final PlayerColor[] playerColorList = {
    PlayerColor.GREEN,
    PlayerColor.YELLOW,
    PlayerColor.CYAN
  };

  RemoteView remoteView;

  @Before
  public void setUp() throws IOException, InterruptedException {
    BlockingQueue<InboxEntry> inbox = new LinkedBlockingQueue<>();
    BlockingQueue<OutboxEntry> outboxRmi = new LinkedBlockingQueue<>();
    BlockingQueue<OutboxEntry> outboxSocket = new LinkedBlockingQueue<>();

    serverMessageRouter = ServerMessageRouter.createWithHandlers(inbox, outboxRmi, outboxSocket);
    serverMessageRouterThread = new Thread(serverMessageRouter, "server-message-router-test");

    serverNetworkAdapter = new SocketServerNetworkAdapter(inbox, outboxSocket, 9000);

    serverMessageRouterThread.start();
    serverNetworkAdapter.start();

    sleep(5);

    proxy = new ClientViewProxy(mockedClientView);
    clientNetworkAdapter = new Thread(new SocketClientNetworkAdapter(proxy, "localhost", 9000), "test-client-network-adapter");
    clientNetworkAdapter.start();

    sleep();

    mockedClientView.sendStartNewMatch("test-match", DashboardChoice.SMALL, PlayersChoice.THREE, RulesChoice.SIMPLE);

    sleep(5);

    remoteView = serverMessageRouter.getContext().getMatches().get("test-match");
    assertThat(remoteView).isNotNull();

    reset(mockedClientView);
  }

  void sleep(int times) throws InterruptedException {
    Thread.sleep(times * 200);
  }

  void sleep() throws InterruptedException {
    sleep(1);
  }

  @After
  public void tearDown() throws IOException {
    serverMessageRouterThread.interrupt();
    serverNetworkAdapter.stop();
    clientNetworkAdapter.interrupt();
  }

}
