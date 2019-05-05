package com.adrenalinici.adrenaline.network.client;

import com.adrenalinici.adrenaline.network.inbox.InboxMessage;
import com.adrenalinici.adrenaline.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.util.LogUtils;
import com.adrenalinici.adrenaline.util.Observer;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public abstract class ClientNetworkAdapter implements Observer<InboxMessage>, Runnable {

  private static final Logger LOG = LogUtils.getLogger(ClientNetworkAdapter.class);

  protected BlockingQueue<InboxMessage> clientViewOutbox;
  protected BlockingQueue<OutboxMessage> clientViewInbox;
  private ClientViewProxy proxy;

  public ClientNetworkAdapter(ClientViewProxy proxy) {
    this.clientViewOutbox = new LinkedBlockingQueue<>();
    this.clientViewInbox = new LinkedBlockingQueue<>();
    this.proxy = proxy;
    proxy.registerObserver(this);
  }

  public abstract void initialize() throws IOException;

  @Override
  public void run() {
    try {
      initialize();
    } catch (IOException e) {
      LOG.severe("Error while initializing " + this.getClass().getName());
      e.printStackTrace();
      return;
    }
    while (!Thread.currentThread().isInterrupted()) {
      try {
        OutboxMessage e = clientViewInbox.take();
        LOG.fine(String.format("Received new message from server: %s", e.getClass().getName()));
        proxy.handleNewServerMessage(e);
      } catch (InterruptedException ex) { }
    }
    try {
      this.stop();
    } catch (IOException e) {
      LOG.severe("Error while stopping " + this.getClass().getName());
      e.printStackTrace();
    }
  }

  public abstract void stop() throws IOException;

  @Override
  public void onEvent(InboxMessage newValue) {
    this.clientViewOutbox.offer(newValue);
  }
}
