package com.adrenalinici.adrenaline.integration.network.server;

import com.adrenalinici.adrenaline.cli.BaseCliGameView;
import com.adrenalinici.adrenaline.cli.CliGameViewProxy;
import com.adrenalinici.adrenaline.client.rmi.RmiClientNetworkAdapter;
import com.adrenalinici.adrenaline.common.model.DashboardChoice;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.PlayersChoice;
import com.adrenalinici.adrenaline.common.model.RulesChoice;
import com.adrenalinici.adrenaline.common.network.inbox.InboxEntry;
import com.adrenalinici.adrenaline.common.network.outbox.OutboxEntry;
import com.adrenalinici.adrenaline.server.network.RemoteView;
import com.adrenalinici.adrenaline.server.network.ServerMessageRouter;
import com.adrenalinici.adrenaline.server.network.rmi.RmiServerNetworkAdapter;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;

public class BaseGameViewRmiIntegrationTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Spy
  BaseCliGameView mockedClientView;
  CliGameViewProxy proxy;
  Thread clientNetworkAdapter;

  RmiServerNetworkAdapter serverNetworkAdapter;
  ServerMessageRouter serverMessageRouter;
  Thread serverMessageRouterThread;
  final PlayerColor[] playerColorList = {
    PlayerColor.GREEN,
    PlayerColor.YELLOW,
    PlayerColor.CYAN
  };

  RemoteView remoteView;
  Registry registry;

  @Before
  public void setUp() throws IOException, InterruptedException {
    BlockingQueue<InboxEntry> inbox = new LinkedBlockingQueue<>();
    BlockingQueue<OutboxEntry> outboxRmi = new LinkedBlockingQueue<>();
    BlockingQueue<OutboxEntry> outboxSocket = new LinkedBlockingQueue<>();

    serverMessageRouter = ServerMessageRouter.createWithHandlers(inbox, outboxRmi, outboxSocket, 3000);
    serverMessageRouterThread = new Thread(serverMessageRouter, "server-message-router-test");

    serverNetworkAdapter = new RmiServerNetworkAdapter(LocateRegistry::createRegistry, inbox, outboxRmi, 3001);

    serverMessageRouterThread.start();
    serverNetworkAdapter.start();

    sleep(4);

    proxy = new CliGameViewProxy(mockedClientView);
    clientNetworkAdapter = new Thread(new RmiClientNetworkAdapter(proxy, "localhost", 3001), "test-client-network-adapter");
    clientNetworkAdapter.start();

    sleep();

    mockedClientView.sendStartNewMatch("test-match", DashboardChoice.SMALL, PlayersChoice.THREE, RulesChoice.SIMPLE);

    sleep(4);

    remoteView = serverMessageRouter.getContext().getMatches().get("test-match");
    assertThat(remoteView).isNotNull();

    reset(mockedClientView);
  }

  void sleep(int times) throws InterruptedException {
    Thread.sleep(times * 500);
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
