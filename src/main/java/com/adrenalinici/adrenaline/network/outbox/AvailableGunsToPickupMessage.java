package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.*;

import java.util.*;

import java.util.function.Consumer;

public class AvailableGunsToPickupMessage implements OutboxMessage {

  private List<Gun> guns;

  public AvailableGunsToPickupMessage(List<Gun> guns) {
    this.guns = guns;
  }

  public List<Gun> getGuns() { return this.guns; }

  @Override
  public void onAvailableGunsToPickupMessage(Consumer<AvailableGunsToPickupMessage> c) { c.accept(this); }

}
