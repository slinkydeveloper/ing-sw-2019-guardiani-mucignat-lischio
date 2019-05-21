package com.adrenalinici.adrenaline.network.outbox;

public class OutboxEntry {

  private String connectionId;
  private OutboxMessage message;

  public OutboxEntry(String connectionId, OutboxMessage message) {
    this.connectionId = connectionId;
    this.message = message;
  }

  public String getConnectionId() {
    return connectionId;
  }

  public OutboxMessage getMessage() {
    return message;
  }
}
