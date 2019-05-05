package com.adrenalinici.adrenaline.network.client.rmi;

import com.adrenalinici.adrenaline.network.client.ClientNetworkAdapter;
import com.adrenalinici.adrenaline.network.client.ClientViewProxy;
import com.adrenalinici.adrenaline.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.network.server.rmi.GameRmiServer;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiClientNetworkAdapter extends ClientNetworkAdapter {

  private static final String ADDRESS = System.getenv().getOrDefault("ADDRESS", "localhost");
  private static final int PORT = Integer.parseInt(System.getenv().getOrDefault("RMI_PORT", "9001"));

  private Thread senderThread;
  private Registry registry;
  private GameRmiServer server;

  public RmiClientNetworkAdapter(ClientViewProxy proxy) {
    super(proxy);
  }

  @Override
  public void initialize() throws IOException {
    try {
      registry = LocateRegistry.getRegistry(ADDRESS, PORT);

      server = (GameRmiServer) registry.lookup(GameRmiServer.class.getSimpleName());

      GameRmiClient gameRmiClientInstance = new GameRmiClientImpl(this.clientViewInbox);
      GameRmiClient remote = (GameRmiClient) UnicastRemoteObject.exportObject(gameRmiClientInstance, 0);

      senderThread = new Thread(new SenderRunnable(clientViewOutbox, server, remote));
      senderThread.start();
      server.startConnection(remote);
    } catch (NotBoundException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void stop() throws IOException {
    if (this.senderThread != null)
      this.senderThread.interrupt();
  }
}
