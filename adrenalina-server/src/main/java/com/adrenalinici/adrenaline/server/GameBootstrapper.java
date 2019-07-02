package com.adrenalinici.adrenaline.server;

import com.adrenalinici.adrenaline.common.network.inbox.InboxEntry;
import com.adrenalinici.adrenaline.common.network.outbox.OutboxEntry;
import com.adrenalinici.adrenaline.server.network.ServerMessageRouter;
import com.adrenalinici.adrenaline.server.network.ServerNetworkAdapter;
import com.adrenalinici.adrenaline.server.network.rmi.RmiServerNetworkAdapter;
import com.adrenalinici.adrenaline.server.network.socket.SocketServerNetworkAdapter;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class bootstraps the server application, starting from choices
 *
 */
public class GameBootstrapper {

  private BlockingQueue<InboxEntry> inbox;
  private BlockingQueue<OutboxEntry> outboxRmi;
  private BlockingQueue<OutboxEntry> outboxSocket;
  private ServerNetworkAdapter rmiNetworkAdapter;
  private ServerNetworkAdapter socketNetworkAdapter;
  private ServerMessageRouter serverMessageRouter;
  private Thread serverMessageRouterThread;

  private final RmiServerNetworkAdapter.RegistryFactory registryFactory;
  private final int rmiPort;
  private final int socketPort;
  private final long turnTimerSeconds;

  public GameBootstrapper(RmiServerNetworkAdapter.RegistryFactory registryFactory, int rmiPort, int socketPort, long turnTimerSeconds) {
    this.registryFactory = registryFactory;
    this.rmiPort = rmiPort;
    this.socketPort = socketPort;
    this.turnTimerSeconds = turnTimerSeconds;
  }

  public GameBootstrapper(int rmiPort, int socketPort, long turnTimerSeconds) {
    this(LocateRegistry::getRegistry, rmiPort, socketPort, turnTimerSeconds);
  }

  public void start() throws IOException {
    this.inbox = new LinkedBlockingQueue<>();
    this.outboxRmi = new LinkedBlockingQueue<>();
    this.outboxSocket = new LinkedBlockingQueue<>();

    this.serverMessageRouter = ServerMessageRouter.createWithHandlers(inbox, outboxRmi, outboxSocket, turnTimerSeconds);

    this.serverMessageRouterThread = new Thread(serverMessageRouter, "message-router");

    this.rmiNetworkAdapter = new RmiServerNetworkAdapter(registryFactory, inbox, outboxRmi, rmiPort);
    this.socketNetworkAdapter = new SocketServerNetworkAdapter(inbox, outboxSocket, socketPort);

    rmiNetworkAdapter.start();
    socketNetworkAdapter.start();
    serverMessageRouterThread.start();
  }

  public void stop() throws IOException {
    this.serverMessageRouterThread.interrupt();
    this.rmiNetworkAdapter.stop();
    this.socketNetworkAdapter.stop();
  }

  public ServerMessageRouter getServerMessageRouter() {
    return serverMessageRouter;
  }
}
