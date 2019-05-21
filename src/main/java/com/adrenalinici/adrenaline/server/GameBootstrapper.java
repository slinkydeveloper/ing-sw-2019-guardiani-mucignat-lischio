package com.adrenalinici.adrenaline.server;

import com.adrenalinici.adrenaline.controller.GameController;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.fat.Dashboard;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.model.fat.PlayerDashboard;
import com.adrenalinici.adrenaline.network.inbox.InboxEntry;
import com.adrenalinici.adrenaline.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.network.server.GameViewServer;
import com.adrenalinici.adrenaline.network.server.ServerNetworkAdapter;
import com.adrenalinici.adrenaline.network.server.rmi.RmiServerNetworkAdapter;
import com.adrenalinici.adrenaline.network.server.socket.SocketServerNetworkAdapter;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class bootstraps the server application, starting from choices
 *
 */
public class GameBootstrapper {

  private GameModel model;
  private GameController controller;
  private BlockingQueue<InboxEntry> inbox;
  private BlockingQueue<OutboxMessage> outboxRmi;
  private BlockingQueue<OutboxMessage> outboxSocket;
  private ServerNetworkAdapter rmiNetworkAdapter;
  private ServerNetworkAdapter socketNetworkAdapter;
  private GameViewServer serverGameView;
  private Thread serverRemoteViewThread;
  private String gameId;

  private DashboardChoice dashboardChoice;
  private PlayersChoice playersChoice;
  private RulesChoice choice;
  private int rmiPort;
  private int socketPort;

  public GameBootstrapper(DashboardChoice dashboardChoice, PlayersChoice playersChoice, RulesChoice choice, int rmiPort, int socketPort) {
    this.dashboardChoice = dashboardChoice;
    this.playersChoice = playersChoice;
    this.choice = choice;
    this.rmiPort = rmiPort;
    this.socketPort = socketPort;
  }

  public void start() throws IOException {
    this.gameId = UUID.randomUUID().toString();

    Dashboard dashboard = dashboardChoice.generate();
    List<PlayerDashboard> playerDashboards = playersChoice.generate();
    this.model = choice.generate(dashboard, playerDashboards);

    this.inbox = new LinkedBlockingQueue<>();
    this.outboxRmi = new LinkedBlockingQueue<>();
    this.outboxSocket = new LinkedBlockingQueue<>();

    this.serverGameView = new GameViewServer(inbox, outboxRmi, outboxSocket, new HashSet<>(model.getPlayers()));
    this.controller = new GameController(model);
    this.serverGameView.registerObserver(controller);
    this.model.registerObserver(this.serverGameView);

    this.serverRemoteViewThread = new Thread(serverGameView, "game-view-server-game-" + gameId);

    this.rmiNetworkAdapter = new RmiServerNetworkAdapter(inbox, outboxRmi, rmiPort, gameId);
    this.socketNetworkAdapter = new SocketServerNetworkAdapter(inbox, outboxSocket, socketPort, gameId);

    rmiNetworkAdapter.start();
    socketNetworkAdapter.start();
    serverRemoteViewThread.start();
  }

  public void stop() throws IOException {
    this.serverRemoteViewThread.interrupt();
    this.rmiNetworkAdapter.stop();
    this.socketNetworkAdapter.stop();
  }

  public GameController getController() {
    return controller;
  }

  public GameModel getModel() {
    return model;
  }
}
