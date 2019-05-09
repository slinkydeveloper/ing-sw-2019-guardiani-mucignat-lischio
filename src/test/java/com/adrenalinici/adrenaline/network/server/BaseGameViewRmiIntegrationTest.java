package com.adrenalinici.adrenaline.network.server;

import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.network.client.ClientViewProxy;
import com.adrenalinici.adrenaline.network.client.rmi.RmiClientNetworkAdapter;
import com.adrenalinici.adrenaline.network.inbox.InboxEntry;
import com.adrenalinici.adrenaline.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.network.server.rmi.RmiServerNetworkAdapter;
import com.adrenalinici.adrenaline.view.BaseClientGameView;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.mockito.ArgumentMatchers.any;

public class BaseGameViewRmiIntegrationTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Spy BaseClientGameView mockedClientView;
  ClientViewProxy proxy;
  Thread clientNetworkAdapter;

  RmiServerNetworkAdapter serverNetworkAdapter;
  GameViewServer serverGameView;
  Thread serverViewThread;
  final PlayerColor[] playerColorList = {
    PlayerColor.GREEN,
    PlayerColor.YELLOW,
    PlayerColor.CYAN
  };

  @Before
  public void setUp() throws IOException, InterruptedException {
    BlockingQueue<InboxEntry> inbox = new LinkedBlockingQueue<>();
    BlockingQueue<OutboxMessage> outbox = new LinkedBlockingQueue<>();

    serverGameView = new GameViewServer(inbox, outbox, new HashSet<>(Arrays.asList(playerColorList)));
    serverViewThread = new Thread(serverGameView, "game-view-server");

    serverNetworkAdapter = new RmiServerNetworkAdapter(inbox, outbox);

    serverViewThread.start();
    serverNetworkAdapter.start();

    Thread.sleep(200);

    proxy = new ClientViewProxy(mockedClientView);
    clientNetworkAdapter = new Thread(new RmiClientNetworkAdapter(proxy), "test-client-network-adapter");
    clientNetworkAdapter.start();

    Thread.sleep(500);
  }

  @After
  public void tearDown() throws IOException {
    serverViewThread.interrupt();
    serverNetworkAdapter.stop();
    clientNetworkAdapter.interrupt();
  }

}
