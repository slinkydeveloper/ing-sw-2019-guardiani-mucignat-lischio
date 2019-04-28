package com.adrenalinici.adrenaline.model.event;

import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.model.PlayerDashboard;

public class PlayerDashboardUpdatedEvent extends BaseModelEvent {

  PlayerDashboard playerDashboard;

  public PlayerDashboardUpdatedEvent(GameModel gameModel, PlayerDashboard playerDashboard) {
    super(gameModel);
    this.playerDashboard = playerDashboard;
  }

  public PlayerDashboard getPlayerDashboard() {
    return playerDashboard;
  }
}
