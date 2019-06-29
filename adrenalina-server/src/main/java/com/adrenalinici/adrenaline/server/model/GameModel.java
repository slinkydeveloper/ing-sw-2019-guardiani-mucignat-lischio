package com.adrenalinici.adrenaline.server.model;

import com.adrenalinici.adrenaline.common.model.*;
import com.adrenalinici.adrenaline.common.model.event.DashboardCellUpdatedEvent;
import com.adrenalinici.adrenaline.common.model.event.GameModelUpdatedEvent;
import com.adrenalinici.adrenaline.common.model.event.ModelEvent;
import com.adrenalinici.adrenaline.common.model.event.PlayerDashboardUpdatedEvent;
import com.adrenalinici.adrenaline.common.model.light.LightGameModel;
import com.adrenalinici.adrenaline.common.util.ObservableImpl;
import com.adrenalinici.adrenaline.server.JsonUtils;
import com.adrenalinici.adrenaline.server.controller.GunLoader;

import java.util.*;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Map.Entry;

public class GameModel extends ObservableImpl<ModelEvent> {
  private final static List<Integer> POINTS_FOR_KILL = Arrays.asList(8, 6, 4, 2);
  private final static List<Integer> POINTS_FOR_KILL_FRENZY_MODE = Arrays.asList(2);

  private List<Map.Entry<PlayerColor, Boolean>> killScore;
  private int remainingSkulls;
  private List<PlayerColor> doubleKillScore;
  private Dashboard dashboard;
  private List<PlayerDashboard> playerDashboards;
  private CardDeck<String> guns;
  private CardDeck<PowerUpCard> powerUps;
  private CardDeck<AmmoCard> ammoCards;
  private PlayerColor frenzyModeActivatedWithPlayerTurn;
  private boolean mustActivateFrenzyMode;

  public GameModel(int remainingSkulls, Dashboard dashboard, List<PlayerDashboard> playerDashboards, boolean mustActivateFrenzyMode) {
    this.remainingSkulls = remainingSkulls;
    this.dashboard = dashboard;
    this.playerDashboards = playerDashboards;
    this.killScore = new ArrayList<>();
    this.doubleKillScore = new ArrayList<>();
    this.guns = new CardDeck<>(GunLoader.getAvailableGuns());
    this.powerUps = new CardDeck<>(JsonUtils.loadPowerUpCards());
    this.ammoCards = new CardDeck<>(JsonUtils.loadAmmoCards());
    this.mustActivateFrenzyMode = mustActivateFrenzyMode;
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

  public boolean noRemainingSkulls() {
    return remainingSkulls == 0;
  }

  public List<PlayerColor> getPlayers() {
    return getPlayerDashboards().stream().map(PlayerDashboard::getPlayer).collect(Collectors.toList());
  }

  public Position getPlayerPosition(PlayerColor player) {
    return dashboard.getPlayersPositions().get(player);
  }

  public void movePlayerInDashboard(Position newPosition, PlayerColor player) {
    if (!newPosition.equals(getPlayerPosition(player))) {
      Position oldPosition = getPlayerPosition(player);
      DashboardCell oldCell = dashboard.getDashboardCell(oldPosition);
      oldCell.removePlayer(player);
      notifyEvent(new DashboardCellUpdatedEvent(light(), oldPosition));

      DashboardCell newCell = dashboard.getDashboardCell(newPosition);
      newCell.addPlayer(player);
      notifyEvent(new DashboardCellUpdatedEvent(light(), newPosition));
    }
  }

  public void acquireAmmoCard(PickupDashboardCell cell, PlayerColor player) {
    PlayerDashboard playerDashboard = getPlayerDashboard(player);
    cell.getAmmoCard()
      .ifPresent(
        ac -> {
          ac.getAmmoColor().forEach(playerDashboard::addAmmo);
          if (ac.isPickPowerUp())
            this.acquirePowerUpCard(player);
          this.ammoCards.addCard(ac);
          cell.removeAmmoCard();
          notifyEvent(new DashboardCellUpdatedEvent(light(), cell.getPosition()));
          notifyEvent(new PlayerDashboardUpdatedEvent(light(), player));
        }
      );
  }

  /**
   * Removes power up from player, then fires a {@link PlayerDashboardUpdatedEvent}
   */
  public void removePowerUpFromPlayer(PlayerColor player, PowerUpCard card) {
    if (this.getPlayerDashboard(player).removePowerUpCard(card)) {
      this.powerUpsDeck().addCard(card);
      notifyEvent(new PlayerDashboardUpdatedEvent(light(), player));
    }
  }

  public void acquireGun(PlayerColor player, Gun chosenGun) {
    PlayerDashboard playerDashboard = getPlayerDashboard(player);
    playerDashboard.addGun(chosenGun.getId());

    RespawnDashboardCell cell = (RespawnDashboardCell) dashboard.getDashboardCell(getPlayerPosition(player));
    cell.getAvailableGuns().remove(chosenGun.getId());

    playerDashboard.removeAmmosIncludingPowerups(chosenGun.getRequiredAmmoToPickup());

    notifyEvent(new DashboardCellUpdatedEvent(light(), cell.getPosition()));
    notifyEvent(new PlayerDashboardUpdatedEvent(light(), player));
  }

  /**
   * Acquire power up card from gun deck
   *
   * @param player
   */
  public void acquirePowerUpCard(PlayerColor player) {
    if (!powerUps.isEmpty()) {
      PowerUpCard cardToAdd = powerUps.getCard();

      boolean added = getPlayerDashboard(player).addPowerUpCard(cardToAdd);
      if (!added) powerUps.addCard(cardToAdd);
      else notifyEvent(new PlayerDashboardUpdatedEvent(light(), player));
    }
  }

  /**
   * Activate frenzy mode: flips player dashboards when possible, register newTurn
   */
  public void activateFrenzyMode(PlayerColor newTurnOfPlayer) {
    this.frenzyModeActivatedWithPlayerTurn = newTurnOfPlayer;

    playerDashboards.forEach(pd -> {
      if (pd.getDamages().isEmpty()) pd.setFlipped(true);
    });
  }

  public boolean isFrenzyModeFinished(PlayerColor nextTurnPlayer) {
    return this.frenzyModeActivatedWithPlayerTurn == nextTurnPlayer;
  }

  public boolean isFrenzyModeActivated() {
    return frenzyModeActivatedWithPlayerTurn != null;
  }

  public boolean isMustActivateFrenzyMode() {
    return mustActivateFrenzyMode;
  }

  public boolean isFirstPlayerOrAfterFirstPlayerInFrenzyMode(PlayerColor thisTurnPlayer) {
    int indexOfFrenzyModeFirstPlayer = getPlayers().indexOf(this.frenzyModeActivatedWithPlayerTurn);
    int indexThisTurnPlayer = getPlayers().indexOf(thisTurnPlayer);
    return indexThisTurnPlayer == 0 || indexOfFrenzyModeFirstPlayer == 0 || indexThisTurnPlayer / indexOfFrenzyModeFirstPlayer == 0;
  }

  public void assignEndGamePoints() {
    playerDashboards.forEach(pd -> {
      assignPoints(pd);
      pd.removeAllDamages();
    });
  }

  /**
   * @return an ordered list with the points assigned to every player.
   * First element correspond to the player who has more points.
   */
  public List<Map.Entry<PlayerColor, Integer>> getRanking() {
    return getPlayerDashboards()
      .stream()
      .map(p -> new SimpleImmutableEntry<>(p.getPlayer(), p.getPoints()))
      .sorted(((o1, o2) -> Integer.compare(o2.getValue(), o1.getValue())))
      .collect(Collectors.toList());
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
      .filter(DashboardCell::isRespawnCell)
      .findFirst()
      .orElseThrow(() -> new IllegalStateException("Cannot find a cell with color " + card.getAmmoColor()));

    cell.addPlayer(playerColor);
    notifyEvent(new DashboardCellUpdatedEvent(light(), cell.getPosition()));

    getPlayerDashboard(playerColor).removePowerUpCard(card);
    powerUps.addCard(card);
    notifyEvent(new PlayerDashboardUpdatedEvent(light(), playerColor));
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

    notifyEvent(new PlayerDashboardUpdatedEvent(light(), player));
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

    boolean killed = victimPlayerDashboard.getKillDamage().isPresent();

    if (killed) {
      // Player was killed
      decrementSkulls();

      // Removed player from cell
      Position killedPlayerPosition = getPlayerPosition(victim);
      dashboard.getDashboardCell(killedPlayerPosition).removePlayer(victim);
      notifyEvent(new DashboardCellUpdatedEvent(light(), killedPlayerPosition));

      // Add kill score to dashboard
      if (victimPlayerDashboard.getCruelDamage().isPresent()) {
        killScore.add(new AbstractMap.SimpleImmutableEntry<>(killer, Boolean.TRUE));
        markPlayer(victim, killer, 1);
      } else killScore.add(new AbstractMap.SimpleImmutableEntry<>(killer, Boolean.FALSE));
      notifyEvent(new GameModelUpdatedEvent(light()));

      // Add points to killers
      assignPoints(victimPlayerDashboard);

      // Reset victim player dashboard
      victimPlayerDashboard.removeAllDamages();

      victimPlayerDashboard.incrementSkullsNumber();
    }

    return killed;
  }

  private void assignPoints(PlayerDashboard victimPlayerDashboard) {
    Map<PlayerColor, Integer> pointsToAssign = new HashMap<>();

    // First blood
    if (!isFrenzyModeActivated() && victimPlayerDashboard.getFirstDamage().isPresent()) {
      pointsToAssign.put(victimPlayerDashboard.getFirstDamage().get(), 1);
    }

    // Ordered players based on damages
    List<PlayerColor> orderedKillers = victimPlayerDashboard
      .getDamages()
      .stream()
      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
      .entrySet()
      .stream()
      .sorted((e1, e2) -> -Long.compare(e1.getValue(), e2.getValue()))
      .map(Map.Entry::getKey)
      .collect(Collectors.toList());

    // Choose what point scheme we should use
    List<Integer> pointScheme = (victimPlayerDashboard.isFlipped()) ? POINTS_FOR_KILL_FRENZY_MODE : POINTS_FOR_KILL;

    IntStream.range(0, orderedKillers.size())
      .forEach(i ->
        pointsToAssign.merge(
          orderedKillers.get(i),
          (i + victimPlayerDashboard.getSkullsNumber() < pointScheme.size()) ? pointScheme.get(i + victimPlayerDashboard.getSkullsNumber()) : 1,
          Integer::sum
        )
      );

    pointsToAssign.forEach((pc, p) -> {
      getPlayerDashboard(pc).addPoints(p);
      notifyEvent(new PlayerDashboardUpdatedEvent(light(), pc));
    });

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

    notifyEvent(new PlayerDashboardUpdatedEvent(light(), victim));
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
    notifyEvent(new PlayerDashboardUpdatedEvent(light(), victim));
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

    notifyEvent(new PlayerDashboardUpdatedEvent(light(), victim));
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
      AtomicBoolean mut = new AtomicBoolean(false);
      dc.visit(rpc -> {
        while (rpc.getAvailableGuns().size() < 3 && !gunsDeck().isEmpty()) {
          rpc.addAvailableGun(gunsDeck().getCard());
          mut.set(true);
        }
      }, pdc -> {
        if (!pdc.getAmmoCard().isPresent() && !ammoCardDeck().isEmpty()) {
          pdc.setAmmoCard(ammoCardDeck().getCard());
          mut.set(true);
        }
      });
      if (mut.get()) {
        notifyEvent(new DashboardCellUpdatedEvent(light(), dc.getPosition()));
      }
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
