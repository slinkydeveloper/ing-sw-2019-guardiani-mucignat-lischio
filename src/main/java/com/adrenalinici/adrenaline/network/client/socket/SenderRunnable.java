package com.adrenalinici.adrenaline.network.client.socket;

import com.adrenalinici.adrenaline.network.inbox.InboxMessage;
import com.adrenalinici.adrenaline.util.LogUtils;
import com.adrenalinici.adrenaline.util.SerializationUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SenderRunnable implements Runnable {

  private static final Logger LOG = LogUtils.getLogger(SenderRunnable.class);

  private BlockingQueue<InboxMessage> clientViewOutbox;
  private SocketChannel channel;

  public SenderRunnable(BlockingQueue<InboxMessage> clientViewOutbox, SocketChannel channel) {
    this.clientViewOutbox = clientViewOutbox;
    this.channel = channel;
  }

  @Override
  public void run() {
    while(!Thread.currentThread().isInterrupted()) {
      try {
        InboxMessage message = clientViewOutbox.take();
        LOG.fine(String.format("Going to send message %s", message.getClass()));
        channel.write(ByteBuffer.wrap(SerializationUtils.serialize(message)));
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      } catch (IOException e) {
        LOG.log(Level.SEVERE, "IOException while sending a message", e);
      }
    }
  }
}
