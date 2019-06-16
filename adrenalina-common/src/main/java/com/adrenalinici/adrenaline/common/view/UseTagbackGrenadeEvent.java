package com.adrenalinici.adrenaline.common.view;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.PowerUpCard;

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
}
