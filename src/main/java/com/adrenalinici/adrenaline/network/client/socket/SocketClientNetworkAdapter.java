package com.adrenalinici.adrenaline.network.client.socket;

import com.adrenalinici.adrenaline.network.client.ClientNetworkAdapter;
import com.adrenalinici.adrenaline.network.client.ClientViewProxy;
import com.adrenalinici.adrenaline.network.inbox.InboxMessage;
import com.adrenalinici.adrenaline.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.util.LogUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class SocketClientNetworkAdapter extends ClientNetworkAdapter {

  private static final String ADDRESS = System.getenv().getOrDefault("ADDRESS", "localhost");
  private static final int PORT = Integer.parseInt(System.getenv().getOrDefault("SOCKET_PORT", "9000"));
  private static final Logger LOG = LogUtils.getLogger(SocketClientNetworkAdapter.class);

  private Thread receiverThread;
  private Thread senderThread;
  private SocketChannel channel;

  public SocketClientNetworkAdapter(ClientViewProxy proxy) {
    super(proxy);
  }

  @Override
  public void initialize() throws IOException {
    Selector readSelector = Selector.open();

    this.channel = SocketChannel.open(new InetSocketAddress(ADDRESS, PORT));
    this.channel.configureBlocking(false);
    this.channel.register(readSelector, SelectionKey.OP_READ);

    LOG.info(String.format("Connected to %s:%d", ADDRESS, PORT));

    this.receiverThread = new Thread(
      new ReceiverRunnable(readSelector, clientViewInbox),
      "client-" + this.channel.socket().getLocalPort() + "-socket-receiver"
    );
    this.receiverThread.start();
    this.senderThread = new Thread(
      new SenderRunnable(clientViewOutbox, channel),
      "client-" + this.channel.socket().getLocalPort() + "-socket-sender"
    );
    this.senderThread.start();
  }

  @Override
  public void stop() throws IOException {
    if (receiverThread != null)
      receiverThread.interrupt();
    if (senderThread != null)
      senderThread.interrupt();
  }
}
