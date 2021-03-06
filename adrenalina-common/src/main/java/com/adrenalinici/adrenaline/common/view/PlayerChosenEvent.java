package com.adrenalinici.adrenaline.common.view;

import com.adrenalinici.adrenaline.common.model.PlayerColor;

import java.util.Objects;
import java.util.function.Consumer;

public class PlayerChosenEvent implements ViewEvent {

  private PlayerColor playerColor;

  public PlayerChosenEvent(PlayerColor playerColor) {
    this.playerColor = playerColor;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  @Override
  public void onPlayerChosenEvent(Consumer<PlayerChosenEvent> consumer) {
    consumer.accept(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PlayerChosenEvent that = (PlayerChosenEvent) o;
    return playerColor == that.playerColor;
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerColor);
  }
}
