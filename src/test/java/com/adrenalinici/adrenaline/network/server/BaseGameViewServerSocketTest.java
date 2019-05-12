package com.adrenalinici.adrenaline.network.server;

import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.network.inbox.InboxEntry;
import com.adrenalinici.adrenaline.network.inbox.InboxMessage;
import com.adrenalinici.adrenaline.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.network.server.socket.SocketServerNetworkAdapter;
import com.adrenalinici.adrenaline.util.SerializationUtils;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseGameViewServerSocketTest {

  SocketServerNetworkAdapter networkAdapter;
  GameViewServer gameViewServer;
  Thread viewThread;
  SocketChannel clientSocket;
  ByteBuffer readBuffer;
  final PlayerColor[] playerColorList = {
    PlayerColor.GREEN,
    PlayerColor.YELLOW,
    PlayerColor.CYAN
  };

  @Before
  public void setUp() throws IOException, InterruptedException {
    BlockingQueue<InboxEntry> inbox = new LinkedBlockingQueue<>();
    BlockingQueue<OutboxMessage> outbox = new LinkedBlockingQueue<>();

    gameViewServer = new GameViewServer(inbox, new LinkedBlockingQueue<>(), outbox, new HashSet<>(Arrays.asList(playerColorList)));
    viewThread = new Thread(gameViewServer, "game-view-server");

    networkAdapter = new SocketServerNetworkAdapter(inbox, outbox, 9000, "test");

    viewThread.start();
    networkAdapter.start();

    readBuffer = ByteBuffer.allocate(20 * 1024);

    clientSocket = SocketChannel.open(new InetSocketAddress("localhost", 9000));

    assertThat(clientSocket.isConnected()).isTrue();

    Thread.sleep(100);
  }

  @After
  public void tearDown() throws IOException {
    viewThread.interrupt();
    networkAdapter.stop();
  }

  OutboxMessage readMessage() throws IOException {
    readBuffer.clear();

    int readBytes = clientSocket.read(readBuffer);
    assertThat(readBytes).isNotEqualTo(-1);

    byte[] serializedMessage = new byte[readBytes];
    System.arraycopy(readBuffer.array(), 0, serializedMessage, 0, readBytes);

    return SerializationUtils.deserialize(serializedMessage);
  }

  void sendMessage(InboxMessage message) throws IOException, InterruptedException {
    byte[] serializedMessage = SerializationUtils.serialize(message);
    clientSocket.write(ByteBuffer.wrap(serializedMessage));

    Thread.sleep(100);
  }

}
