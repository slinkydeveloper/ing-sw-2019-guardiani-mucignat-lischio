package com.adrenalinici.adrenaline.model.common;

import com.adrenalinici.adrenaline.model.fat.Dashboard;
import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.model.fat.PlayerDashboard;

import java.util.List;

public enum RulesChoice {
  SIMPLE {
    @Override
    public GameModel generate(Dashboard dashboard, List<PlayerDashboard> playerDashboards) {
      return new GameModel(5, dashboard, playerDashboards); //TODO frenzy or not
    }
  },
  COMPLETE {
    @Override
    public GameModel generate(Dashboard dashboard, List<PlayerDashboard> playerDashboards) {
      return new GameModel(8, dashboard, playerDashboards); //TODO frenzy or not
    }
  };

  public abstract GameModel generate(Dashboard dashboard, List<PlayerDashboard> playerDashboards);

}
