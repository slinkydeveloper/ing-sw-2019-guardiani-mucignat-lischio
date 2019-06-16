package com.adrenalinici.adrenaline.server.network;

import com.adrenalinici.adrenaline.common.network.inbox.InboxEntry;
import com.adrenalinici.adrenaline.common.network.outbox.OutboxEntry;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public abstract class ServerNetworkAdapter {

  protected BlockingQueue<InboxEntry> viewInbox;
  protected BlockingQueue<OutboxEntry> viewOutbox;

  public ServerNetworkAdapter(BlockingQueue<InboxEntry> viewInbox, BlockingQueue<OutboxEntry> viewOutbox) {
    this.viewInbox = viewInbox;
    this.viewOutbox = viewOutbox;
  }

  public abstract void start() throws IOException;

  public abstract void stop() throws IOException;
}
