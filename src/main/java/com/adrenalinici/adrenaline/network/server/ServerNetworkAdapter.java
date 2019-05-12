package com.adrenalinici.adrenaline.network.server;

import com.adrenalinici.adrenaline.network.inbox.InboxEntry;
import com.adrenalinici.adrenaline.network.outbox.OutboxMessage;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public abstract class ServerNetworkAdapter {

  protected BlockingQueue<InboxEntry> viewInbox;
  protected BlockingQueue<OutboxMessage> viewOutbox;
  protected String gameId;

  public ServerNetworkAdapter(BlockingQueue<InboxEntry> viewInbox, BlockingQueue<OutboxMessage> viewOutbox, String gameId) {
    this.viewInbox = viewInbox;
    this.viewOutbox = viewOutbox;
    this.gameId = gameId;
  }

  public abstract void start() throws IOException;

  public abstract void stop() throws IOException;
}
