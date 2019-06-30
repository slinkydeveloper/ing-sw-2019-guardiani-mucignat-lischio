package com.adrenalinici.adrenaline.server.network.socket;

import com.adrenalinici.adrenaline.common.network.NetworkUtils;
import com.adrenalinici.adrenaline.common.network.inbox.ConnectedPlayerMessage;
import com.adrenalinici.adrenaline.common.network.inbox.DisconnectedPlayerMessage;
import com.adrenalinici.adrenaline.common.network.inbox.InboxEntry;
import com.adrenalinici.adrenaline.common.network.inbox.InboxMessage;
import com.adrenalinici.adrenaline.common.network.outbox.OutboxEntry;
import com.adrenalinici.adrenaline.common.util.CollectionUtils;
import com.adrenalinici.adrenaline.common.util.LogUtils;
import com.adrenalinici.adrenaline.common.util.SerializationUtils;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketEventLoopRunnable implements Runnable {

  private final static Logger LOG = LogUtils.getLogger(SocketEventLoopRunnable.class);

  private final static long KEEP_ALIVE_THRESHOLD = 10 * 1000;

  private BlockingQueue<OutboxEntry> viewOutbox;
  private BlockingQueue<InboxEntry> viewInbox;
  private Map<String, Queue<ByteBuffer>> remainingWrites;
  private Map<String, ByteBuffer> remainingReads;
  private Selector selector;
  private Map<Socket, String> connectedClients;
  private Map<String, Long> lastKeepAlive;

  public SocketEventLoopRunnable(BlockingQueue<OutboxEntry> viewOutbox, BlockingQueue<InboxEntry> viewInbox, Selector selector) {
    this.viewOutbox = viewOutbox;
    this.viewInbox = viewInbox;
    this.selector = selector;
    this.connectedClients = new HashMap<>();
    this.remainingWrites = new HashMap<>();
    this.remainingReads = new HashMap<>();
    this.lastKeepAlive = new HashMap<>();
  }

  @Override
  public void run() {
    while(!Thread.currentThread().isInterrupted()) {
      try {
        this.selector.select(); // <- Blocks the thread waiting for new events to process

        Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();
        while (keys.hasNext()) {
          SelectionKey key = keys.next();
          keys.remove();
          if (!key.isValid()) {
            continue;
          }
          if (key.isWritable()) {
            tryToReadOutboxQueue();
            handleWrite(key);
          }
          if (key.isReadable()) {
            handleRead(key);
          }
          if (key.isAcceptable()) {
            handleNewConnection((ServerSocketChannel) key.channel());
          }
        }

        checkKeepAlive();
      } catch (IOException e) {
        LOG.log(Level.WARNING, "IOException in SocketEventLoopRunnable", e);
      } catch (ClosedSelectorException e) {
        LOG.log(Level.INFO, "Server selector was closed", e);
        Thread.currentThread().interrupt();
      } catch (CancelledKeyException e) {
        LOG.log(Level.INFO, "Cancelled key", e);
      } catch (Exception e) {
        LOG.log(Level.WARNING, "Uncaught exception in SocketEventLoopRunnable", e);
      }
    }
  }

  private void tryToReadOutboxQueue() {
    while (!viewOutbox.isEmpty()) {
      OutboxEntry message = viewOutbox.remove();

      if (connectedClients.containsValue(message.getConnectionId())) {
        CollectionUtils
          .keys(connectedClients, message.getConnectionId())
          .forEach(s -> {

            List<ByteBuffer> bufs = NetworkUtils.prepareSerializedBuffer(
              SerializationUtils.serialize(message.getMessage())
            );

            LOG.info(String.format("Prepared message %s splitted in %d chunks for %s", message.getMessage().getClass(), bufs.size(), message.getConnectionId()));

            Queue<ByteBuffer> queue = remainingWrites
              .computeIfAbsent(message.getConnectionId(), k -> new ArrayDeque<>());

            bufs.forEach(queue::offer);
          });
      } else {
        LOG.finer(String.format("Thread %s discards message %s", Thread.currentThread().getName(), message));
      }
    }
  }

  private void handleWrite(SelectionKey key) throws IOException {
    SocketChannel channel = (SocketChannel) key.channel();
    Socket socket = channel.socket();

    String connectionId = connectedClients.get(socket);

    try {
      Queue<ByteBuffer> bufQueue = this.remainingWrites.computeIfAbsent(connectionId, k -> new ArrayDeque<>());

      while (!bufQueue.isEmpty()) {
        ByteBuffer buf = bufQueue.peek();

        int wrote = channel.write(buf);

        if (buf.hasRemaining() || wrote == 0) {
          return;
        } else {
          bufQueue.remove().clear();
        }
      }
    } catch (IOException e) {
      handleDisconnection(key);
      LOG.log(Level.WARNING, "IOException while writing payload to " + connectionId + " (" + channel.getRemoteAddress() + ")", e);
    }
  }

  private void handlePendingRead(SelectionKey key, SocketChannel channel, String connectionId, ByteBuffer pendingBuffer) throws IOException {
    try {
      int read = channel.read(pendingBuffer);

      if (read == -1) {
        // Connection closed
        handleDisconnection(key);
        return;
      }
    } catch (IOException e) {
      handleDisconnection(key);
      LOG.log(Level.WARNING, "IOException while reading payload from " + connectionId + " (" + channel.getRemoteAddress() + ")", e);
      return;
    }

    if (pendingBuffer.hasRemaining()) {
      // Still missing something to read
      this.remainingReads.put(connectionId, pendingBuffer);
    } else {
      // Full buffer, ready to unmarshall

      pendingBuffer.rewind();
      byte[] data = new byte[pendingBuffer.capacity()];
      pendingBuffer.get(data);

      InboxMessage message = SerializationUtils.deserialize(data);
      LOG.info(String.format(
        "Received inbox message %s from address %s (connection id %s)",
        message.getClass(),
        channel.getRemoteAddress(),
        connectionId
      ));
      this.viewInbox.offer(new InboxEntry(connectionId, message));
    }
  }

  private void handleRead(SelectionKey key) throws IOException {
    SocketChannel channel = (SocketChannel) key.channel();
    Socket socket = channel.socket();
    String connectionId = connectedClients.get(socket);

    if (this.remainingReads.containsKey(connectionId)) {
      handlePendingRead(
        key,
        channel,
        connectionId,
        this.remainingReads.remove(connectionId)
      );
    } else {
      // Read the size of next message
      ByteBuffer sizeBuf = ByteBuffer.allocate(4);
      int numRead;
      try {
        numRead = channel.read(sizeBuf);
      } catch (IOException e) {
        handleDisconnection(key);
        LOG.log(Level.WARNING, "IOException while reading payload size from " + connectionId + " (" + channel.getRemoteAddress() + ")", e);
        return;
      }

      if (numRead == -1) {
        handleDisconnection(key);
        return;
      }

      int payloadSize = sizeBuf.getInt(0);

      if (payloadSize == Integer.MAX_VALUE) {
        lastKeepAlive.put(connectionId, System.currentTimeMillis());
        LOG.info(String.format("Keep alive from connection id %s and %s", connectionId, channel.getRemoteAddress()));
      } else {
        // Prepare the payload buf
        ByteBuffer payloadBuf = ByteBuffer.allocate(payloadSize);

        handlePendingRead(key, channel, connectionId, payloadBuf);
      }
    }
  }

  private void handleNewConnection(ServerSocketChannel serverChannel) throws IOException {
    SocketChannel channel = serverChannel.accept(); // Retrieve client socket channel
    channel.configureBlocking(false); // Configure socket as non blocking

    channel.register(this.selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

    channel.socket().setKeepAlive(true);
    channel.socket().setReceiveBufferSize(64 * 1024 * 1024);
    channel.socket().setSendBufferSize(64 * 1024 * 1024);

    String connectionId = UUID.randomUUID().toString();
    this.connectedClients.put(channel.socket(), connectionId);
    LOG.info(String.format("New connection from address %s | Connection id: %s", channel.socket().getInetAddress(), connectionId));
    this.viewInbox.offer(new InboxEntry(connectionId, new ConnectedPlayerMessage()));
  }

  private void handleDisconnection(SelectionKey key) throws IOException {
    SocketChannel channel = (SocketChannel) key.channel();
    Socket socket = channel.socket();

    channel.close();
    key.cancel();

    String connectionId = this.connectedClients.remove(socket);
    LOG.info(String.format("Connection with id %s and address %s disconnected", connectionId, socket.getInetAddress()));
    this.viewInbox.offer(new InboxEntry(connectionId, new DisconnectedPlayerMessage()));
  }

  private void handleDisconnection(String connectionId) {
    this.connectedClients.entrySet().stream().filter(f -> f.getValue().equals(connectionId)).map(Map.Entry::getKey).findFirst().ifPresent(socket -> {
      this.connectedClients.remove(socket);
      LOG.info(String.format("Connection with id %s and address %s disconnected", connectionId, socket.getInetAddress()));
      this.viewInbox.offer(new InboxEntry(connectionId, new DisconnectedPlayerMessage()));
    });
  }

  private void checkKeepAlive() {
    lastKeepAlive.forEach((connectionId, lastTime) -> {
      if (System.currentTimeMillis() - lastTime > KEEP_ALIVE_THRESHOLD) {
        handleDisconnection(connectionId);
      }
    });
  }
}
