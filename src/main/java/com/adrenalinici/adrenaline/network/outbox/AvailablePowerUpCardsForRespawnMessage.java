package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.*;
import com.adrenalinici.adrenaline.model.common.*;

import java.util.*;

import java.util.function.Consumer;

public class AvailablePowerUpCardsForRespawnMessage implements OutboxMessage {

  private PlayerColor player;
  private List<PowerUpCard> powerUpCards;

  public AvailablePowerUpCardsForRespawnMessage(PlayerColor player, List<PowerUpCard> powerUpCards) {
    this.player = player;
    this.powerUpCards = powerUpCards;
  }

  public PlayerColor getPlayer() { return this.player; }

  public List<PowerUpCard> getPowerUpCards() { return this.powerUpCards; }

  @Override
  public void onAvailablePowerUpCardsForRespawnMessage(Consumer<AvailablePowerUpCardsForRespawnMessage> c) { c.accept(this); }

}
