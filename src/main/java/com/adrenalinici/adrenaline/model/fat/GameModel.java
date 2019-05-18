package com.adrenalinici.adrenaline.model.fat;

import com.adrenalinici.adrenaline.controller.GunLoader;
import com.adrenalinici.adrenaline.model.CardDeck;
import com.adrenalinici.adrenaline.model.common.*;
import com.adrenalinici.adrenaline.model.event.DashboardCellUpdatedEvent;
import com.adrenalinici.adrenaline.model.event.GameModelUpdatedEvent;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.model.event.PlayerDashboardUpdatedEvent;
import com.adrenalinici.adrenaline.model.light.LightGameModel;
import com.adrenalinici.adrenaline.util.JsonUtils;
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
  private CardDeck<String> guns;
  private CardDeck<PowerUpCard> powerUps;
  private CardDeck<AmmoCard> ammoCards;

  public GameModel(int remainingSkulls, Dashboard dashboard, List<PlayerDashboard> playerDashboards) {
    this.remainingSkulls = remainingSkulls;
    this.dashboard = dashboard;
    this.playerDashboards = playerDashboards;
    this.killScore = new ArrayList<>();
    this.doubleKillScore = new ArrayList<>();
    this.guns = new CardDeck<>(GunLoader.getAvailableGuns());
    this.powerUps = new CardDeck<>(JsonUtils.loadPowerUpCards());
    this.ammoCards = new CardDeck<>(JsonUtils.loadAmmoCards());
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
    return getPlayerDashboards().stream().filter(d -> player.equals(d.getPlayer())).findFirst().orElseThrow(() -> new IllegalStateException("Player not present " + player));
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
    notifyEvent(new DashboardCellUpdatedEvent(this, oldPosition));

    DashboardCell newCell = dashboard.getDashboardCell(newPosition);
    newCell.addPlayer(player);
    notifyEvent(new DashboardCellUpdatedEvent(this, newPosition));
  }

  public void acquireAmmoCard(PickupDashboardCell cell, PlayerColor player) {
    PlayerDashboard playerDashboard = getPlayerDashboard(player);
    cell.getAmmoCard()
      .ifPresent(
        ac -> {
          ac.getAmmoColor().forEach(playerDashboard::addAmmo);
          if (ac.isPickPowerUp())
            this.acquirePowerUpCard(player);
          cell.removeAmmoCard();
          notifyEvent(new DashboardCellUpdatedEvent(this, cell.getPosition()));
          notifyEvent(new PlayerDashboardUpdatedEvent(this, player));
        }
      );
  }

  public void acquireGun(PlayerColor player, Gun chosenGun) {
    PlayerDashboard playerDashboard = getPlayerDashboard(player);
    playerDashboard.addGun(chosenGun.getId());

    RespawnDashboardCell cell = (RespawnDashboardCell) dashboard.getDashboardCell(getPlayerPosition(player));
    cell.getAvailableGuns().remove(chosenGun.getId());

    playerDashboard.removeAmmosIncludingPowerups(chosenGun.getRequiredAmmoToPickup());

    notifyEvent(new DashboardCellUpdatedEvent(this, cell.getPosition()));
    notifyEvent(new PlayerDashboardUpdatedEvent(this, player));
  }

  /**
   * Acquire power up card from gun deck
   *
   * @param player
   */
  public void acquirePowerUpCard(PlayerColor player) {
    getPlayerDashboard(player).addPowerUpCard(powerUps.getCard());
    notifyEvent(new PlayerDashboardUpdatedEvent(this, player));
  }

  /**
   * Respawn a player
   *
   * @param playerColor
   * @param card
   */
  public void respawnPlayer(PlayerColor playerColor, PowerUpCard card) {
    DashboardCell cell = dashboard
      .stream()
      .filter(c -> c.getCellColor().matchesAmmoColor(card.getAmmoColor()))
      .findFirst()
      .orElseThrow(() -> new IllegalStateException("Cannot find a cell with color " + card.getAmmoColor()));

    cell.addPlayer(playerColor);
    notifyEvent(new DashboardCellUpdatedEvent(this, cell.getPosition()));

    getPlayerDashboard(playerColor).removePowerUpCard(card);
    powerUps.addCard(card);
    notifyEvent(new PlayerDashboardUpdatedEvent(this, playerColor));
  }

  /**
   * Respawn player in dashboard position
   *
   * @param player
   * @param chosenGun
   */

  public void reloadGun(PlayerColor player, Gun chosenGun) {
    PlayerDashboard playerDashboard = getPlayerDashboard(player);
    playerDashboard.reloadGun(chosenGun.getId());

    playerDashboard.removeAmmosIncludingPowerups(chosenGun.getRequiredAmmoToReload());

    notifyEvent(new PlayerDashboardUpdatedEvent(this, player));
  }

  private boolean hitter(PlayerColor killer, PlayerColor victim, int damages) {
    PlayerDashboard victimPlayerDashboard = getPlayerDashboard(victim);

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
      // Player was killed
      decrementSkulls();
      victimPlayerDashboard.incrementSkullsNumber();

      // Removed player from cell
      Position killedPlayerPosition = getPlayerPosition(victim);
      dashboard.getDashboardCell(killedPlayerPosition).removePlayer(victim);
      notifyEvent(new DashboardCellUpdatedEvent(this, killedPlayerPosition));

      if (victimPlayerDashboard.getCruelDamage().isPresent()) {
        killScore.add(new AbstractMap.SimpleImmutableEntry<>(killer, Boolean.TRUE));
        markPlayer(victim, killer, 1);
      } else killScore.add(new AbstractMap.SimpleImmutableEntry<>(killer, Boolean.FALSE));
      notifyEvent(new GameModelUpdatedEvent(this));
    }

    return victimPlayerDashboard.getKillDamage().isPresent();
  }

  private void marker(PlayerColor killer, PlayerColor victim, int marks) {
    PlayerDashboard victimPlayerDashboard = getPlayerDashboard(victim);
    int killerMarksOnVictimPlayerDashboard = calculateKillerMarksOnVictimPlayerDashboard(killer, victim);

    victimPlayerDashboard.addMarks(
      Collections.nCopies(
        marks > 3 - killerMarksOnVictimPlayerDashboard ? 3 - killerMarksOnVictimPlayerDashboard : marks,
        killer
      )
    );
  }

  /**
   * This method hits a victim (adding damages).
   * It also mutates killer's marks into damages (in case there's any of it)
   * and then removes it from victim Playerdashboard.
   * In case victim is killed it decrements skulls in Game model,
   * and increments skulls in victim Playerdashboard,
   * then it checks if cruelDamage is present and add a new element to killScore,
   * launching a new GameModelUpdatedEvent.
   * Finally launches a new PlayerDashboardUpdatedEvent
   * of victim's color.
   *
   * @param killer  player who hits
   * @param victim  hitted player
   * @param damages number of damages to add
   * @return true if victim was killed
   */
  public boolean hitPlayer(PlayerColor killer, PlayerColor victim, int damages) {
    boolean killed = hitter(killer, victim, damages);

    notifyEvent(new PlayerDashboardUpdatedEvent(this, victim));
    return killed;
  }

  /**
   * This method marks a victim after making a check on the number
   * of killer's marks already present on all other PlayerDashboards.
   * Finally launches a new PlayerDashboardUpdatedEvent
   * of victim's color.
   *
   * @param killer player who marks
   * @param victim marked player
   * @param marks number of marks to add
   */
  public void markPlayer(PlayerColor killer, PlayerColor victim, int marks) {
    marker(killer, victim, marks);
    notifyEvent(new PlayerDashboardUpdatedEvent(this, victim));
  }

  /**
   * This method hits a victim (adding damages).
   * It also mutates killer's marks into damages (in case there's any of it)
   * and then removes it from victim Playerdashboard.
   * In case victim is killed it decrements skulls in Game model,
   * and increments skulls in victim Playerdashboard,
   * then it checks if cruelDamage is present and add a new element to killScore,
   * launching a new GameModelUpdatedEvent.
   * After that, it marks the victim after making a check on the number
   * of killer's marks already present on all other PlayerDashboards.
   * Finally launches a new PlayerDashboardUpdatedEvent
   * of victim's color.
   *
   * @param killer
   * @param victim
   * @param damages
   * @param marks
   * @return true if victim was killed
   */
  public boolean hitAndMarkPlayer(PlayerColor killer, PlayerColor victim, int damages, int marks) {
    boolean killed = hitter(killer, victim, damages);
    marker(killer, victim, marks);

    notifyEvent(new PlayerDashboardUpdatedEvent(this, victim));
    return killed;
  }

  /**
   * @param player
   * @return number of player marks on other playerDashboards
   */
  @Deprecated
  public int calculateMarksOnOtherPlayerDashboards(PlayerColor player) {
    return (int) playerDashboards
      .stream()
      .filter(playerDashboard -> !playerDashboard.getPlayer().equals(player))
      .mapToInt(playerDashboard ->
        playerDashboard
          .getMarks()
          .stream()
          .filter(playerColor -> playerColor.equals(player))
          .collect(Collectors.toList())
          .size()
      ).count();
  }

  /**
   * @param killer
   * @param victim
   * @return number of killer marks on victim dashboard
   */
  public int calculateKillerMarksOnVictimPlayerDashboard(PlayerColor killer, PlayerColor victim) {
    return (int) getPlayerDashboard(victim)
      .getMarks()
      .stream()
      .filter(playerColor -> playerColor.equals(killer)).count();
  }

  /**
   * Mutates the internal dashboard refilling, if needed, {@link RespawnDashboardCell} with guns and {@link PickupDashboardCell} with ammo cards
   */
  public void refillDashboard() {
    dashboard.stream().forEach(dc -> {
      dc.visit(rpc -> {
        while (rpc.getAvailableGuns().size() < 3 && !gunsDeck().isEmpty()) {
          rpc.addAvailableGun(gunsDeck().getCard());
        }
      }, pdc -> {
        if (!pdc.getAmmoCard().isPresent() && !ammoCardDeck().isEmpty()) {
          pdc.setAmmoCard(ammoCardDeck().getCard());
        }
      });
    });
  }

  public LightGameModel light() {
    return new LightGameModel(
      killScore,
      remainingSkulls,
      doubleKillScore,
      dashboard.light(),
      playerDashboards.stream().map(PlayerDashboard::light).collect(Collectors.toList())
    );
  }

  public CardDeck<String> gunsDeck() {
    return guns;
  }

  public CardDeck<PowerUpCard> powerUpsDeck() {
    return powerUps;
  }

  public CardDeck<AmmoCard> ammoCardDeck() {
    return ammoCards;
  }
}
