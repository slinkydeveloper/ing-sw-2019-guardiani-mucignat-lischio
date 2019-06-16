package com.adrenalinici.adrenaline.common.network.inbox;

public class InboxEntry {

  private String connectionId;
  private InboxMessage message;

  public InboxEntry(String connectionId, InboxMessage message) {
    this.connectionId = connectionId;
    this.message = message;
  }

  public String getConnectionId() {
    return connectionId;
  }

  public InboxMessage getMessage() {
    return message;
  }
}
