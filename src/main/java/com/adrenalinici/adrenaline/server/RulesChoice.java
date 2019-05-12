package com.adrenalinici.adrenaline.server;

import com.adrenalinici.adrenaline.model.fat.Dashboard;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.model.fat.PlayerDashboard;

import java.util.List;

public enum RulesChoice {
  SIMPLE {
    @Override
    public GameModel generate(Dashboard dashboard, List<PlayerDashboard> playerDashboards) {
      return new GameModel(5, dashboard, playerDashboards);
    }
  },
  COMPLETE {
    @Override
    public GameModel generate(Dashboard dashboard, List<PlayerDashboard> playerDashboards) {
      return new GameModel(8, dashboard, playerDashboards);
    }
  };

  public abstract GameModel generate(Dashboard dashboard, List<PlayerDashboard> playerDashboards);

}
