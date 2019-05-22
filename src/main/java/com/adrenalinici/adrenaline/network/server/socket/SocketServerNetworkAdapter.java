package com.adrenalinici.adrenaline.network.server.socket;

import com.adrenalinici.adrenaline.network.inbox.InboxEntry;
import com.adrenalinici.adrenaline.network.outbox.OutboxEntry;
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
  private Thread senderThread;
  private ServerSocketChannel serverChannel;
  private Selector readSelector;

  public SocketServerNetworkAdapter(BlockingQueue<InboxEntry> viewInbox, BlockingQueue<OutboxEntry> viewOutbox, int port) {
    super(viewInbox, viewOutbox);
    this.port = port;
  }

  @Override
  public void start() throws IOException {
    readSelector = Selector.open();
    Map<Socket, String> connectedClients = new ConcurrentHashMap<>();

    // Configure socket server and register channel to selector
    this.serverChannel = ServerSocketChannel.open();
    this.serverChannel.configureBlocking(false);
    this.serverChannel.socket().bind(new InetSocketAddress(port));
    this.serverChannel.register(readSelector, SelectionKey.OP_ACCEPT);

    LOG.info(String.format("Started socket server on port %d", port));

    this.receiverThread = new Thread(
      new ReceiverRunnable(connectedClients, readSelector, viewInbox),
      "socket-receiver"
    );
    this.receiverThread.start();
    this.senderThread = new Thread(
      new SenderRunnable(connectedClients, viewOutbox),
      "socket-sender"
    );
    this.senderThread.start();
  }

  @Override
  public void stop() throws IOException {
    if (receiverThread != null)
      receiverThread.interrupt();
    if (senderThread != null)
      senderThread.interrupt();
    if (readSelector != null && readSelector.isOpen())
      readSelector.close();
    if (serverChannel != null && serverChannel.isOpen())
      serverChannel.close();
    LOG.info(String.format("Stopped socket server on port %d", port));
  }
}
