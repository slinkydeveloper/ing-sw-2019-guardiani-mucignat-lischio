package com.adrenalinici.adrenaline.model;

import com.adrenalinici.adrenaline.model.event.DashboardCellUpdatedEvent;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.model.event.PlayerDashboardUpdatedEvent;
import com.adrenalinici.adrenaline.util.Bag;
import com.adrenalinici.adrenaline.util.Observable;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.Entry;

public class GameStatus extends Observable<ModelEvent> {
  private List<Map.Entry<PlayerColor, Boolean>> killScore;
  private int remainingSkulls;
  private List<PlayerColor> doubleKillScore;
  private Dashboard dashboard;
  private List<PlayerDashboard> playerDashboards;

  public GameStatus(int remainingSkulls, Dashboard dashboard, List<PlayerDashboard> playerDashboards) {
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


  public Position getPlayerPosition(PlayerColor player) {
    return dashboard.getPlayersPositions().get(player);
  }

  public void movePlayerInDashboard(Position newPosition, PlayerColor player) {
    Position oldPosition = getPlayerPosition(player);
    DashboardCell oldCell = dashboard.getDashboardCell(oldPosition).get();
    oldCell.removePlayer(player);
    notifyEvent(new DashboardCellUpdatedEvent(this, oldCell));

    DashboardCell newCell = dashboard.getDashboardCell(newPosition).get();
    newCell.addPlayer(player);
    notifyEvent(new DashboardCellUpdatedEvent(this, newCell));
  }

  public List<Gun> calculateAvailableGunsToPickup(RespawnDashboardCell respawnCell, PlayerColor player) {
    List<AmmoColor> playerAmmos = new ArrayList<>(getPlayerDashboard(player).getAmmos());
    getPlayerDashboard(player).getPowerUpCards().stream().forEach(powerUpCard -> playerAmmos.add(powerUpCard.getAmmoColor()));
    Bag playerAmmosBag = Bag.from(playerAmmos);

    return respawnCell.getAvailableGuns().stream()
      .filter(
        gun -> playerAmmosBag.contains(Bag.from(gun.getRequiredAmmoToPickup()))
      )
      .collect(Collectors.toList());
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
    playerDashboard.removeAmmos(chosenGun.getRequiredAmmoToPickup());
    cell.getAvailableGuns().remove(chosenGun);
    notifyEvent(new DashboardCellUpdatedEvent(this, cell));
    notifyEvent(new PlayerDashboardUpdatedEvent(this, playerDashboard));
  }


}
