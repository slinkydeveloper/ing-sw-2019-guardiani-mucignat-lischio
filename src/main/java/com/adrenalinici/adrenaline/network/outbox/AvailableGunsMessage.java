package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.*;

import java.util.*;

import java.util.function.Consumer;

public class AvailableGunsMessage implements OutboxMessage {

  private List<Gun> guns;

  public AvailableGunsMessage(List<Gun> guns) {
    this.guns = guns;
  }

  public List<Gun> getGuns() { return this.guns; }

  @Override
  public void onAvailableGunsMessage(Consumer<AvailableGunsMessage> c) { c.accept(this); }

}
