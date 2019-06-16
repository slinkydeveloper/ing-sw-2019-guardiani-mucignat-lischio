package com.adrenalinici.adrenaline.common.network.outbox;

import java.util.Set;
import java.util.function.Consumer;

public class AvailableGunsToPickupMessage implements OutboxMessage {

  private Set<String> guns;

  public AvailableGunsToPickupMessage(Set<String> guns) {
    this.guns = guns;
  }

  public Set<String> getGuns() { return this.guns; }

  @Override
  public void onAvailableGunsToPickupMessage(Consumer<AvailableGunsToPickupMessage> c) { c.accept(this); }

}
