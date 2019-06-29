package com.adrenalinici.adrenaline.client.socket;

import com.adrenalinici.adrenaline.client.ClientNetworkAdapter;
import com.adrenalinici.adrenaline.client.ClientViewProxy;
import com.adrenalinici.adrenaline.common.util.LogUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

public class SocketClientNetworkAdapter extends ClientNetworkAdapter {

  private static final Logger LOG = LogUtils.getLogger(SocketClientNetworkAdapter.class);

  private String host;
  private int port;
  private Thread eventLoop;
  private SocketChannel channel;
  private Selector selector;

  public SocketClientNetworkAdapter(ClientViewProxy proxy, String host, int port) {
    super(proxy);
    this.host = host;
    this.port = port;
  }

  @Override
  public void initialize() throws IOException {
    if (selector == null) { // Avoid double initialization
      selector = Selector.open();

      this.channel = SocketChannel.open(new InetSocketAddress(host, port));
      this.channel.configureBlocking(false);
      this.channel.socket().setKeepAlive(true);
      this.channel.socket().setSendBufferSize(128 * 1024 * 1024);
      this.channel.socket().setReceiveBufferSize(128 * 1024 * 1024);

      this.channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

      LOG.info(String.format("Connected to %s:%d", host, port));

      this.eventLoop = new Thread(
        new SocketEventLoopRunnable(selector, clientViewInbox, clientViewOutbox),
        "client-" + this.channel.socket().getLocalPort() + "-event-loop"
      );
      this.eventLoop.start();
    }
  }

  @Override
  public void stop() throws IOException {
    if (eventLoop != null)
      eventLoop.interrupt();
    if (selector != null && selector.isOpen())
      selector.close();
    if (channel != null && channel.isOpen())
      channel.close();
  }
}
