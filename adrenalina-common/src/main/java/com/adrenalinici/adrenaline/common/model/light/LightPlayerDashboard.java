package com.adrenalinici.adrenaline.common.model.light;

import com.adrenalinici.adrenaline.common.model.AmmoColor;
import com.adrenalinici.adrenaline.common.model.Gun;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.PowerUpCard;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class LightPlayerDashboard implements Serializable {

  private PlayerColor player;
  private List<AmmoColor> ammos;
  private List<PlayerColor> damages;
  private List<PlayerColor> marks;
  private Set<Gun> loadedGuns;
  private Set<Gun> unloadedGuns;
  private List<PowerUpCard> powerUpCards;
  private int skullsNumber;
  private int points;
  private boolean flipped;

  public LightPlayerDashboard(PlayerColor player, List<AmmoColor> ammos, List<PlayerColor> damages, List<PlayerColor> marks, Set<Gun> loadedGuns, Set<Gun> unloadedGuns, List<PowerUpCard> powerUpCards, int skullsNumber, int points, boolean flipped) {
    this.player = player;
    this.ammos = ammos;
    this.damages = damages;
    this.marks = marks;
    this.loadedGuns = loadedGuns;
    this.unloadedGuns = unloadedGuns;
    this.powerUpCards = powerUpCards;
    this.skullsNumber = skullsNumber;
    this.points = points;
    this.flipped = flipped;
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

  public boolean isFlipped() {
    return flipped;
  }

  @Override
  public String toString() {
    return "LightPlayerDashboard{" +
      "player=" + player +
      ", ammos=" + ammos +
      ", damages=" + damages +
      ", marks=" + marks +
      ", loadedGuns=" + loadedGuns +
      ", unloadedGuns=" + unloadedGuns +
      ", powerUpCards=" + powerUpCards +
      ", skullsNumber=" + skullsNumber +
      ", points=" + points +
      '}';
  }
}
