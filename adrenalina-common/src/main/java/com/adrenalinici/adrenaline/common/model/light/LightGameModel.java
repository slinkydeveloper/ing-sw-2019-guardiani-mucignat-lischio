package com.adrenalinici.adrenaline.common.model.light;

import com.adrenalinici.adrenaline.common.model.PlayerColor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class LightGameModel implements Serializable {

  private List<Map.Entry<PlayerColor, Boolean>> killScore;
  private int remainingSkulls;
  private List<PlayerColor> doubleKillScore;
  private LightDashboard dashboard;
  private List<LightPlayerDashboard> playerDashboards;

  public LightGameModel(List<Map.Entry<PlayerColor, Boolean>> killScore, int remainingSkulls, List<PlayerColor> doubleKillScore, LightDashboard dashboard, List<LightPlayerDashboard> playerDashboards) {
    this.killScore = killScore;
    this.remainingSkulls = remainingSkulls;
    this.doubleKillScore = doubleKillScore;
    this.dashboard = dashboard;
    this.playerDashboards = playerDashboards;
  }

  public List<Map.Entry<PlayerColor, Boolean>> getKillScore() {
    return killScore;
  }

  public int getRemainingSkulls() {
    return remainingSkulls;
  }

  public List<PlayerColor> getDoubleKillScore() {
    return doubleKillScore;
  }

  public LightDashboard getDashboard() {
    return dashboard;
  }

  public List<LightPlayerDashboard> getPlayerDashboards() {
    return playerDashboards;
  }

  public LightPlayerDashboard getPlayerDashboard(PlayerColor player) {
    return getPlayerDashboards().stream().filter(d -> player.equals(d.getPlayer())).findFirst().orElseThrow(() -> new IllegalStateException("Player not present " + player));
  }

  @Override
  public String toString() {
    return "LightGameModel{" +
      "killScore=" + killScore +
      ", remainingSkulls=" + remainingSkulls +
      ", doubleKillScore=" + doubleKillScore +
      ", dashboard=" + dashboard +
      ", playerDashboards=" + playerDashboards +
      '}';
  }
}
