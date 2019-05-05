package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.*;

import java.util.*;

import java.util.function.Consumer;

public class ReloadableGunsMessage implements OutboxMessage {

  private List<Gun> guns;

  public ReloadableGunsMessage(List<Gun> guns) {
    this.guns = guns;
  }

  public List<Gun> getGuns() { return this.guns; }

  @Override
  public void onReloadableGunsMessage(Consumer<ReloadableGunsMessage> c) { c.accept(this); }

}
