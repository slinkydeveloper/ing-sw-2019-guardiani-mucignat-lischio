package com.adrenalinici.adrenaline.network.server.rmi;

import com.adrenalinici.adrenaline.network.client.rmi.GameRmiClient;
import com.adrenalinici.adrenaline.network.inbox.ConnectedPlayerMessage;
import com.adrenalinici.adrenaline.network.inbox.InboxEntry;
import com.adrenalinici.adrenaline.network.inbox.InboxMessage;
import com.adrenalinici.adrenaline.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.network.server.ServerNetworkAdapter;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class RmiServerNetworkAdapter extends ServerNetworkAdapter implements GameRmiServer {

  public static final int PORT = Integer.parseInt(System.getenv().getOrDefault("RMI_PORT", "9001"));

  private Map<String, String> addressToConnectionId;
  private Map<String, GameRmiClient> connectionIdToClient;

  private Thread broadcasterThread;
  private Registry registry;

  public RmiServerNetworkAdapter(BlockingQueue<InboxEntry> viewInbox, BlockingQueue<OutboxMessage> viewOutbox) {
    super(viewInbox, viewOutbox);
    this.addressToConnectionId = new ConcurrentHashMap<>();
    this.connectionIdToClient = new ConcurrentHashMap<>();
  }

  @Override
  public void start() throws IOException {
    Remote remoteObject = UnicastRemoteObject.exportObject(this, PORT);
    registry = LocateRegistry.createRegistry(PORT);
    registry.rebind(GameRmiServer.class.getSimpleName(), remoteObject);

    broadcasterThread = new Thread(new BroadcasterRunnable(connectionIdToClient, viewOutbox));
    broadcasterThread.start();
  }

  @Override
  public void stop() throws IOException {
    try {
      registry.unbind(GameRmiServer.class.getSimpleName());
    } catch (NotBoundException e) {
      e.printStackTrace();
    }
    if (broadcasterThread != null)
      broadcasterThread.interrupt();
  }

  @Override
  public void acceptMessage(InboxMessage message, GameRmiClient client) throws RemoteException {
    try {
      viewInbox.offer(new InboxEntry(addressToConnectionId.get(RemoteServer.getClientHost()), message));
    } catch (ServerNotActiveException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void startConnection(GameRmiClient client) throws RemoteException {
    try {
      String connectionAddress = RemoteServer.getClientHost(); //TODO only host!!!
      String connectionId = UUID.randomUUID().toString();
      addressToConnectionId.put(connectionAddress, connectionId);
      connectionIdToClient.put(connectionId, client);
      viewInbox.offer(new InboxEntry(
        addressToConnectionId.get(connectionAddress),
        new ConnectedPlayerMessage()
      ));
    } catch (ServerNotActiveException e) {
      e.printStackTrace();
    }
  }
}
