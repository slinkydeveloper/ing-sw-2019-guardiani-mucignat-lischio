package com.adrenalinici.adrenaline.cli;

import com.adrenalinici.adrenaline.model.common.*;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.model.light.LightGameModel;
import com.adrenalinici.adrenaline.network.client.ClientNetworkAdapter;
import com.adrenalinici.adrenaline.network.client.rmi.RmiClientNetworkAdapter;
import com.adrenalinici.adrenaline.network.client.socket.SocketClientNetworkAdapter;
import com.adrenalinici.adrenaline.network.outbox.InfoType;
import com.adrenalinici.adrenaline.view.event.ActionChosenEvent;
import com.adrenalinici.adrenaline.view.event.MovementChosenEvent;
import com.adrenalinici.adrenaline.view.event.NewTurnEvent;
import com.adrenalinici.adrenaline.view.event.PowerUpCardChosenEvent;

import java.util.*;
import java.util.stream.Collectors;

public class CliMain extends BaseCliGameView {

  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_BLACK = "\u001B[30m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_YELLOW = "\u001B[33m";
  public static final String ANSI_BLUE = "\u001B[34m";
  public static final String ANSI_PURPLE = "\u001B[35m";
  public static final String ANSI_CYAN = "\u001B[36m";
  public static final String ANSI_WHITE = "\u001B[37m";
  public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
  public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
  public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
  public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
  public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
  public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
  public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
  public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

  Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {

    if (args.length < 3) {
      System.err.println("Usage: adrenaline-cli socket|rmi HOST PORT");
      System.exit(0);
    }

    String transport = args[0];
    String host = args[1];
    int port = Integer.parseInt(args[2]);

    CliMain cliMain = new CliMain();
    CliGameViewProxy proxy = new CliGameViewProxy(cliMain);
    ClientNetworkAdapter networkAdapter = null;
    if (transport.equals("socket")) {
      networkAdapter = new SocketClientNetworkAdapter(proxy, host, port);
    } else if (transport.equals("rmi")) {
      networkAdapter = new RmiClientNetworkAdapter(proxy, host, port);
    } else {
      System.out.println("Wrong transport!");
      System.exit(1);
    }
    System.out.println(String.format("Transport %s to %s:%d initialized", transport, host, port));
    networkAdapter.run();
  }

  @Override
  public void showInfoMessage(String information, InfoType infoType) {
    System.out.println(infoType.name() + ":" + information);
  }

  @Override
  public void showAvailableMatchesAndPlayers(Map<String, Set<PlayerColor>> matches) {
    if (getMyPlayer() == null) {
      System.out.println("Available matches");
      matches.forEach((matchId, availablePlayers) ->
        System.out.println(String.format("Match %s: %s", matchId, availablePlayers.stream().map(Objects::toString).collect(Collectors.joining(", "))))
      );
    }
    String command = scanner.nextLine();
    //TODO I did in this way just because I'm lazy to do something better
    // @peppelischio offers himself as volunteer to improve it and all this shitty class <3
    String[] commandChunks = command.trim().split("\\s");
    if ("new".equals(commandChunks[0].toLowerCase())) {
      String matchId = commandChunks[1];
      DashboardChoice dashboard = DashboardChoice.valueOf(commandChunks[2].trim().toUpperCase());
      PlayersChoice players = PlayersChoice.valueOf(commandChunks[3].trim().toUpperCase());
      RulesChoice rules = RulesChoice.valueOf(commandChunks[4].trim().toUpperCase());
      sendStartNewMatch(matchId, dashboard, players, rules);
    } else {
      String matchId = commandChunks[0];
      PlayerColor color = PlayerColor.valueOf(commandChunks[1].trim().toUpperCase());
      sendChosenMatch(matchId, color);
    }
  }

  @Override
  public void showAvailableActions(List<Action> actions) {
    if (isMyTurn()) {
      System.out.println("Available actions:");
      printNumberedList(actions);

      int chosenIndex = parseIndex(actions.size() - 1);

      sendViewEvent(new ActionChosenEvent(actions.get(chosenIndex)));
    }
  }

  @Override
  public void showAvailableMovements(List<Position> positions) {
    if (isMyTurn()) {
      System.out.println("Available movements:");
      printNumberedList(positions);

      int chosenIndex = parseIndex(positions.size() - 1);
      sendViewEvent(new MovementChosenEvent(positions.get(chosenIndex)));
    }
  }

  @Override
  public void showNextTurn(PlayerColor player) {
    this.setTurnOfPlayer(player);
    System.out.println("Turn of " + player);
    this.sendViewEvent(new NewTurnEvent());
  }

  @Override
  public void showReloadableGuns(Set<String> guns) {
    if (isMyTurn()) {
      List<String> gunsList = new ArrayList<>(guns);

      System.out.println("Reloadable guns:");
      printNumberedList(gunsList);

      int chosenIndex = parseIndex(gunsList.size() - 1);
      sendViewEvent(new GunChosenEvent(gunsList.get(chosenIndex)));
    }
  }

  @Override
  public void showAvailablePowerUpCardsForRespawn(PlayerColor player, List<PowerUpCard> powerUpCards) {
    if (player.equals(getMyPlayer())) {
      System.out.println("Available power up cards:");
      printNumberedList(powerUpCards);

      int chosenIndex = parseIndex(powerUpCards.size() - 1);

      System.out.println("Chosen Power up card: " + powerUpCards.get(chosenIndex));
      sendViewEvent(new PowerUpCardChosenEvent(getMyPlayer(), powerUpCards.get(chosenIndex)));
    }
  }

  @Override
  public void showAvailableAlternativeEffectsGun(Effect firstEffect, Effect secondEffect) {

  }

  @Override
  public void showChoosePlayerToHit(List<PlayerColor> players) {

  }

  @Override
  public void showAvailableExtraEffects(Effect firstExtraEffect, Effect secondExtraEffect) {

  }

  @Override
  public void showAvailableGuns(Set<String> guns) {
    if (isMyTurn()) {
      List<String> gunsList = new ArrayList<>(guns);

      System.out.println("Loaded guns:");
      printNumberedList(gunsList);

      int chosenIndex = parseIndex(gunsList.size() - 1);
      sendViewEvent(new GunChosenEvent(gunsList.get(chosenIndex)));
    }
  }

  @Override
  public void showAvailableGunsToPickup(Set<String> guns) {
    if (isMyTurn()) {
      List<String> gunsList = new ArrayList<>(guns);

      System.out.println("Available guns to pickup:");
      printNumberedList(gunsList);

      int chosenIndex = parseIndex(gunsList.size() - 1);
      sendViewEvent(new GunChosenEvent(gunsList.get(chosenIndex)));
    }
  }

  @Override
  public void showAvailableTagbackGrenade(PlayerColor player, List<PowerUpCard> powerUpCards) {
    if (player.equals(getMyPlayer())) {
      System.out.println("Available tagback granades:");
      printNumberedList(powerUpCards);

      int chosenIndex = parseIndex(powerUpCards.size() - 1);
      sendViewEvent(new UseTagbackGrenadeEvent(getMyPlayer(), powerUpCards.get(chosenIndex)));
    }
  }

  @Override
  public void showAvailableRooms(Set<CellColor> rooms) {
    if (isMyTurn()) {
      List<CellColor> roomsList = new ArrayList<>(rooms);

      System.out.println("Available rooms to hit:");
      printNumberedList(roomsList);

      int chosenIndex = parseIndex(roomsList.size() - 1);

      sendViewEvent(new RoomChosenEvent(roomsList.get(chosenIndex)));
    }
  }

  @Override
  public void showAvailableCellsToHit(Set<Position> cells) {
    if (isMyTurn()) {
      List<Position> cellsList = new ArrayList<>(cells);

      System.out.println("Available cells to hit:");
      printNumberedList(cellsList);

      int chosenIndex = parseIndex(cellsList.size() - 1);

      sendViewEvent(new CellToHitChosenEvent(cellsList.get(chosenIndex)));
    }
  }

  @Override
  public void showRanking(List<Map.Entry<PlayerColor, Integer>> ranking) {

  }

  @Override
  public void onEvent(ModelEvent newValue) {
    System.out.println("Model updated! " + newValue);
    showModel(newValue.getGameModel());
  }

  private void showModel(LightGameModel model) {

  }

  private int parseIndex(int maximum) {
    int chosenIndex = -1;

    while (!(chosenIndex >= 0 && chosenIndex <= maximum)) {
      try {
        chosenIndex = Integer.parseInt(scanner.nextLine());
        if (!(chosenIndex >= 0 && chosenIndex <= maximum))
          System.out.println(String.format("Please try again, Insert an index between 0 and %d:", maximum));
      } catch (NumberFormatException e) {
        System.out.println("What you inserted is not an integer, please retry.\nInsert index:");
      }
    }

    return chosenIndex;
  }

  private <T> void printNumberedList(List<T> list) {
    int i = 0;
    for (T element : list) {
      System.out.println(String.format("\t%d) %s", i, element.toString()));
      i++;
    }
  }
}
