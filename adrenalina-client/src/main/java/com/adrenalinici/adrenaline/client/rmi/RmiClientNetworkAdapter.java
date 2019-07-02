package com.adrenalinici.adrenaline.client.rmi;

import com.adrenalinici.adrenaline.client.ClientNetworkAdapter;
import com.adrenalinici.adrenaline.client.ClientViewProxy;
import com.adrenalinici.adrenaline.common.network.rmi.GameRmiClient;
import com.adrenalinici.adrenaline.common.network.rmi.GameRmiServer;
import com.adrenalinici.adrenaline.common.util.LogUtils;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RmiClientNetworkAdapter extends ClientNetworkAdapter {

  private static final Logger LOG = LogUtils.getLogger(RmiClientNetworkAdapter.class);

  private static final long KEEP_ALIVE_PERIOD = 3L * 1000L;

  private Thread senderThread;
  private Registry registry;
  private GameRmiServer server;
  private String host;
  private int port;
  private Timer keepAliveTimer;

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

        initializeKeepAlive();

        LOG.info("Connected to server");
      } catch (NotBoundException e) {
        LOG.log(Level.SEVERE, "Error while starting rmi client adapter", e);
      }
    }
  }

  @Override
  public void stop() throws IOException {
    if (keepAliveTimer != null)
      this.keepAliveTimer.cancel();
    if (this.senderThread != null)
      this.senderThread.interrupt();
  }

  private void initializeKeepAlive() {
    keepAliveTimer = new Timer();
    keepAliveTimer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        try {
          server.keepAlive();
        } catch (RemoteException e) { }
      }
    }, 0, KEEP_ALIVE_PERIOD);
  }
}
