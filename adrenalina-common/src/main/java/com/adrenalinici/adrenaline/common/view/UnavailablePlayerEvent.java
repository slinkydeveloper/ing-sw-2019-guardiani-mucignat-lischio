package com.adrenalinici.adrenaline.common.view;

import com.adrenalinici.adrenaline.common.model.PlayerColor;

public class UnavailablePlayerEvent implements ViewEvent {

  private final PlayerColor playerColor;

  public UnavailablePlayerEvent(PlayerColor playerColor) {
    this.playerColor = playerColor;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  @Override
  public boolean isUnavailablePlayerEvent() {
    return true;
  }
}
