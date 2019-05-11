package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.PowerUpCard;

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
