package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.*;

import java.util.*;

import java.util.function.Consumer;

public class AvailableRespawnLocationsMessage implements OutboxMessage {

  private List<AmmoColor> respawnLocations;

  public AvailableRespawnLocationsMessage(List<AmmoColor> respawnLocations) {
    this.respawnLocations = respawnLocations;
  }

  public List<AmmoColor> getRespawnLocations() { return this.respawnLocations; }

  @Override
  public void onAvailableRespawnLocationsMessage(Consumer<AvailableRespawnLocationsMessage> c) { c.accept(this); }

}
