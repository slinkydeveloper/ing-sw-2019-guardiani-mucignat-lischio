package com.adrenalinici.adrenaline.network.server.socket;

import com.adrenalinici.adrenaline.network.inbox.InboxEntry;
import com.adrenalinici.adrenaline.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.network.server.BaseGameViewServer;
import com.adrenalinici.adrenaline.network.server.ServerNetworkAdapter;
import com.adrenalinici.adrenaline.util.LogUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class SocketServerNetworkAdapter extends ServerNetworkAdapter {

  private static final Logger LOG = LogUtils.getLogger(SocketServerNetworkAdapter.class);

  private int port;
  private Thread receiverThread;
  private Thread broadcasterThread;
  private ServerSocketChannel serverChannel;
  private Map<Socket, String> connectedClients;
  private Selector readSelector;

  public SocketServerNetworkAdapter(BlockingQueue<InboxEntry> viewInbox, BlockingQueue<OutboxMessage> viewOutbox, int port, String gameId) {
    super(viewInbox, viewOutbox, gameId);
    this.port = port;
  }

  @Override
  public void start() throws IOException {
    readSelector = Selector.open();
    this.connectedClients = new ConcurrentHashMap<>();

    // Configure socket server and register channel to selector
    this.serverChannel = ServerSocketChannel.open();
    this.serverChannel.configureBlocking(false);
    this.serverChannel.socket().bind(new InetSocketAddress(port));
    this.serverChannel.register(readSelector, SelectionKey.OP_ACCEPT);

    LOG.info(String.format("Started socket server for game %s on port %d", gameId, port));

    this.receiverThread = new Thread(
      new ReceiverRunnable(connectedClients, readSelector, viewInbox),
      "socket-receiver-" + gameId
    );
    this.receiverThread.start();
    this.broadcasterThread = new Thread(
      new BroadcasterRunnable(connectedClients, viewOutbox),
      "socket-broadcaster-" + gameId
    );
    this.broadcasterThread.start();
  }

  @Override
  public void stop() throws IOException {
    if (receiverThread != null)
      receiverThread.interrupt();
    if (broadcasterThread != null)
      broadcasterThread.interrupt();
    if (readSelector != null && readSelector.isOpen())
      readSelector.close();
    if (serverChannel != null && serverChannel.isOpen())
      serverChannel.close();
    LOG.info(String.format("Stopped socket server for game %s on port %d", gameId, port));
  }
}
