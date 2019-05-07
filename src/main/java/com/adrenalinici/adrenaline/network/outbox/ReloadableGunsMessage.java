package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.*;

import java.util.*;

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
