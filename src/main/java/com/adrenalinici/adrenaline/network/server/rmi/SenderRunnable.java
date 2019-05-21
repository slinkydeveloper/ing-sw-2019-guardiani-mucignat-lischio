package com.adrenalinici.adrenaline.network.server.rmi;

import com.adrenalinici.adrenaline.network.client.rmi.GameRmiClient;
import com.adrenalinici.adrenaline.network.outbox.OutboxEntry;
import com.adrenalinici.adrenaline.util.LogUtils;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SenderRunnable implements Runnable {

  private static final Logger LOG = LogUtils.getLogger(SenderRunnable.class);

  private Map<String, GameRmiClient> connectedClients;
  private BlockingQueue<OutboxEntry> viewOutbox;

  public SenderRunnable(Map<String, GameRmiClient> connectedClients, BlockingQueue<OutboxEntry> viewOutbox) {
    this.connectedClients = connectedClients;
    this.viewOutbox = viewOutbox;
  }

  @Override
  public void run() {
    while(!Thread.currentThread().isInterrupted()) {
      try {
        OutboxEntry message = viewOutbox.take();
        if (connectedClients.containsKey(message.getConnectionId())) {
          try {
            connectedClients.get(message.getConnectionId()).acceptMessage(message.getMessage());
          } catch (RemoteException e) {
            LOG.log(Level.WARNING, "This should never happen", e);
          }
        } else {
          LOG.finer(String.format("Thread %s discards message %s", Thread.currentThread().getName(), message));
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }
}
