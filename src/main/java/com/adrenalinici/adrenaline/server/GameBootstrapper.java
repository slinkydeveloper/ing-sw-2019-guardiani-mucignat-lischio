package com.adrenalinici.adrenaline.server;

import com.adrenalinici.adrenaline.network.inbox.InboxEntry;
import com.adrenalinici.adrenaline.network.outbox.OutboxEntry;
import com.adrenalinici.adrenaline.network.server.ServerMessageRouter;
import com.adrenalinici.adrenaline.network.server.ServerNetworkAdapter;
import com.adrenalinici.adrenaline.network.server.rmi.RmiServerNetworkAdapter;
import com.adrenalinici.adrenaline.network.server.socket.SocketServerNetworkAdapter;

import java.io.IOException;
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

  private int rmiPort;
  private int socketPort;

  public GameBootstrapper(int rmiPort, int socketPort) {
    this.rmiPort = rmiPort;
    this.socketPort = socketPort;
  }

  public void start() throws IOException {
    this.inbox = new LinkedBlockingQueue<>();
    this.outboxRmi = new LinkedBlockingQueue<>();
    this.outboxSocket = new LinkedBlockingQueue<>();

    this.serverMessageRouter = ServerMessageRouter.createWithHandlers(inbox, outboxRmi, outboxSocket);

    this.serverMessageRouterThread = new Thread(serverMessageRouter, "message-router");

    this.rmiNetworkAdapter = new RmiServerNetworkAdapter(inbox, outboxRmi, rmiPort);
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
