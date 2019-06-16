package com.adrenalinici.adrenaline.server.network.socket;

import com.adrenalinici.adrenaline.common.network.inbox.ConnectedPlayerMessage;
import com.adrenalinici.adrenaline.common.network.inbox.DisconnectedPlayerMessage;
import com.adrenalinici.adrenaline.common.network.inbox.InboxEntry;
import com.adrenalinici.adrenaline.common.network.inbox.InboxMessage;
import com.adrenalinici.adrenaline.common.util.LogUtils;
import com.adrenalinici.adrenaline.common.util.SerializationUtils;

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

public class ReceiverRunnable extends BaseSocketRunnable {

  private static final Logger LOG = LogUtils.getLogger(ReceiverRunnable.class);

  private Selector readSelector;
  private BlockingQueue<InboxEntry> viewInbox;

  public ReceiverRunnable(Map<Socket, String> connectedClients, Selector readSelector, BlockingQueue<InboxEntry> viewInbox) {
    super(connectedClients);
    this.readSelector = readSelector;
    this.viewInbox = viewInbox;
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
          } else if (key.isAcceptable()) {
            handleNewConnection((ServerSocketChannel) key.channel());
          }
        }
      } catch (IOException e) {
        LOG.log(Level.WARNING, "IOException in NewConnectionAcceptRunnable", e);
      } catch (ClosedSelectorException e) {
        Thread.currentThread().interrupt();
      }
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
      handleUserDisconnection(connectionId, socket, key, channel);
      return;
    }

    if (numRead == -1) {
      handleUserDisconnection(connectionId, socket, key, channel);
      return;
    }

    int size = sizeBuf.getInt(0);
    ByteBuffer valueBuf = ByteBuffer.allocate(size);
    try {
      numRead = 0;
      while (valueBuf.hasRemaining()) {
        int readNow = channel.read(valueBuf);
        if (readNow == -1) {
          handleUserDisconnection(connectionId, socket, key, channel);
          return;
        }
        numRead += readNow;
      }
    } catch (IOException e) {
      handleUserDisconnection(connectionId, socket, key, channel);
      return;
    }

    if (numRead == -1) {
      handleUserDisconnection(connectionId, socket, key, channel);
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
    channel.register(this.readSelector, SelectionKey.OP_READ);

    String connectionId = UUID.randomUUID().toString();
    this.connectedClients.put(channel.socket(), connectionId);
    LOG.info(String.format("New connection from address %s | Connection id: %s", channel.socket().getInetAddress(), connectionId));
    this.viewInbox.offer(new InboxEntry(connectionId, new ConnectedPlayerMessage()));
  }

  private void handleUserDisconnection(String connectionId, Socket socket, SelectionKey key, SocketChannel channel) throws IOException {
    channel.close();
    key.cancel();
    LOG.info(String.format("Connection with id %s and address %s disconnected", connectionId, socket.getInetAddress()));
    this.connectedClients.remove(socket);
    this.viewInbox.offer(new InboxEntry(connectionId, new DisconnectedPlayerMessage()));
  }

}
