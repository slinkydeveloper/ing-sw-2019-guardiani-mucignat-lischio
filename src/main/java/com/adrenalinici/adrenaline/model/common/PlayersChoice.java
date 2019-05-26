package com.adrenalinici.adrenaline.model.common;

import com.adrenalinici.adrenaline.model.fat.PlayerDashboard;

import java.util.Arrays;
import java.util.List;

public enum PlayersChoice {
  THREE {
    @Override
    public List<PlayerDashboard> generate() {
      return Arrays.asList(
        new PlayerDashboard(PlayerColor.GREEN),
        new PlayerDashboard(PlayerColor.YELLOW),
        new PlayerDashboard(PlayerColor.PURPLE)
      );
    }
  },
  FOUR {
    @Override
    public List<PlayerDashboard> generate() {
      return Arrays.asList(
        new PlayerDashboard(PlayerColor.GREEN),
        new PlayerDashboard(PlayerColor.YELLOW),
        new PlayerDashboard(PlayerColor.PURPLE),
        new PlayerDashboard(PlayerColor.GRAY)
      );
    }
  },
  FIVE {
    @Override
    public List<PlayerDashboard> generate() {
      return Arrays.asList(
        new PlayerDashboard(PlayerColor.GREEN),
        new PlayerDashboard(PlayerColor.YELLOW),
        new PlayerDashboard(PlayerColor.PURPLE),
        new PlayerDashboard(PlayerColor.GRAY),
        new PlayerDashboard(PlayerColor.CYAN)
      );
    }
  };

  public abstract List<PlayerDashboard> generate();

}
