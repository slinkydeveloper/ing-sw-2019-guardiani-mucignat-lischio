package com.adrenalinici.adrenaline.model.event;

import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.model.common.PlayerColor;

public class PlayerDashboardUpdatedEvent extends BaseModelEvent {

  private PlayerColor playerColor;

  public PlayerDashboardUpdatedEvent(GameModel gameModel, PlayerColor playerColor) {
    super(gameModel);
    this.playerColor = playerColor;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }
}
