package com.adrenalinici.adrenaline.server.network.socket;

import com.adrenalinici.adrenaline.common.network.inbox.InboxEntry;
import com.adrenalinici.adrenaline.common.network.outbox.OutboxEntry;
import com.adrenalinici.adrenaline.common.util.LogUtils;
import com.adrenalinici.adrenaline.server.network.ServerNetworkAdapter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class SocketServerNetworkAdapter extends ServerNetworkAdapter {

  private static final Logger LOG = LogUtils.getLogger(SocketServerNetworkAdapter.class);

  private int port;
  private Thread eventLoop;
  private ServerSocketChannel serverChannel;
  private Selector selector;

  public SocketServerNetworkAdapter(BlockingQueue<InboxEntry> viewInbox, BlockingQueue<OutboxEntry> viewOutbox, int port) {
    super(viewInbox, viewOutbox);
    this.port = port;
  }

  @Override
  public void start() throws IOException {
    selector = Selector.open();

    // Configure socket server and register channel to selector
    this.serverChannel = ServerSocketChannel.open();
    this.serverChannel.configureBlocking(false);
    this.serverChannel.socket().setReceiveBufferSize(64 * 1024 * 1024);
    this.serverChannel.socket().bind(new InetSocketAddress(port));
    this.serverChannel.register(selector, SelectionKey.OP_ACCEPT);

    LOG.info(String.format("Started socket server on port %d", port));

    this.eventLoop = new Thread(
      new SocketEventLoopRunnable(viewOutbox, viewInbox, selector),
      "socket-event-loop"
    );
    this.eventLoop.start();
  }

  @Override
  public void stop() throws IOException {
    if (eventLoop != null)
      eventLoop.interrupt();
    if (selector != null && selector.isOpen())
      selector.close();
    if (serverChannel != null && serverChannel.isOpen())
      serverChannel.close();
    LOG.info(String.format("Stopped socket server on port %d", port));
  }
}
