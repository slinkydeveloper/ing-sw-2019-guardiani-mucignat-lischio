package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.common.CellColor;

import java.util.Set;
import java.util.function.Consumer;

public class AvailableRoomsMessage implements OutboxMessage {

  private Set<CellColor> rooms;

  public AvailableRoomsMessage(Set<CellColor> rooms) {
    this.rooms = rooms;
  }

  public Set<CellColor> getRooms() {
    return this.rooms;
  }

  @Override
  public void onAvailableRoomsMessage(Consumer<AvailableRoomsMessage> c) {
    c.accept(this);
  }

}
