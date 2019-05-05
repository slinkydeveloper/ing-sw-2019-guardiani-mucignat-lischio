package com.adrenalinici.adrenaline.network.server.socket;

import com.adrenalinici.adrenaline.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.network.server.BaseGameViewServer;
import com.adrenalinici.adrenaline.util.LogUtils;
import com.adrenalinici.adrenaline.util.SerializationUtils;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class BroadcasterRunnable extends BaseSocketRunnable {

  private static final Logger LOG = LogUtils.getLogger(BroadcasterRunnable.class);

  private BlockingQueue<OutboxMessage> viewOutbox;

  public BroadcasterRunnable(Map<Socket, String> connectedClients, BlockingQueue<OutboxMessage> viewOutbox) {
    super(connectedClients);
    this.viewOutbox = viewOutbox;
  }

  @Override
  public void run() {
    while(!Thread.currentThread().isInterrupted()) {
      try {
        OutboxMessage message = viewOutbox.take();
        LOG.fine(String.format("Going to broadcast message %s", message.getClass()));
        connectedClients.forEach((s, cid) -> {
          ByteBuffer byteBuffer = ByteBuffer.wrap(SerializationUtils.serialize(message));
          try {
            s.getChannel().write(byteBuffer);
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
      } catch (InterruptedException e) {}
    }
  }
}
