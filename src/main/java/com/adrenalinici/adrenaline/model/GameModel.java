package com.adrenalinici.adrenaline.model;

import com.adrenalinici.adrenaline.model.event.DashboardCellUpdatedEvent;
import com.adrenalinici.adrenaline.model.event.GameModelUpdatedEvent;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.model.event.PlayerDashboardUpdatedEvent;
import com.adrenalinici.adrenaline.util.Bag;
import com.adrenalinici.adrenaline.util.ListUtils;
import com.adrenalinici.adrenaline.util.Observable;

import java.util.*;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.stream.Collectors;

import static java.util.Map.Entry;

public class GameModel extends Observable<ModelEvent> {
  private List<Map.Entry<PlayerColor, Boolean>> killScore;
  private int remainingSkulls;
  private List<PlayerColor> doubleKillScore;
  private Dashboard dashboard;
  private List<PlayerDashboard> playerDashboards;

  public GameModel(int remainingSkulls, Dashboard dashboard, List<PlayerDashboard> playerDashboards) {
    this.remainingSkulls = remainingSkulls;
    this.dashboard = dashboard;
    this.playerDashboards = playerDashboards;
    this.killScore = new ArrayList<>();
    this.doubleKillScore = new ArrayList<>();
  }

  public int getRemainingSkulls() {
    return remainingSkulls;
  }

  public List<PlayerColor> getDoubleKillScore() {
    return doubleKillScore;
  }

  public List<Map.Entry<PlayerColor, Boolean>> getKillScore() {
    return killScore;
  }

  public int decrementSkulls() {
    return remainingSkulls -= 1;
  }

  public void addDoubleKillScore(PlayerColor playerColor) {
    doubleKillScore.add(playerColor);
  }

  public void addKillScore(PlayerColor playerColor, boolean cruelKill) {
    Entry<PlayerColor, Boolean> e = new SimpleImmutableEntry<>(playerColor, cruelKill);
    killScore.add(e);
  }

  public Dashboard getDashboard() {
    return dashboard;
  }

  public List<PlayerDashboard> getPlayerDashboards() {
    return playerDashboards;
  }

  public PlayerDashboard getPlayerDashboard(PlayerColor player) {
    return getPlayerDashboards().stream().filter(d -> player.equals(d.getPlayer())).findFirst().get();
  }

  public boolean isFrenzyMode() {
    return remainingSkulls == 0;
  }

  public List<PlayerColor> getPlayers() {
    return getPlayerDashboards().stream().map(PlayerDashboard::getPlayer).collect(Collectors.toList());
  }

  public Position getPlayerPosition(PlayerColor player) {
    return dashboard.getPlayersPositions().get(player);
  }

  public void movePlayerInDashboard(Position newPosition, PlayerColor player) {
    Position oldPosition = getPlayerPosition(player);
    DashboardCell oldCell = dashboard.getDashboardCell(oldPosition);
    oldCell.removePlayer(player);
    notifyEvent(new DashboardCellUpdatedEvent(this, oldCell));

    DashboardCell newCell = dashboard.getDashboardCell(newPosition);
    newCell.addPlayer(player);
    notifyEvent(new DashboardCellUpdatedEvent(this, newCell));
  }

  public void acquireAmmoCard(PickupDashboardCell cell, PlayerColor player) {
    PlayerDashboard playerDashboard = getPlayerDashboard(player);
    cell.getAmmoCard()
      .ifPresent(
        ac -> {
          ac.getAmmoColor().forEach(playerDashboard::addAmmo);
          ac.getPowerUpCard().ifPresent(playerDashboard::addPowerUpCard);
          cell.setAmmoCard(null);
          notifyEvent(new DashboardCellUpdatedEvent(this, cell));
          notifyEvent(new PlayerDashboardUpdatedEvent(this, playerDashboard));
        }
      );
  }

  public void acquireGun(RespawnDashboardCell cell, PlayerColor player, Gun chosenGun) {
    PlayerDashboard playerDashboard = getPlayerDashboard(player);
    playerDashboard.addLoadedGun(chosenGun);
    List<AmmoColor> playerAmmos = new ArrayList<>(playerDashboard.getAmmos());
    Bag<AmmoColor> playerAmmosBag = Bag.from(playerAmmos);
    Bag<AmmoColor> requiredAmmoToPickupBag = Bag.from(chosenGun.getRequiredAmmoToPickup());

    if (!playerAmmosBag.contains(requiredAmmoToPickupBag)) {
      List<AmmoColor> ammosToGetFromPowerUp = ListUtils.differencePure(chosenGun.getRequiredAmmoToPickup(),
        playerAmmos.stream()
          .filter(ammo ->
            chosenGun.getRequiredAmmoToPickup().contains(ammo)
          ).collect(Collectors.toList()));

      List<AmmoColor> ammosToRemove = ListUtils.differencePure(chosenGun.getRequiredAmmoToPickup(),
        ammosToGetFromPowerUp);
      playerDashboard.removeAmmos(ammosToRemove);

      ammosToGetFromPowerUp.forEach(ammo -> {
        PowerUpCard toRemove = playerDashboard.getPowerUpCards().stream().filter(powerUpCard ->
          powerUpCard.getAmmoColor().equals(ammo)).findFirst().get();
        playerDashboard.removePowerUpCard(toRemove);
      });
    } else playerDashboard.removeAmmos(chosenGun.getRequiredAmmoToPickup());
    cell.getAvailableGuns().remove(chosenGun);
    notifyEvent(new DashboardCellUpdatedEvent(this, cell));
    notifyEvent(new PlayerDashboardUpdatedEvent(this, playerDashboard));
  }

  public void reloadGun(PlayerColor player, Gun chosenGun) {
    PlayerDashboard playerDashboard = getPlayerDashboard(player);
    playerDashboard.addLoadedGun(chosenGun);
    playerDashboard.removeUnloadedGun(chosenGun);
    List<AmmoColor> playerAmmos = new ArrayList<>(playerDashboard.getAmmos());
    Bag<AmmoColor> playerAmmosBag = Bag.from(playerAmmos);
    Bag<AmmoColor> requiredAmmoToReloadBag = Bag.from(chosenGun.getRequiredAmmoToReload());

    if (!playerAmmosBag.contains(requiredAmmoToReloadBag)) {
      List<AmmoColor> ammosToGetFromPowerUp = ListUtils.differencePure(chosenGun.getRequiredAmmoToReload(),
        playerAmmos.stream()
          .filter(ammo ->
            chosenGun.getRequiredAmmoToReload().contains(ammo)
          ).collect(Collectors.toList()));

      List<AmmoColor> ammosToRemove = ListUtils.differencePure(chosenGun.getRequiredAmmoToReload(),
        ammosToGetFromPowerUp);
      playerDashboard.removeAmmos(ammosToRemove);

      ammosToGetFromPowerUp.forEach(ammo -> {
        PowerUpCard toRemove = playerDashboard.getPowerUpCards().stream().filter(
          powerUpCard -> powerUpCard.getAmmoColor().equals(ammo)
        ).findFirst().get();
        playerDashboard.removePowerUpCard(toRemove);
      });
    } else playerDashboard.removeAmmos(chosenGun.getRequiredAmmoToPickup());
    notifyEvent(new PlayerDashboardUpdatedEvent(this, playerDashboard));
  }

  public boolean hitPlayer(PlayerColor killer, PlayerColor victim, int damages) {
    PlayerDashboard victimPlayerDashboard = getPlayerDashboard(victim);
    PlayerDashboard killerPlayerDashboard = getPlayerDashboard(killer);
    int killerMarksOnVictimDashboard = victimPlayerDashboard.getMarks().stream()
      .filter(playerColor -> playerColor.equals(killer)).collect(Collectors.toList()).size();

    damages += killerMarksOnVictimDashboard;

    victimPlayerDashboard.addDamages(
      Collections.nCopies(damages, killer)
    );

    victimPlayerDashboard.removeMarks(
      Collections.nCopies(killerMarksOnVictimDashboard, killer)
    );

    if (victimPlayerDashboard.getKillDamage().isPresent()) {
      decrementSkulls();
      victimPlayerDashboard.incrementSkullsNumber();

      if (victimPlayerDashboard.getCruelDamage().isPresent()) {
        killScore.add(new AbstractMap.SimpleEntry<PlayerColor, Boolean>(killer, Boolean.TRUE));
        markPlayer(victim, killer, 1);
        notifyEvent(new PlayerDashboardUpdatedEvent(this, killerPlayerDashboard));
      } else killScore.add(new AbstractMap.SimpleEntry<PlayerColor, Boolean>(killer, Boolean.FALSE));
      notifyEvent(new GameModelUpdatedEvent(this));
    }

    notifyEvent(new PlayerDashboardUpdatedEvent(this, victimPlayerDashboard));
    return victimPlayerDashboard.getKillDamage().isPresent();
  }

  public void markPlayer(PlayerColor killer, PlayerColor victim, int marks) {
    PlayerDashboard victimPlayerDashboard = getPlayerDashboard(victim);
    int marksOnOtherPlayerDashboards = calculateMarksOnOtherPlayerDashboards(killer);

    if (marksOnOtherPlayerDashboards != 3) {
      switch (marksOnOtherPlayerDashboards) {
        case 0:
          victimPlayerDashboard.addMarks(Collections.nCopies(marks, killer));
          break;
        case 1:
          victimPlayerDashboard.addMarks(Collections.nCopies(marks > 2 ? 2 : marks, killer));
          break;
        case 2:
          victimPlayerDashboard.addMarks(Collections.nCopies(marks > 1 ? 1 : marks, killer));
          break;
      }
    }

    notifyEvent(new PlayerDashboardUpdatedEvent(this, victimPlayerDashboard));
  }

  public boolean hitAndMarkPlayer(PlayerColor killer, PlayerColor victim, int damages, int marks) {
    PlayerDashboard victimPlayerDashboard = getPlayerDashboard(victim);

    hitPlayer(killer, victim, damages);
    markPlayer(killer, victim, marks);

    return victimPlayerDashboard.getKillDamage().isPresent();
  }

  /**
   * @param player
   * @return number of player marks on other playerDashboards
   */
  public int calculateMarksOnOtherPlayerDashboards(PlayerColor player) {
    List<PlayerColor> marksOnOtherPlayerDashboards = new ArrayList<>();
    playerDashboards.stream()
      .filter(playerDashboard -> !playerDashboard.getPlayer().equals(player))
      .forEach(playerDashboard ->
        playerDashboard.getMarks().stream()
          .filter(playerColor -> playerColor.equals(player))
          .forEach(playerColor -> marksOnOtherPlayerDashboards.add(playerColor))
      );

    return marksOnOtherPlayerDashboards.size();
  }

}
