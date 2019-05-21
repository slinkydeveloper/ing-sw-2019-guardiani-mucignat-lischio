package com.adrenalinici.adrenaline.network.server.rmi;

import com.adrenalinici.adrenaline.network.client.rmi.GameRmiClient;
import com.adrenalinici.adrenaline.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.util.LogUtils;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BroadcasterRunnable implements Runnable {

  private static final Logger LOG = LogUtils.getLogger(BroadcasterRunnable.class);

  private Map<String, GameRmiClient> connectedClients;
  private BlockingQueue<OutboxMessage> viewOutbox;

  public BroadcasterRunnable(Map<String, GameRmiClient> connectedClients, BlockingQueue<OutboxMessage> viewOutbox) {
    this.connectedClients = connectedClients;
    this.viewOutbox = viewOutbox;
  }

  @Override
  public void run() {
    while(!Thread.currentThread().isInterrupted()) {
      try {
        OutboxMessage message = viewOutbox.take();
        LOG.info(String.format("Going to broadcast message %s", message.getClass()));
        connectedClients.forEach((s, rmiClient) -> {
          try {
            rmiClient.acceptMessage(message);
          } catch (RemoteException e) {
            LOG.log(Level.WARNING, "This should never happen", e);
          }
        });
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }
}
