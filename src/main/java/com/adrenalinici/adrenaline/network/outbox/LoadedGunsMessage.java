package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.*;

import java.util.*;

import java.util.function.Consumer;

public class LoadedGunsMessage implements OutboxMessage {

  private List<Gun> guns;

  public LoadedGunsMessage(List<Gun> guns) {
    this.guns = guns;
  }

  public List<Gun> getGuns() { return this.guns; }

  @Override
  public void onLoadedGunsMessage(Consumer<LoadedGunsMessage> c) { c.accept(this); }

}
