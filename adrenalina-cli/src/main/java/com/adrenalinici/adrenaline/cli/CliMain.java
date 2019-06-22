package com.adrenalinici.adrenaline.cli;

import com.adrenalinici.adrenaline.client.ClientNetworkAdapter;
import com.adrenalinici.adrenaline.client.rmi.RmiClientNetworkAdapter;
import com.adrenalinici.adrenaline.client.socket.SocketClientNetworkAdapter;
import com.adrenalinici.adrenaline.common.model.*;
import com.adrenalinici.adrenaline.common.model.event.ModelEvent;
import com.adrenalinici.adrenaline.common.model.light.LightGameModel;
import com.adrenalinici.adrenaline.common.network.outbox.InfoType;
import com.adrenalinici.adrenaline.common.view.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

  public static final Map<String, String> COLORS = new HashMap<String, String>() {{
    put("RED", "\u001B[31m");
    put("GREEN", "\u001B[32m");
    put("CYAN", "\u001B[36m");
    put("YELLOW", "\u001B[33m");
    put("PURPLE", "\u001B[35m");
    put("GRAY", "\u001B[30;1m");
    put("RESET", "\u001B[0m");
  }};

  public static final String SMALL = "SMALL";
  public static final String MEDIUM_1 = "MEDIUM_1";
  public static final String MEDIUM_2 = "MEDIUM_2";
  public static final String BIG = "BIG";

  public static final List<PlayerColor> THREE_PLAYERS = Arrays.asList(
    PlayerColor.GREEN,
    PlayerColor.YELLOW,
    PlayerColor.PURPLE
  );

  public static final List<PlayerColor> FOUR_PLAYERS = Arrays.asList(
    PlayerColor.GREEN,
    PlayerColor.YELLOW,
    PlayerColor.PURPLE,
    PlayerColor.GRAY
  );

  public static final List<PlayerColor> FIVE_PLAYERS = Arrays.asList(
    PlayerColor.GREEN,
    PlayerColor.YELLOW,
    PlayerColor.PURPLE,
    PlayerColor.GRAY,
    PlayerColor.CYAN
  );

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
      int chosenIndex;
      if (matches.isEmpty()) {
        System.out.println("No existing matches available, so let's start a new match!\n\tChoose a name for your match:");
        newMatchManager();
      } else {
        System.out.println("Available matches");
        matches.forEach((matchId, availablePlayers) ->
          System.out.println(String.format("Match %s: %s", matchId, availablePlayers.stream().map(Objects::toString).collect(Collectors.joining(", "))))
        );
        System.out.println("Type name of the match you wanna join, or 'new' to create a new one");
        String choice = scanner.nextLine().trim().toLowerCase();
        if (choice.equals("new")) {
          newMatchManager();
        } else {
          while (!matches.keySet().contains(choice)) {//choice non è uguale ad uno degli id dei match
            System.out.println("Please select an existing match, or type 'new' to create one:");
            choice = scanner.nextLine().trim().toLowerCase();
          }
          System.out.println("Now choose your player:");
          List<PlayerColor> availablePlayers = new ArrayList<>(matches.get(choice));
          printNumberedList(availablePlayers);
          chosenIndex = parseIndex(0, availablePlayers.size() - 1);
          sendChosenMatch(choice, availablePlayers.get(chosenIndex));
        }
      }
    }
  }

  private void newMatchManager() {
    int chosenIndex;
    System.out.println("Choose a name for your match:");
    String matchId = scanner.nextLine();
    System.out.println("Now choose one of the dashboards:");
    List<DashboardChoice> dashboardChoices = Arrays.asList(DashboardChoice.values());
    printNumberedList(Arrays.asList(DashboardChoice.values()));
    chosenIndex = parseIndex(0, dashboardChoices.size() - 1);
    DashboardChoice dashboard = dashboardChoices.get(chosenIndex);
    System.out.println("Now select how many player you want in the match:");
    List<PlayersChoice> playersChoices = Arrays.asList(PlayersChoice.values());
    printNumberedList(playersChoices);
    chosenIndex = parseIndex(0, playersChoices.size() - 1);
    PlayersChoice players = playersChoices.get(chosenIndex);
    System.out.println("Last thing, choose the ruleset:");
    List<RulesChoice> rulesChoices = Arrays.asList(RulesChoice.values());
    printNumberedList(rulesChoices);
    chosenIndex = parseIndex(0, rulesChoices.size() - 1);
    RulesChoice rules = rulesChoices.get(chosenIndex);
    sendStartNewMatch(matchId, dashboard, players, rules);
  }

  @Override
  public void showAvailableActions(List<Action> actions) {
    if (isMyTurn()) {
      System.out.println("Available actions:");
      printNumberedList(actions);

      int chosenIndex = parseIndex(-3, actions.size() - 1);

      sendViewEvent(new ActionChosenEvent(actions.get(chosenIndex)));
    }
  }

  @Override
  public void showAvailableMovements(List<Position> positions) {
    if (isMyTurn()) {
      System.out.println("Available movements:");
      printNumberedList(positions);

      int chosenIndex = parseIndex(-1, positions.size() - 1);
      sendViewEvent(new MovementChosenEvent(chosenIndex != -1 ? positions.get(chosenIndex) : null));
    }
  }

  @Override
  public void showAvailableEnemyMovements(List<Position> positions) {
    if (isMyTurn()) {
      System.out.println("You can move the enemy in these positions:");
      printNumberedList(positions);

      int chosenIndex = parseIndex(-1, positions.size() - 1);
      sendViewEvent(new EnemyMovementChosenEvent(chosenIndex != -1 ? positions.get(chosenIndex) : null));
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

      int chosenIndex = parseIndex(-1, gunsList.size() - 1);
      sendViewEvent(new GunChosenEvent(chosenIndex != -1 ? gunsList.get(chosenIndex) : null));
    }
  }

  @Override
  public void showAvailablePowerUpCardsForRespawn(PlayerColor player, List<PowerUpCard> powerUpCards) {
    if (player.equals(getMyPlayer())) {
      System.out.println("Available power up cards:");
      printNumberedList(powerUpCards);

      int chosenIndex = parseIndex(-3, powerUpCards.size() - 1);

      System.out.println("Chosen Power up card: " + powerUpCards.get(chosenIndex));
      sendViewEvent(new PowerUpCardChosenEvent(getMyPlayer(), powerUpCards.get(chosenIndex)));
    }
  }

  @Override
  public void showAvailableAlternativeEffectsGun(Effect firstEffect, Effect secondEffect) {
    if (isMyTurn()) {
      System.out.println(String.format("0) %s\n\tDescription: %s", firstEffect.getName(), firstEffect.getDescription()));
      System.out.println(String.format("1) %s\n\tDescription: %s", secondEffect.getName(), secondEffect.getDescription()));

      int chosenIndex = parseIndex(-3, 1);

      sendViewEvent(new AlternativeGunEffectChosenEvent(chosenIndex == 1));
    }
  }

  @Override
  public void showChoosePlayerToHit(List<PlayerColor> players) {
    if (isMyTurn()) {
      System.out.println("Available players to hit:");
      printNumberedList(players);

      int chosenIndex = parseIndex(-3, players.size() - 1);
      sendViewEvent(new PlayerChosenEvent(chosenIndex != -1 ? players.get(chosenIndex) : null));
    }
  }

  @Override
  public void showAvailableExtraEffects(Effect firstExtraEffect, Effect secondExtraEffect) {
    if (isMyTurn()) {
      String firstChoice = "n";
      String secondChoice = "n";
      System.out.println("Available extra effects:");

      if (firstExtraEffect != null) {
        System.out.println("First extra effect: " + firstExtraEffect.getId());
        System.out.println("Wanna use it? Type y or n");
        firstChoice = scanner.nextLine();
        while (!firstChoice.equals("y") && !firstChoice.equals("n")) {
          System.out.println("That's not a valid input, please retry with y or n");
          firstChoice = scanner.nextLine();
        }
      }

      if (secondExtraEffect != null) {
        System.out.println("Second extra effect: " + secondExtraEffect.getId());
        System.out.println("Wanna use it? Type y or n");
        secondChoice = scanner.nextLine();
        while (!secondChoice.equals("y") && !secondChoice.equals("n")) {
          System.out.println("That's not a valid input, please retry with y or n");
          secondChoice = scanner.nextLine();
        }
      }

      sendViewEvent(new BaseGunEffectChosenEvent(firstChoice.equals("y"), secondChoice.equals("y")));

    }
  }

  @Override
  public void showAvailableGuns(Set<String> guns) {
    if (isMyTurn()) {
      List<String> gunsList = new ArrayList<>(guns);

      System.out.println("Loaded guns:");
      printNumberedList(gunsList);

      int chosenIndex = parseIndex(-3, gunsList.size() - 1);
      sendViewEvent(new GunChosenEvent(chosenIndex != -1 ? gunsList.get(chosenIndex) : null));
    }
  }

  @Override
  public void showAvailableGunsToPickup(Set<String> guns) {
    if (isMyTurn()) {
      List<String> gunsList = new ArrayList<>(guns);

      System.out.println("Available guns to pickup:");
      printNumberedList(gunsList);

      int chosenIndex = parseIndex(-1, gunsList.size() - 1);
      sendViewEvent(new GunChosenEvent(chosenIndex != -1 ? gunsList.get(chosenIndex) : null));
    }
  }

  @Override
  public void showAvailableTagbackGrenade(PlayerColor player, List<PowerUpCard> powerUpCards) {
    if (player.equals(getMyPlayer())) {
      System.out.println("Available tagback granades:");
      printNumberedList(powerUpCards);

      int chosenIndex = parseIndex(-1, powerUpCards.size() - 1);
      sendViewEvent(
        new UseTagbackGrenadeEvent(getMyPlayer(), chosenIndex != -1 ? powerUpCards.get(chosenIndex) : null)
      );
    }
  }

  @Override
  public void showAvailableRooms(Set<CellColor> rooms) {
    if (isMyTurn()) {
      List<CellColor> roomsList = new ArrayList<>(rooms);

      System.out.println("Available rooms to hit:");
      printNumberedList(roomsList);

      int chosenIndex = parseIndex(-1, roomsList.size() - 1);

      sendViewEvent(new RoomChosenEvent(chosenIndex != -1 ? roomsList.get(chosenIndex) : null));
    }
  }

  @Override
  public void showAvailableCellsToHit(Set<Position> cells) {
    if (isMyTurn()) {
      List<Position> cellsList = new ArrayList<>(cells);

      System.out.println("Available cells to hit:");
      printNumberedList(cellsList);

      int chosenIndex = parseIndex(-1, cellsList.size() - 1);

      sendViewEvent(new CellToHitChosenEvent(chosenIndex != -1 ? cellsList.get(chosenIndex) : null));
    }
  }

  @Override
  public void showRanking(List<Map.Entry<PlayerColor, Integer>> ranking) {

  }

  @Override
  public void onEvent(ModelEvent newValue) {
    //System.out.println("Model updated! " + newValue);
    //here should be used buffered console out
    showModel(newValue.getGameModel());
    BufferedConsoleOut.OUT.println(ANSI_PURPLE + "ehilà campione\n\n" + ANSI_RESET);
    BufferedConsoleOut.OUT.flush();
  }

  private void showModel(LightGameModel model) {
    Map<Position, List<PlayerColor>> playersMap = new HashMap<>();
    model.getDashboard().stream().filter(Objects::nonNull).forEach(dc -> {
      playersMap.put(Position.of(dc.getLine(), dc.getCell()), dc.getPlayersInCell());
    });

    DashboardChoice dashboardChoice = model.getDashboard().getDashboardChoice();

    switch (dashboardChoice) {
      case SMALL:
        PrintUtils.printSmallDashboard(playersMap);
        break;
      case MEDIUM_1:
        PrintUtils.printMedium1Dashboard(playersMap);
        break;
      case MEDIUM_2:
        PrintUtils.printMedium2Dashboard(playersMap);
        break;
      case BIG:
        PrintUtils.printBigDashboard(playersMap);
        break;

    }

    System.out.println("Remaining skulls: " + model.getRemainingSkulls());
    System.out.println("Kill track: " + model.getKillScore().toString());

    model.getPlayerDashboards()
      .stream()
      .filter(pd -> !pd.getPlayer().equals(getMyPlayer()))
      .forEach(pd -> {
        System.out.println(COLORS.get(pd.getPlayer().name()) + pd.getPlayer() + " -> DAMAGES: " + pd.getDamages().toString());
        System.out.println("\t  MARKS: " + pd.getMarks().toString());
        System.out.println("\t  NUMBER OF DEATHS: " + pd.getSkullsNumber());
      });

    model.getPlayerDashboards()
      .stream()
      .filter(pd -> pd.getPlayer().equals(getMyPlayer()))
      .findFirst()
      .ifPresent(myPD -> {
        System.out.println(COLORS.get(myPD.getPlayer().name()) + "MY STATUS -> DAMAGES: " + myPD.getDamages().toString());
        System.out.println("\t  MARKS: " + myPD.getMarks().toString());
        System.out.println("\t  AMMOS: " + myPD.getAmmos().toString());
        System.out.println("\t  LOADED GUNS: " + myPD.getLoadedGuns().toString());
        System.out.println("\t  UNLOADED GUNS: " + myPD.getUnloadedGuns().toString());
        System.out.println("\t  POWERUPS: ");
        myPD.getPowerUpCards()
          .forEach(puc -> System.out.println(String.format("\t\t - %s (%s)", puc.getPowerUpType(), puc.getAmmoColor())));
        System.out.println("\t  NUMBER OF DEATHS: " + myPD.getSkullsNumber());
        System.out.println("\t  POINTS: " + myPD.getPoints());
      });
    //devo aggiungere anche le robe da printare, magari differenziandole anche in base al turnOfPlayer
    //printo tipo le ammo rimanenti e le armi cariche/scariche solo per il player in corso
  }

  private int parseIndex(int minimum, int maximum) {
    int chosenIndex = -2;
    String command = scanner.nextLine();
    //potrei gestire qui il parsing dei comandi che attivano teleporter o kinetic ray
    //magari sulla nextline provo a vedere se becco uno dei due comandi 'teleporter' o 'kinetic'
    //se true allora in base a quale dei due faccio restituire -2 o -3 e questi li vado a gestire
    //in tutti i metodi show


    //qui piazzo l'if per il controllo sul minimum xD
    if (command.trim().toLowerCase().equals("teleporter")) return -2;
    if (command.trim().toLowerCase().equals("kinetic")) return -3;

    while (!(chosenIndex >= -1 && chosenIndex <= maximum)) {
      try {
        //chosenIndex = Integer.parseInt(scanner.nextLine());
        chosenIndex = Integer.parseInt(command);
        if (!(chosenIndex >= -1 && chosenIndex <= maximum))
          System.out.println(String.format("Please try again, Insert an index between 0 and %d:", maximum));
      } catch (NumberFormatException e) {
        System.out.println("What you inserted is not an integer, please retry.\nInsert index:");
      }
      command = scanner.nextLine();
    }

    return chosenIndex;
  }

  private <T> void printNumberedList(List<T> list) {
    IntStream.range(0, list.size())
      .forEach(i -> System.out.println(String.format("%d) %s", i, list.get(i).toString().toUpperCase())));
    System.out.println("Insert index of your selection, or -1 if you wanna skip:");
  }
}
