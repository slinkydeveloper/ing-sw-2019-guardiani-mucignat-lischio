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
  private Thread receiverThread;
  private Thread senderThread;
  private SocketChannel channel;
  private Selector readSelector;

  public SocketClientNetworkAdapter(ClientViewProxy proxy, String host, int port) {
    super(proxy);
    this.host = host;
    this.port = port;
  }

  @Override
  public void initialize() throws IOException {
    if (readSelector == null) { // Avoid double initialization
      readSelector = Selector.open();

      this.channel = SocketChannel.open(new InetSocketAddress(host, port));
      this.channel.configureBlocking(false);
      this.channel.register(readSelector, SelectionKey.OP_READ);

      LOG.info(String.format("Connected to %s:%d", host, port));

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
  }

  @Override
  public void stop() throws IOException {
    if (receiverThread != null)
      receiverThread.interrupt();
    if (senderThread != null)
      senderThread.interrupt();
    if (readSelector != null && readSelector.isOpen())
      readSelector.close();
    if (channel != null && channel.isOpen())
      channel.close();
  }
}
