package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.model.Position;
import com.adrenalinici.adrenaline.view.GameView;

import java.util.function.Consumer;

public class MovementChosenEvent extends BaseViewEvent {

  Position coordinates;

  public MovementChosenEvent(GameView view, Position coordinates) {
    super(view);
    this.coordinates = coordinates;
  }

  public Position getCoordinates() {
    return coordinates;
  }

  @Override
  public void onActionChosenEvent(Consumer<ActionChosenEvent> consumer) {
  }

  @Override
  public void onMovementChosenEvent(Consumer<MovementChosenEvent> consumer) {
    consumer.accept(this);
  }

  @Override
  public void onNewTurnEvent(Consumer<NewTurnEvent> consumer) {
  }
}
