package com.adrenalinici.adrenaline.server;

import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.fat.PlayerDashboard;

import java.util.Arrays;
import java.util.List;

public enum PlayersChoice {
  THREE {
    @Override
    public List<PlayerDashboard> generate() {
      return Arrays.asList(
        new PlayerDashboard(PlayerColor.GREEN, true),
        new PlayerDashboard(PlayerColor.YELLOW, false),
        new PlayerDashboard(PlayerColor.PURPLE, false)
      );
    }
  },
  FOUR {
    @Override
    public List<PlayerDashboard> generate() {
      return Arrays.asList(
        new PlayerDashboard(PlayerColor.GREEN, true),
        new PlayerDashboard(PlayerColor.YELLOW, false),
        new PlayerDashboard(PlayerColor.PURPLE, false),
        new PlayerDashboard(PlayerColor.GRAY, false)
      );
    }
  },
  FIVE {
    @Override
    public List<PlayerDashboard> generate() {
      return Arrays.asList(
        new PlayerDashboard(PlayerColor.GREEN, true),
        new PlayerDashboard(PlayerColor.YELLOW, false),
        new PlayerDashboard(PlayerColor.PURPLE, false),
        new PlayerDashboard(PlayerColor.GRAY, false),
        new PlayerDashboard(PlayerColor.CYAN, false)
      );
    }
  };

  public abstract List<PlayerDashboard> generate();

}
