package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.*;
import com.adrenalinici.adrenaline.model.common.*;

import java.util.*;

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
