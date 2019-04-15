package com.adrenalinici.adrenaline.model.event;

import com.adrenalinici.adrenaline.model.GameStatus;
import com.adrenalinici.adrenaline.model.PlayerDashboard;

public class PlayerDashboardUpdatedEvent extends BaseModelEvent {

  PlayerDashboard playerDashboard;

  public PlayerDashboardUpdatedEvent(GameStatus gameStatus, PlayerDashboard playerDashboard) {
    super(gameStatus);
    this.playerDashboard = playerDashboard;
  }

  public PlayerDashboard getPlayerDashboard() {
    return playerDashboard;
  }
}
