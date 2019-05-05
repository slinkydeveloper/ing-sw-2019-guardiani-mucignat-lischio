package com.adrenalinici.adrenaline.network.server;

import com.adrenalinici.adrenaline.network.inbox.InboxEntry;
import com.adrenalinici.adrenaline.network.outbox.OutboxMessage;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public abstract class ServerNetworkAdapter {

  protected BlockingQueue<InboxEntry> viewInbox;
  protected BlockingQueue<OutboxMessage> viewOutbox;

  public ServerNetworkAdapter(BlockingQueue<InboxEntry> viewInbox, BlockingQueue<OutboxMessage> viewOutbox) {
    this.viewInbox = viewInbox;
    this.viewOutbox = viewOutbox;
  }

  public abstract void start() throws IOException;

  public abstract void stop() throws IOException;
}
