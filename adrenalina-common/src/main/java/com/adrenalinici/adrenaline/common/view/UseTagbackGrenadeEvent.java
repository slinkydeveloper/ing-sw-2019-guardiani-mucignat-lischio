package com.adrenalinici.adrenaline.common.view;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.PowerUpCard;

import java.util.Objects;
import java.util.function.Consumer;

public class UseTagbackGrenadeEvent implements ViewEvent {

  private PlayerColor playerColor;
  private PowerUpCard chosenCard;

  public UseTagbackGrenadeEvent(PlayerColor playerColor, PowerUpCard chosenCard) {
    this.playerColor = playerColor;
    this.chosenCard = chosenCard;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public PowerUpCard getChosenCard() {
    return chosenCard;
  }

  @Override
  public void onUseVenomGrenadeEvent(Consumer<UseTagbackGrenadeEvent> consumer) {
    consumer.accept(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UseTagbackGrenadeEvent that = (UseTagbackGrenadeEvent) o;
    return playerColor == that.playerColor &&
      Objects.equals(chosenCard, that.chosenCard);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerColor, chosenCard);
  }
}
