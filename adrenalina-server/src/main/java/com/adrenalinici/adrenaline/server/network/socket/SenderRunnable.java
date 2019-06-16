package com.adrenalinici.adrenaline.server.network.socket;

import com.adrenalinici.adrenaline.common.network.outbox.OutboxEntry;
import com.adrenalinici.adrenaline.common.util.CollectionUtils;
import com.adrenalinici.adrenaline.common.util.LogUtils;
import com.adrenalinici.adrenaline.common.util.SerializationUtils;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SenderRunnable extends BaseSocketRunnable {

  private static final Logger LOG = LogUtils.getLogger(SenderRunnable.class);

  private BlockingQueue<OutboxEntry> viewOutbox;

  public SenderRunnable(Map<Socket, String> connectedClients, BlockingQueue<OutboxEntry> viewOutbox) {
    super(connectedClients);
    this.viewOutbox = viewOutbox;
  }

  @Override
  public void run() {
    while(!Thread.currentThread().isInterrupted()) {
      try {
        OutboxEntry message = viewOutbox.take();
        if (connectedClients.containsValue(message.getConnectionId())) {
          CollectionUtils.keys(connectedClients, message.getConnectionId())
            .forEach(s -> {
              try {
                LOG.info(String.format("Going to send message %s to %s", message.getMessage().getClass(), message.getConnectionId()));
                byte[] serialized = SerializationUtils.serialize(message.getMessage());
                ByteBuffer bufWithSize = ByteBuffer.allocate(serialized.length + 4);
                bufWithSize.putInt(serialized.length);
                bufWithSize.put(serialized);
                bufWithSize.rewind();
                s.getChannel().write(bufWithSize);
              } catch (IOException e) {
                LOG.log(Level.WARNING, "IOException while broadcasting message", e);
              }
            });
        } else {
          LOG.finer(String.format("Thread %s discards message %s", Thread.currentThread().getName(), message));
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }
}
