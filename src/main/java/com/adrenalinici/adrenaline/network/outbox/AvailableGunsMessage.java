package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.*;

import java.util.*;

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
