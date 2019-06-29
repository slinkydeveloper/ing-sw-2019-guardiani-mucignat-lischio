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

  private BlockingQueue<OutboxEntry> viewOutbox;
  private BlockingQueue<InboxEntry> viewInbox;
  private Map<String, Queue<ByteBuffer>> remainingWrites;
  private Selector selector;
  private Map<Socket, String> connectedClients;

  public SocketEventLoopRunnable(BlockingQueue<OutboxEntry> viewOutbox, BlockingQueue<InboxEntry> viewInbox, Selector selector) {
    this.viewOutbox = viewOutbox;
    this.viewInbox = viewInbox;
    this.selector = selector;
    this.connectedClients = new HashMap<>();
    this.remainingWrites = new HashMap<>();
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
      } catch (IOException e) {
        LOG.log(Level.WARNING, "IOException in SenderRunnable", e);
      } catch (ClosedSelectorException e) {
        LOG.log(Level.WARNING, "Selector was closed!", e);
        Thread.currentThread().interrupt();
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
            LOG.info(String.format("Prepared message %s for %s", message.getMessage().getClass(), message.getConnectionId()));

            List<ByteBuffer> bufs = NetworkUtils.prepareSerializedBuffer(
              SerializationUtils.serialize(message.getMessage())
            );
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
      handleDisconnection(connectionId, socket, key, channel);
    }
  }

  private void handleRead(SelectionKey key) throws IOException {
    SocketChannel channel = (SocketChannel) key.channel();
    Socket socket = channel.socket();
    String connectionId = connectedClients.get(socket);

    ByteBuffer sizeBuf = ByteBuffer.allocate(4);
    int numRead;
    try {
      numRead = channel.read(sizeBuf);
    } catch (IOException e) {
      handleDisconnection(connectionId, socket, key, channel);
      return;
    }

    if (numRead == -1) {
      handleDisconnection(connectionId, socket, key, channel);
      return;
    }

    int size = sizeBuf.getInt(0);
    ByteBuffer valueBuf = ByteBuffer.allocate(size);
    try {
      numRead = 0;
      while (valueBuf.hasRemaining()) {
        int readNow = channel.read(valueBuf);
        if (readNow == -1) {
          handleDisconnection(connectionId, socket, key, channel);
          return;
        }
        numRead += readNow;
      }
    } catch (IOException e) {
      handleDisconnection(connectionId, socket, key, channel);
      return;
    }

    if (numRead == -1) {
      handleDisconnection(connectionId, socket, key, channel);
      return;
    }

    byte[] data = new byte[numRead];
    System.arraycopy(valueBuf.array(), 0, data, 0, numRead);
    InboxMessage message = SerializationUtils.deserialize(data);
    LOG.info(String.format("Received inbox message %s from address %s (connection id %s)", message.getClass(), connectionId, socket.getInetAddress()));
    this.viewInbox.offer(new InboxEntry(connectionId, message));
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

  private void handleDisconnection(String connectionId, Socket socket, SelectionKey key, SocketChannel channel) throws IOException {
    channel.close();
    key.cancel();
    LOG.info(String.format("Connection with id %s and address %s disconnected", connectionId, socket.getInetAddress()));
    this.connectedClients.remove(socket);
    this.viewInbox.offer(new InboxEntry(connectionId, new DisconnectedPlayerMessage()));
  }
}
