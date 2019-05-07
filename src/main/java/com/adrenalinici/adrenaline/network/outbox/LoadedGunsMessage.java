package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.*;

import java.util.*;

import java.util.function.Consumer;

public class LoadedGunsMessage implements OutboxMessage {

  private Set<String> guns;

  public LoadedGunsMessage(Set<String> guns) {
    this.guns = guns;
  }

  public Set<String> getGuns() { return this.guns; }

  @Override
  public void onLoadedGunsMessage(Consumer<LoadedGunsMessage> c) { c.accept(this); }

}
