package com.adrenalinici.adrenaline.common.network.outbox;

import java.util.Set;
import java.util.function.Consumer;

public class ReloadableGunsMessage implements OutboxMessage {

  private Set<String> guns;

  public ReloadableGunsMessage(Set<String> guns) {
    this.guns = guns;
  }

  public Set<String> getGuns() { return this.guns; }

  @Override
  public void onReloadableGunsMessage(Consumer<ReloadableGunsMessage> c) { c.accept(this); }

}
