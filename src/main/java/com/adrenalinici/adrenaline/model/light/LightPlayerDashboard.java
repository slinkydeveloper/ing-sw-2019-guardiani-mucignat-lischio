package com.adrenalinici.adrenaline.model.light;

import com.adrenalinici.adrenaline.model.common.AmmoColor;
import com.adrenalinici.adrenaline.model.common.Gun;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.PowerUpCard;

import java.util.List;
import java.util.Set;

public class LightPlayerDashboard {

  private PlayerColor player;
  private List<AmmoColor> ammos;
  private List<PlayerColor> damages;
  private List<PlayerColor> marks;
  private Set<Gun> loadedGuns;
  private Set<Gun> unloadedGuns;
  private List<PowerUpCard> powerUpCards;
  private int skullsNumber;
  private int points;
  private boolean firstPlayer;

  public LightPlayerDashboard(PlayerColor player, List<AmmoColor> ammos, List<PlayerColor> damages, List<PlayerColor> marks, Set<Gun> loadedGuns, Set<Gun> unloadedGuns, List<PowerUpCard> powerUpCards, int skullsNumber, int points, boolean firstPlayer) {
    this.player = player;
    this.ammos = ammos;
    this.damages = damages;
    this.marks = marks;
    this.loadedGuns = loadedGuns;
    this.unloadedGuns = unloadedGuns;
    this.powerUpCards = powerUpCards;
    this.skullsNumber = skullsNumber;
    this.points = points;
    this.firstPlayer = firstPlayer;
  }

  public PlayerColor getPlayer() {
    return player;
  }

  public List<AmmoColor> getAmmos() {
    return ammos;
  }

  public List<PlayerColor> getDamages() {
    return damages;
  }

  public List<PlayerColor> getMarks() {
    return marks;
  }

  public Set<Gun> getLoadedGuns() {
    return loadedGuns;
  }

  public Set<Gun> getUnloadedGuns() {
    return unloadedGuns;
  }

  public List<PowerUpCard> getPowerUpCards() {
    return powerUpCards;
  }

  public int getSkullsNumber() {
    return skullsNumber;
  }

  public int getPoints() {
    return points;
  }

  public boolean isFirstPlayer() {
    return firstPlayer;
  }
}