package com.adrenalinici.adrenaline.common.view;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.model.PowerUpCard;

import java.util.Objects;
import java.util.function.Consumer;

public class UseNewtonEvent implements ViewEvent {

  private final PowerUpCard chosenCard;
  private final Position chosenPosition;
  private final PlayerColor player;

  public UseNewtonEvent(PowerUpCard chosenCard, Position chosenPosition, PlayerColor player) {
    this.chosenCard = chosenCard;
    this.chosenPosition = chosenPosition;
    this.player = player;
  }

  public PowerUpCard getChosenCard() {
    return chosenCard;
  }

  public Position getChosenPosition() {
    return chosenPosition;
  }

  public PlayerColor getPlayer() {
    return player;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UseNewtonEvent that = (UseNewtonEvent) o;
    return Objects.equals(chosenCard, that.chosenCard) &&
      Objects.equals(chosenPosition, that.chosenPosition) &&
      player == that.player;
  }

  @Override
  public int hashCode() {
    return Objects.hash(chosenCard, chosenPosition, player);
  }

  @Override
  public void onUseNewtonEvent(Consumer<UseNewtonEvent> consumer) {
    consumer.accept(this);
  }
}
