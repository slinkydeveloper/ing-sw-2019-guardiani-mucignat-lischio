package com.adrenalinici.adrenaline.common.network.outbox;

import java.util.Set;
import java.util.function.Consumer;

public class AvailableGunsMessage implements OutboxMessage {

  private Set<String> guns;

  public AvailableGunsMessage(Set<String> guns) {
    this.guns = guns;
  }

  public Set<String> getGuns() { return this.guns; }

  @Override
  public void onAvailableGunsMessage(Consumer<AvailableGunsMessage> c) { c.accept(this); }

}
