package com.adrenalinici.adrenaline.network.client.rmi;

import com.adrenalinici.adrenaline.network.client.ClientNetworkAdapter;
import com.adrenalinici.adrenaline.network.client.ClientViewProxy;
import com.adrenalinici.adrenaline.network.server.rmi.GameRmiServer;
import com.adrenalinici.adrenaline.util.LogUtils;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RmiClientNetworkAdapter extends ClientNetworkAdapter {

  private static final Logger LOG = LogUtils.getLogger(RmiClientNetworkAdapter.class);

  private Thread senderThread;
  private Registry registry;
  private GameRmiServer server;
  private String host;
  private int port;

  public RmiClientNetworkAdapter(ClientViewProxy proxy, String host, int port) {
    super(proxy);
    this.port = port;
    this.host = host;
  }

  @Override
  public void initialize() throws IOException {
    if (registry == null) { // Avoid double initialization
      try {
        registry = LocateRegistry.getRegistry(host, port);

        server = (GameRmiServer) registry.lookup(GameRmiServer.class.getSimpleName());

        GameRmiClient gameRmiClientInstance = new GameRmiClientImpl(this.clientViewInbox);
        GameRmiClient remote = (GameRmiClient) UnicastRemoteObject.exportObject(gameRmiClientInstance, 0);

        senderThread = new Thread(new SenderRunnable(clientViewOutbox, server, remote));
        senderThread.start();
        server.startConnection(remote);

        LOG.info("Connected to server");
      } catch (NotBoundException e) {
        LOG.log(Level.SEVERE, "Error while starting rmi client adapter", e);
      }
    }
  }

  @Override
  public void stop() throws IOException {
    if (this.senderThread != null)
      this.senderThread.interrupt();
  }
}
