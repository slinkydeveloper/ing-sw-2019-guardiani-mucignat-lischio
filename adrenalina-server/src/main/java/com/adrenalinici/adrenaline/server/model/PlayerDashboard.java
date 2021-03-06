package com.adrenalinici.adrenaline.server.model;

import com.adrenalinici.adrenaline.common.model.AmmoColor;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.PowerUpCard;
import com.adrenalinici.adrenaline.common.model.PowerUpType;
import com.adrenalinici.adrenaline.common.model.light.LightPlayerDashboard;
import com.adrenalinici.adrenaline.common.util.Bag;
import com.adrenalinici.adrenaline.common.util.CollectionUtils;
import com.adrenalinici.adrenaline.server.controller.GunLoader;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerDashboard {

  private PlayerColor player;
  private List<AmmoColor> ammos;
  private List<PlayerColor> damages;
  private List<PlayerColor> marks;
  private HashMap<String, Boolean> guns;
  private List<PowerUpCard> powerUpCards;
  private int skullsNumber;
  private int points;
  private boolean flipped;

  public PlayerDashboard(PlayerColor player) {
    this.player = player;
    this.powerUpCards = new ArrayList<>();
    this.ammos = new ArrayList<>();
    this.damages = new ArrayList<>();
    this.marks = new ArrayList<>();
    this.guns = new HashMap<>();
    this.skullsNumber = 0;
    this.points = 0;
    this.flipped = false;
    addAmmo(AmmoColor.RED);
    addAmmo(AmmoColor.BLUE);
    addAmmo(AmmoColor.YELLOW);
  }

  public PlayerDashboard(PlayerColor player, List<PowerUpCard> powerUpCards) {
    this.player = player;
    this.powerUpCards = new ArrayList<>(powerUpCards);
    this.ammos = new ArrayList<>();
    this.damages = new ArrayList<>();
    this.marks = new ArrayList<>();
    this.guns = new HashMap<>();
    this.skullsNumber = 0;
    this.points = 0;
    addAmmo(AmmoColor.RED);
    addAmmo(AmmoColor.BLUE);
    addAmmo(AmmoColor.YELLOW);
  }

  public PlayerColor getPlayer() {
    return player;
  }

  public void addAmmo(AmmoColor ammo) {
    if (ammos.stream().filter(a -> a.equals(ammo)).count() < 3) ammos.add(ammo);
  }

  public void removeAmmos(List<AmmoColor> ammos) {
    CollectionUtils.difference(this.ammos, ammos);
  }

  public List<AmmoColor> getAmmos() {
    return ammos;
  }

  public void addDamages(List<PlayerColor> damages) {
    this.damages.addAll(damages);
    if (this.damages.size() > 12) {
      this.damages = this.damages.subList(0, 12);
    }
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
    this.marks = Bag.sum(this.marks, marks).toList();
  }

  public void removeMarks(List<PlayerColor> marks) {
    this.marks = Bag.difference(this.marks, marks).toList();
  }

  public List<PlayerColor> getMarks() {
    return marks;
  }

  /**
   * Add a loaded gun
   *
   * @param gun
   */
  public void addGun(String gun) {
    if (guns.size() == 3) {
      guns
        .entrySet()
        .stream()
        .min((e1, e2) -> (e1.getValue().equals(e2.getValue())) ? 0 : !e1.getValue() ? -1 : 1)
        .ifPresent(e -> guns.remove(e.getKey())); // I remove the first unloaded gun
    }
    guns.put(gun, true);
  }

  /**
   * Unload a gun already owned by this player
   *
   * @param gun
   */
  public void unloadGun(String gun) {
    guns.replace(gun, false);
  }

  /**
   * Load a gun already owned by this player
   *
   * @param gun
   */
  public void reloadGun(String gun) {
    guns.replace(gun, true);
  }

  /**
   * Remove a gun owned by this player
   *
   * @param gun
   */
  public void removeGun(String gun) {
    guns.remove(gun);
  }

  public Set<String> getLoadedGuns() {
    return guns
      .entrySet()
      .stream()
      .filter(Map.Entry::getValue)
      .map(Map.Entry::getKey)
      .collect(Collectors.toSet());
  }

  public Set<String> getUnloadedGuns() {
    return guns
      .entrySet()
      .stream()
      .filter(e -> !e.getValue())
      .map(Map.Entry::getKey)
      .collect(Collectors.toSet());
  }

  public boolean addPowerUpCard(PowerUpCard powerUp) {
    if (powerUpCards.size() < 3) {
      powerUpCards.add(powerUp);
      return true;
    }
    return false;
  }

  public boolean removePowerUpCard(PowerUpCard powerUp) {
    return powerUpCards.remove(powerUp);
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

  /**
   * Checks if dashboard is flipped, so point scheme for frenzy mode should be used
   *
   * @return
   */
  public boolean isFlipped() {
    return flipped;
  }

  public PlayerDashboard setFlipped(boolean flipped) {
    this.flipped = flipped;
    return this;
  }

  public Bag<AmmoColor> getAllAmmosIncludingPowerups() {
    List<AmmoColor> playerAmmos = new ArrayList<>(getAmmos());
    getPowerUpCards().stream().forEach(powerUpCard -> playerAmmos.add(powerUpCard.getAmmoColor()));
    return Bag.from(playerAmmos);
  }


  /**
   * Removes the specified list of AmmoColor from PlayerDashboard.
   * In case PlayerDashboard does not contain all of the ammos in the list,
   * it calculates the missing ammos and removes the first PowerupCard of the same color
   * for each one
   *
   * @param ammos
   */
  public List<PowerUpCard> removeAmmosIncludingPowerups(List<AmmoColor> ammos) {
    List<AmmoColor> ammosToGetFromPowerUp = CollectionUtils.differencePure(ammos, getAmmos());

    List<AmmoColor> ammosToRemove = CollectionUtils.differencePure(ammos, ammosToGetFromPowerUp);
    removeAmmos(ammosToRemove);

    List<PowerUpCard> powerUpsToRemove = new ArrayList<>();

    if (!ammosToGetFromPowerUp.isEmpty()) {
      ammosToGetFromPowerUp.forEach(ammo -> {
        PowerUpCard toRemove = getPowerUpCards().stream().filter(
          powerUpCard -> powerUpCard.getAmmoColor().equals(ammo)
        ).findFirst().get();

        removePowerUpCard(toRemove);
        powerUpsToRemove.add(toRemove);
      });
    }
    return powerUpsToRemove;
  }

  public boolean hasVenomGrenade() {
    return powerUpCards.stream().anyMatch(p -> p.getPowerUpType().equals(PowerUpType.TAGBACK_GRENADE));
  }

  /**
   * @return LightPlayerDashboard based on the fat one
   */
  public LightPlayerDashboard light() {
    return new LightPlayerDashboard(
      player,
      new ArrayList<>(ammos),
      new ArrayList<>(damages),
      new ArrayList<>(marks),
      new HashSet<>(getLoadedGuns().stream().map(GunLoader.INSTANCE::getModelGun).collect(Collectors.toSet())),
      new HashSet<>(getUnloadedGuns().stream().map(GunLoader.INSTANCE::getModelGun).collect(Collectors.toSet())),
      new ArrayList<>(powerUpCards),
      skullsNumber,
      points,
      this.flipped
    );
  }
}
