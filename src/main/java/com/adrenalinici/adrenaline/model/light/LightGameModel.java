package com.adrenalinici.adrenaline.model.light;

import com.adrenalinici.adrenaline.model.common.PlayerColor;

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
}
