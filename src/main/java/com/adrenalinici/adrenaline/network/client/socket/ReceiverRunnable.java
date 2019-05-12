package com.adrenalinici.adrenaline.network.client.socket;

import com.adrenalinici.adrenaline.network.inbox.ConnectedPlayerMessage;
import com.adrenalinici.adrenaline.network.inbox.DisconnectedPlayerMessage;
import com.adrenalinici.adrenaline.network.inbox.InboxEntry;
import com.adrenalinici.adrenaline.network.inbox.InboxMessage;
import com.adrenalinici.adrenaline.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.util.LogUtils;
import com.adrenalinici.adrenaline.util.SerializationUtils;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiverRunnable implements Runnable {

  private static final Logger LOG = LogUtils.getLogger(ReceiverRunnable.class);

  private Selector readSelector;
  private BlockingQueue<OutboxMessage> clientViewInbox;

  public ReceiverRunnable(Selector readSelector, BlockingQueue<OutboxMessage> clientViewInbox) {
    this.readSelector = readSelector;
    this.clientViewInbox = clientViewInbox;
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        this.readSelector.select(); // <- Blocks the thread waiting for new events to process

        Iterator<SelectionKey> keys = this.readSelector.selectedKeys().iterator();
        while (keys.hasNext()) {
          SelectionKey key = keys.next();
          keys.remove();
          if (!key.isValid()) {
            continue;
          }
          if (key.isReadable()) {
            handleRead(key);
          }
        }
      } catch (IOException e) {
        LOG.log(Level.SEVERE, "Error in client socket BroadcasterRunnable", e);
      } catch (ClosedSelectorException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  private void handleRead(SelectionKey key) throws IOException {
    SocketChannel channel = (SocketChannel) key.channel();
    ByteBuffer sizeBuf = ByteBuffer.allocate(4);
    int numRead = channel.read(sizeBuf);

    if (numRead == -1) {
      channel.close();
      key.cancel();
      LOG.severe("Server disconnected!");
      return;
    }
    int size = sizeBuf.getInt(0);
    ByteBuffer valueBuf = ByteBuffer.allocate(size);
    numRead = channel.read(valueBuf);

    if (numRead == -1) {
      channel.close();
      key.cancel();
      LOG.severe("Server disconnected!");
      return;
    }

    byte[] data = new byte[numRead];
    System.arraycopy(valueBuf.array(), 0, data, 0, numRead);
    OutboxMessage message = SerializationUtils.deserialize(data);
    if (message != null) {
      LOG.fine(String.format("Received message from server %s", message.getClass()));
      this.clientViewInbox.offer(message);
    }
  }

}
