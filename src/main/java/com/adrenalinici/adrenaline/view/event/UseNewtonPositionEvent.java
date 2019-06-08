package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.Position;

import java.util.function.Consumer;

public class UseNewtonPositionEvent implements ViewEvent {

  private Position chosenPosition;
  private PlayerColor player;

  public UseNewtonPositionEvent(Position chosenPosition, PlayerColor player) {
    this.chosenPosition = chosenPosition;
    this.player = player;
  }

  public Position getChosenPosition() {
    return chosenPosition;
  }

  public PlayerColor getPlayer() {
    return player;
  }

  @Override
  public void onUseNewtonPositionEvent(Consumer<UseNewtonPositionEvent> consumer) {
    consumer.accept(this);
  }
}
