package com.adrenalinici.adrenaline.server.network.rmi;

import com.adrenalinici.adrenaline.common.network.inbox.ConnectedPlayerMessage;
import com.adrenalinici.adrenaline.common.network.inbox.DisconnectedPlayerMessage;
import com.adrenalinici.adrenaline.common.network.inbox.InboxEntry;
import com.adrenalinici.adrenaline.common.network.inbox.InboxMessage;
import com.adrenalinici.adrenaline.common.network.outbox.OutboxEntry;
import com.adrenalinici.adrenaline.common.network.rmi.GameRmiClient;
import com.adrenalinici.adrenaline.common.network.rmi.GameRmiServer;
import com.adrenalinici.adrenaline.common.util.LogUtils;
import com.adrenalinici.adrenaline.server.network.ServerNetworkAdapter;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RmiServerNetworkAdapter extends ServerNetworkAdapter implements GameRmiServer {

  private static final Logger LOG = LogUtils.getLogger(RmiServerNetworkAdapter.class);
  private final static long KEEP_ALIVE_THRESHOLD = 10 * 1000;

  private int port;
  private Map<String, String> addressToConnectionId;
  private Map<String, GameRmiClient> connectionIdToClient;
  private Map<String, Long> lastKeepAlive;
  private Timer keepAliveTimer;

  private Thread senderThread;
  private Registry registry;

  public RmiServerNetworkAdapter(BlockingQueue<InboxEntry> viewInbox, BlockingQueue<OutboxEntry> viewOutbox, int port) {
    super(viewInbox, viewOutbox);
    this.addressToConnectionId = new ConcurrentHashMap<>();
    this.connectionIdToClient = new ConcurrentHashMap<>();
    this.lastKeepAlive = new ConcurrentHashMap<>();
    this.port = port;

    initializeKeepAlive();
  }

  private void initializeKeepAlive() {
    this.keepAliveTimer = new Timer();
    this.keepAliveTimer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        lastKeepAlive.forEach((connectionId, lastTime) -> {
          if (System.currentTimeMillis() - lastTime > KEEP_ALIVE_THRESHOLD) {
            handleDisconnection(connectionId);
          }
        });
      }
    }, 0, KEEP_ALIVE_THRESHOLD);
  }

  private void handleDisconnection(String connectionId) {
    this.addressToConnectionId
      .entrySet()
      .stream()
      .filter(f -> f.getValue().equals(connectionId))
      .map(Map.Entry::getKey)
      .findFirst()
      .ifPresent(address -> {
      this.addressToConnectionId.remove(address);
      LOG.info(String.format("Connection with id %s and host %s disconnected", connectionId, getRemoteHost()));
      this.viewInbox.offer(new InboxEntry(connectionId, new DisconnectedPlayerMessage()));
    });
  }

  @Override
  public void start() throws IOException {
    Remote remoteObject = UnicastRemoteObject.exportObject(this, port);
    registry = LocateRegistry.createRegistry(port);
    registry.rebind(GameRmiServer.class.getSimpleName(), remoteObject);

    LOG.info(String.format("Started rmi registry on port %d", port));

    senderThread = new Thread(new SenderRunnable(connectionIdToClient, viewOutbox), "rmi-sender");
    senderThread.start();
  }

  @Override
  public void stop() throws IOException {
    try {
      this.keepAliveTimer.cancel();
      registry.unbind(GameRmiServer.class.getSimpleName());
      UnicastRemoteObject.unexportObject(registry, true);
      LOG.info(String.format("Stopped rmi registry on port %d", port));
    } catch (NotBoundException e) {}
    if (senderThread != null)
      senderThread.interrupt();
  }

  @Override
  public void acceptMessage(InboxMessage message, GameRmiClient client) throws RemoteException {
    if (!addressToConnectionId.containsKey(getRemoteHost())) {
      LOG.info(String.format("Received a message from unregistered host %s", getRemoteHost()));
    } else {
      LOG.info(String.format("Received message %s from %s", message, addressToConnectionId.get(getRemoteHost())));
      viewInbox.offer(new InboxEntry(addressToConnectionId.get(getRemoteHost()), message));
    }
  }

  @Override
  public void startConnection(GameRmiClient client) throws RemoteException {
    String connectionAddress = getRemoteHost();
    String connectionId = UUID.randomUUID().toString();
    addressToConnectionId.put(connectionAddress, connectionId);
    connectionIdToClient.put(connectionId, client);
    LOG.info(String.format("New connection from %s with %s", connectionAddress, connectionId));
    viewInbox.offer(new InboxEntry(
      addressToConnectionId.get(connectionAddress),
      new ConnectedPlayerMessage()
    ));
  }

  @Override
  public void keepAlive() throws RemoteException {
    this.lastKeepAlive.put(this.addressToConnectionId.get(getRemoteHost()), System.currentTimeMillis());
  }

  private String getRemoteHost() {
    try {
      return RemoteServer.getClientHost();
      //Only host!!! This can cause bugs where multiple clients are on the same host!
      // Thank you RMI for this beautiful API, I'm so happy that these things are not used
      // anymore in production environments
    } catch (ServerNotActiveException e) {
      LOG.log(Level.SEVERE, "Can't get client host", e);
      throw new RuntimeException(e);
    }
  }
}
