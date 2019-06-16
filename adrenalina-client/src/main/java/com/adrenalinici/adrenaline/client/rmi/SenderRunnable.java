package com.adrenalinici.adrenaline.client.rmi;

import com.adrenalinici.adrenaline.common.network.inbox.InboxMessage;
import com.adrenalinici.adrenaline.common.network.rmi.GameRmiClient;
import com.adrenalinici.adrenaline.common.network.rmi.GameRmiServer;
import com.adrenalinici.adrenaline.common.util.LogUtils;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SenderRunnable implements Runnable {

  private static final Logger LOG = LogUtils.getLogger(SenderRunnable.class);

  private BlockingQueue<InboxMessage> clientViewOutbox;
  private GameRmiServer rmiServer;
  private GameRmiClient rmiClient;

  public SenderRunnable(BlockingQueue<InboxMessage> clientViewOutbox, GameRmiServer rmiServer, GameRmiClient rmiClient) {
    this.clientViewOutbox = clientViewOutbox;
    this.rmiServer = rmiServer;
    this.rmiClient = rmiClient;
  }

  @Override
  public void run() {
    while(!Thread.currentThread().isInterrupted()) {
      try {
        InboxMessage message = clientViewOutbox.take();
        LOG.fine(String.format("Going to send message %s", message.getClass()));
        rmiServer.acceptMessage(message, rmiClient);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      } catch (IOException e) {
        LOG.log(Level.SEVERE, "IOException while sending a message", e);
      }
    }
  }
}
