package com.adrenalinici.adrenaline.common.view;

import com.adrenalinici.adrenaline.common.model.CellColor;

import java.util.Objects;
import java.util.function.Consumer;

public class RoomChosenEvent implements ViewEvent {
  private CellColor roomColor;

  public RoomChosenEvent(CellColor roomColor) {
    this.roomColor = roomColor;
  }

  public CellColor getRoomColor() {
    return roomColor;
  }

  @Override
  public void onRoomChosenEvent(Consumer<RoomChosenEvent> consumer) {
    consumer.accept(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RoomChosenEvent that = (RoomChosenEvent) o;
    return roomColor == that.roomColor;
  }

  @Override
  public int hashCode() {
    return Objects.hash(roomColor);
  }
}
