package com.adrenalinici.adrenaline.common.model.event;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.light.LightGameModel;

public class PlayerDashboardUpdatedEvent extends BaseModelEvent {

  private PlayerColor playerColor;

  public PlayerDashboardUpdatedEvent(LightGameModel gameModel, PlayerColor playerColor) {
    super(gameModel);
    this.playerColor = playerColor;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }
}
