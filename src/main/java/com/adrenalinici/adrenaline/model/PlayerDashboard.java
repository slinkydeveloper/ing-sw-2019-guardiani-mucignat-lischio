package com.adrenalinici.adrenaline.model;

import com.adrenalinici.adrenaline.util.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class PlayerDashboard {
  private PlayerColor player;
  private List<AmmoColor> ammos;
  private List<PlayerColor> damages;
  private List<PlayerColor> marks;
  private List<Gun> loadedGuns;
  private List<Gun> unloadedGuns;
  private List<PowerUpCard> powerUpCards;
  private int skullsNumber;
  private int points;
  private boolean firstPlayer;

  public PlayerDashboard(PlayerColor player, boolean firstPlayer, List<PowerUpCard> powerUpCards) {
    this.player = player;
    this.firstPlayer = firstPlayer;
    this.powerUpCards = new ArrayList<>(powerUpCards);
    this.ammos = new ArrayList<>();
    this.damages = new ArrayList<>();
    this.marks = new ArrayList<>();
    this.loadedGuns = new ArrayList<>();
    this.unloadedGuns = new ArrayList<>();
    this.skullsNumber = 0;
    this.points = 0;
    addAmmo(AmmoColor.RED);
    addAmmo(AmmoColor.BLUE);
    addAmmo(AmmoColor.YELLOW);
  }

  public PlayerColor getPlayer() {
    return player;
  }

  public void addAmmo(AmmoColor ammo) throws IllegalStateException {
    int countAmmo = 0;
    for (AmmoColor a : ammos) {
      if (a.equals(ammo)) {
        countAmmo++;
      }
    }
    if (countAmmo == 3) {
      throw new IllegalStateException("I have plus than 3 ammos of the same colors");
    } else {
      ammos.add(ammo);
    }
  }

  public void removeAmmos(List<AmmoColor> ammos) {
    ListUtils.difference(this.ammos, ammos);
  }

  public List<AmmoColor> getAmmos() {
    return ammos;
  }

  public void addDamages(List<PlayerColor> damages) {
    this.damages.addAll(damages);
  }

  public void removeAllDamages() {
    this.damages.clear();
  }

  public List<PlayerColor> getDamages() {
    return damages;
  }

  public Optional<PlayerColor> getFirstDamage() {
    if (damages.size() >= 1)
      return Optional.of(damages.get(0));
    else return Optional.empty();
  }

  public Optional<PlayerColor> getKillDamage() {
    if (damages.size() >= 11)
      return Optional.of(damages.get(10));
    else return Optional.empty();
  }

  public Optional<PlayerColor> getCruelDamage() {
    if (damages.size() >= 12)
      return Optional.of(damages.get(11));
    else return Optional.empty();
  }

  public void addMarks(List<PlayerColor> marks) {
    this.marks.addAll(marks);
  }

  public void removeMarks(List<PlayerColor> marks) {
    ListUtils.difference(this.marks, marks);
  }

  public List<PlayerColor> getMarks() {
    return marks;
  }

  public void addLoadedGun(Gun loadedGun) {
    if (loadedGuns.size() == 3) {
      Random random = new Random();
      loadedGuns.remove(random.nextInt(3));
    }
    loadedGuns.add(loadedGun);
  }

  public void addUnloadedGun(Gun unloadedGun) {
    //TODO gestire caso limite armi
    unloadedGuns.add(unloadedGun);
  }

  public void removeLoadedGun(Gun loadedGun) {
    loadedGuns.remove(loadedGun);
  }

  public List<Gun> getLoadedGuns() {
    return loadedGuns;
  }

  public void removeUnloadedGun(Gun loadedGun) {
    unloadedGuns.remove(loadedGun);
  }

  public List<Gun> getUnloadedGuns() {
    return unloadedGuns;
  }

  public void addPowerUpCard(PowerUpCard powerUp) throws IllegalStateException {
    if (powerUpCards.size() < 3) {
      powerUpCards.add(powerUp);
    } else throw new IllegalStateException("You can't have more than 3 PowerUpCards");
  }

  public void removePowerUpCard(PowerUpCard powerUp) {
    powerUpCards.remove(powerUp);
  }

  public List<PowerUpCard> getPowerUpCards() {
    return powerUpCards;
  }

  public void incrementSkullsNumber() {
    skullsNumber++;
  }

  public int getSkullsNumber() {
    return skullsNumber;
  }

  public void addPoints(int points) {
    this.points += points;
  }

  public int getPoints() {
    return points;
  }

  public boolean isFirstPlayer() {
    return firstPlayer;
  }
}
