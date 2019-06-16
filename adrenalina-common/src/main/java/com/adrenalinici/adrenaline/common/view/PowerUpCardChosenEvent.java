package com.adrenalinici.adrenaline.common.view;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.PowerUpCard;

import java.util.function.Consumer;

public class PowerUpCardChosenEvent implements ViewEvent {

  private final PlayerColor player;
  private final PowerUpCard card;

  public PowerUpCardChosenEvent(PlayerColor player, PowerUpCard card) {
    this.player = player;
    this.card = card;
  }

  public PlayerColor getPlayer() {
    return player;
  }

  public PowerUpCard getCard() {
    return card;
  }

  @Override
  public void onPowerUpChosenEvent(Consumer<PowerUpCardChosenEvent> consumer) {
    consumer.accept(this);
  }
}
