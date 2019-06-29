package com.adrenalinici.adrenaline.client.socket;

import com.adrenalinici.adrenaline.common.network.NetworkUtils;
import com.adrenalinici.adrenaline.common.network.inbox.InboxMessage;
import com.adrenalinici.adrenaline.common.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.common.util.LogUtils;
import com.adrenalinici.adrenaline.common.util.SerializationUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketEventLoopRunnable implements Runnable {

  private static final Logger LOG = LogUtils.getLogger(SocketEventLoopRunnable.class);

  private Selector selector;
  private BlockingQueue<OutboxMessage> clientViewInbox;
  private BlockingQueue<InboxMessage> clientViewOutbox;

  private Queue<ByteBuffer> remainingWrites;

  public SocketEventLoopRunnable(Selector selector, BlockingQueue<OutboxMessage> clientViewInbox, BlockingQueue<InboxMessage> clientViewOutbox) {
    this.selector = selector;
    this.clientViewInbox = clientViewInbox;
    this.clientViewOutbox = clientViewOutbox;

    this.remainingWrites = new ArrayDeque<>();
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        this.selector.select(); // <- Blocks the thread waiting for new events to process

        Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();
        while (keys.hasNext()) {
          SelectionKey key = keys.next();
          keys.remove();
          if (!key.isValid()) {
            continue;
          }
          if (key.isReadable()) {
            handleRead(key);
          }
          if (key.isWritable()) {
            tryToReadOutboxQueue();
            handleWrite(key);
          }
        }
      } catch (IOException e) {
        LOG.log(Level.SEVERE, "Error in client socket event loop", e);
      } catch (ClosedSelectorException e) {
        Thread.currentThread().interrupt();
      } catch (Exception e) {
        LOG.log(Level.WARNING, "Uncaught exception in SocketEventLoopRunnable", e);
      }
    }
  }

  private void tryToReadOutboxQueue() {
    while (!clientViewOutbox.isEmpty()) {
      InboxMessage message = clientViewOutbox.remove();
      List<ByteBuffer> bufs = NetworkUtils.prepareSerializedBuffer(
        SerializationUtils.serialize(message)
      );

      bufs.forEach(remainingWrites::offer);

      LOG.info(String.format("Prepared message %s", message.getClass()));
    }
  }

  private void handleWrite(SelectionKey key) throws IOException {
    SocketChannel channel = (SocketChannel) key.channel();

    while (!remainingWrites.isEmpty()) {
      ByteBuffer buf = remainingWrites.peek();

      int wrote = channel.write(buf);

      if (buf.hasRemaining() || wrote == 0) {
        return;
      } else {
        remainingWrites.remove().clear();
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
    numRead = 0;
    while (valueBuf.hasRemaining()) {
      int readNow = channel.read(valueBuf);
      if (readNow == -1) {
        numRead = -1;
        break;
      }
      numRead += readNow;
    }

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
