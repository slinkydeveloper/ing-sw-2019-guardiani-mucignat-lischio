package com.adrenalinici.adrenaline.network.inbox;

import com.adrenalinici.adrenaline.model.common.PlayerColor;

import java.util.function.Consumer;

public class ChosenMatchMessage implements InboxMessage {

  private String matchId;
  private PlayerColor color;

  public ChosenMatchMessage(String matchId, PlayerColor color) {
    this.matchId = matchId;
    this.color = color;
  }

  public String getMatchId() {
    return matchId;
  }

  public PlayerColor getColor() {
    return color;
  }

  @Override
  public void onChosenMatchMessage(Consumer<ChosenMatchMessage> consumer) {
    consumer.accept(this);
  }
}
