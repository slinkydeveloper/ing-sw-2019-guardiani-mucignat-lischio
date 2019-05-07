package com.adrenalinici.adrenaline.model.event;

import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.model.PlayerDashboard;

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
