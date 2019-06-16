package com.adrenalinici.adrenaline.server.network;

import com.adrenalinici.adrenaline.common.network.inbox.InboxEntry;
import com.adrenalinici.adrenaline.common.network.inbox.InboxMessage;
import com.adrenalinici.adrenaline.common.network.outbox.OutboxEntry;
import com.adrenalinici.adrenaline.common.network.outbox.OutboxMessage;
import com.adrenalinici.adrenaline.common.util.CollectionUtils;
import com.adrenalinici.adrenaline.common.util.LogUtils;
import com.adrenalinici.adrenaline.server.controller.GameController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class ServerContext {

  private static final Logger LOG = LogUtils.getLogger(ServerContext.class);

  private final BlockingQueue<InboxEntry> inbox;
  private final BlockingQueue<OutboxEntry> outboxRmi;
  private final BlockingQueue<OutboxEntry> outboxSocket;

  private final Map<String, String> playerMatchMap;
  private final Map<String, RemoteView> matchesMap;
  private final Map<String, GameController> matchesControllersMap;

  public ServerContext(BlockingQueue<InboxEntry> inbox, BlockingQueue<OutboxEntry> outboxRmi, BlockingQueue<OutboxEntry> outboxSocket) {
    this.inbox = inbox;
    this.outboxRmi = outboxRmi;
    this.outboxSocket = outboxSocket;
    this.playerMatchMap = new HashMap<>();
    this.matchesMap = new HashMap<>();
    this.matchesControllersMap = new HashMap<>();
  }

  public void broadcastToMatch(String matchId, OutboxMessage message) {
    CollectionUtils
      .keys(this.playerMatchMap, matchId)
      .forEach(p -> send(p, message));
  }

  public void enqueueInboxMessage(String matchId, InboxMessage message) {
    CollectionUtils.keys(playerMatchMap, matchId).findFirst().ifPresent(aRandomGuyInThisMatchConnectionId  -> {
      this.inbox.offer(new InboxEntry(aRandomGuyInThisMatchConnectionId, message));
    }); // If nobody in this match, why enqueue stuff?
  }

  public void send(String connectionId, OutboxMessage message) {
    OutboxEntry entry = new OutboxEntry(connectionId, message);
    this.outboxRmi.offer(entry);
    this.outboxSocket.offer(entry);
  }

  public void addConnectionToMatch(String connectionId, String matchId) {
    LOG.info("Registered player " + connectionId + " to match " + matchId);
    this.playerMatchMap.put(connectionId, matchId);
  }

  public void addMatch(RemoteView remoteView, GameController controller) {
    this.matchesMap.put(remoteView.getMatchId(), remoteView);
    this.matchesControllersMap.put(remoteView.getMatchId(), controller);
  }

  public void removeMatch(String matchId) {
    this.matchesMap.remove(matchId);
    this.matchesControllersMap.remove(matchId);
  }

  public void onDisconnection(String connectionId) {
    String matchId = playerMatchMap.remove(connectionId);
    if (matchId != null) {
      this.matchesMap.get(matchId).notifyDisconnectedPlayer(connectionId);
    }
  }

  public Map<String, RemoteView> getMatches() {
    return matchesMap;
  }

  public Map<String, GameController> getMatchesControllersMap() {
    return matchesControllersMap;
  }

  public RemoteView getConnectionMatch(String connectionId) {
    String matchId = playerMatchMap.get(connectionId);
    if (matchId != null)
      return matchesMap.get(matchId);
    return null;
  }
}
