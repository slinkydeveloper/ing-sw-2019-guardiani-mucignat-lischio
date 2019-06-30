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
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketEventLoopRunnable implements Runnable {

  private static final Logger LOG = LogUtils.getLogger(SocketEventLoopRunnable.class);

  private static final long KEEP_ALIVE_PERIOD = 3 * 1000;

  private Selector selector;
  private BlockingQueue<OutboxMessage> clientViewInbox;
  private BlockingQueue<InboxMessage> clientViewOutbox;

  private Queue<ByteBuffer> remainingWrites;
  private ByteBuffer remainingRead;
  private Timer actualTimer;

  public SocketEventLoopRunnable(Selector selector, BlockingQueue<OutboxMessage> clientViewInbox, BlockingQueue<InboxMessage> clientViewOutbox) {
    this.selector = selector;
    this.clientViewInbox = clientViewInbox;
    this.clientViewOutbox = clientViewOutbox;

    this.remainingWrites = new LinkedBlockingQueue<>();

    this.initializeKeepAlive();
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

    if (remainingRead != null) {
      ByteBuffer pending = remainingRead;
      remainingRead = null;
      handlePendingRead(key, channel, pending);
    } else {

      // Read length
      ByteBuffer sizeBuf = ByteBuffer.allocate(4);
      int numRead = channel.read(sizeBuf);

      if (numRead == -1) {
        channel.close();
        key.cancel();
        LOG.severe("Server disconnected!");
        return;
      }
      int size = sizeBuf.getInt(0);
      ByteBuffer payloadBuf = ByteBuffer.allocate(size);

      handlePendingRead(key, channel, payloadBuf);
    }
  }

  private void handlePendingRead(SelectionKey key, SocketChannel channel, ByteBuffer pendingBuffer) throws IOException {
    try {
      int read = channel.read(pendingBuffer);

      if (read == -1) {
        // Connection closed
        channel.close();
        key.cancel();
        LOG.severe("Server disconnected!");
        return;
      }
    } catch (IOException e) {
      channel.close();
      key.cancel();
      LOG.severe("Server disconnected!");
      return;
    }

    if (pendingBuffer.hasRemaining()) {
      // Still missing something to read
      this.remainingRead = pendingBuffer;
    } else {
      // Full buffer, ready to unmarshall

      pendingBuffer.rewind();
      byte[] data = new byte[pendingBuffer.capacity()];
      pendingBuffer.get(data);

      OutboxMessage message = SerializationUtils.deserialize(data);

      if (message != null) {
        this.clientViewInbox.offer(message);
      }
    }
  }

  private void initializeKeepAlive() {
    actualTimer = new Timer();
    actualTimer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        ByteBuffer keepAliveBuf = ByteBuffer.allocate(4).putInt(Integer.MAX_VALUE);
        keepAliveBuf.rewind();
        remainingWrites.offer(keepAliveBuf);
      }
    }, 0, KEEP_ALIVE_PERIOD);
  }

}
