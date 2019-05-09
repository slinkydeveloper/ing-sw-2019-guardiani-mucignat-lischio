package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.model.common.Position;

import java.util.Objects;
import java.util.function.Consumer;

public class MovementChosenEvent implements ViewEvent {

  Position coordinates;

  public MovementChosenEvent(Position coordinates) {
    this.coordinates = coordinates;
  }

  public Position getCoordinates() {
    return coordinates;
  }

  @Override
  public void onMovementChosenEvent(Consumer<MovementChosenEvent> consumer) {
    consumer.accept(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MovementChosenEvent that = (MovementChosenEvent) o;
    return Objects.equals(coordinates, that.coordinates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coordinates);
  }
}
