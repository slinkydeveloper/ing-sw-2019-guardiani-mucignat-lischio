package com.adrenalinici.adrenaline.cli;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintUtils {
  public static final String RED = "RED";
  public static final String GREEN = "GREEN";
  public static final String CYAN = "CYAN";
  public static final String YELLOW = "YELLOW";
  public static final String PURPLE = "PURPLE";
  public static final String GRAY = "GRAY";
  public static final String RESET = "RESET";

  public static final String BOLD = "\u001b[1m";
  public static final String TOP = "═════════";
  public static final String HORIZ_WALL = "═════════";
  public static final String SPACES = "         "; //9 spaces
  public static final String VERT_WALL = "          ║";
  public static final String HORIZ_DOOR = "──     ──";

  public static final Map<String, String> COLORS = new HashMap<String, String>() {{
    put(RED, "\u001B[31m");
    put(GREEN, "\u001B[32m");
    put(CYAN, "\u001B[36m");
    put(YELLOW, "\u001B[33m");
    put(PURPLE, "\u001B[35m");
    put(GRAY, "\u001B[30;1m");
    put(RESET, "\u001B[0m");
  }};

  /*public static final Map<Position, List<PlayerColor>> playersMap = new HashMap<Position, List<PlayerColor>>() {{
    put(Position.of(0, 0), Arrays.asList(PlayerColor.CYAN, PlayerColor.YELLOW));
    put(Position.of(0, 1), Arrays.asList(PlayerColor.GREEN, PlayerColor.CYAN, PlayerColor.YELLOW));
    put(Position.of(0, 2), Collections.singletonList(PlayerColor.PURPLE));
    put(Position.of(0, 3), Arrays.asList(PlayerColor.GREEN, PlayerColor.CYAN, PlayerColor.YELLOW));
    put(Position.of(1, 0), Arrays.asList(PlayerColor.CYAN, PlayerColor.YELLOW));
    put(Position.of(1, 1), Arrays.asList(PlayerColor.GREEN, PlayerColor.CYAN, PlayerColor.YELLOW, PlayerColor.PURPLE, PlayerColor.GRAY));
    put(Position.of(1, 2), Collections.singletonList(PlayerColor.PURPLE));
    put(Position.of(1, 3), Collections.singletonList(PlayerColor.PURPLE));
    put(Position.of(2, 0), Arrays.asList(PlayerColor.CYAN, PlayerColor.YELLOW));
    put(Position.of(2, 1), Arrays.asList(PlayerColor.GREEN, PlayerColor.CYAN, PlayerColor.YELLOW));
    put(Position.of(2, 2), Collections.singletonList(PlayerColor.PURPLE));
    put(Position.of(2, 3), Arrays.asList(PlayerColor.GREEN, PlayerColor.CYAN, PlayerColor.YELLOW));

  }};

  public static void main(String[] args) {
    printSmallDashboard(playersMap);
    printMedium1Dashboard(playersMap);
    printMedium2Dashboard(playersMap);
    printBigDashboard(playersMap);
  }*/

  public static void printSmallDashboard(Map<Position, List<PlayerColor>> playersMap) {
    //first line
    System.out.println(COLORS.get(CYAN) + "╔" + TOP + "╤" + TOP + "╤" + TOP + "╗");

    System.out.println("║ " + SPACES + SPACES + SPACES + " ║");
    System.out.println("║ " + players(playersMap.get(Position.of(0, 0))) +
      players(playersMap.get(Position.of(0, 1))) + " " +
      players(playersMap.get(Position.of(0, 2))) + COLORS.get(CYAN) + "║");

    System.out.println("║ " + SPACES + SPACES + SPACES + " ║");

    System.out.println("╙" + HORIZ_DOOR + "╧" + HORIZ_WALL + "╧" + HORIZ_DOOR + "╜");

    //second line
    System.out.println(COLORS.get(RED) + "╓" + HORIZ_DOOR + "╤" + HORIZ_WALL + "╤" + HORIZ_DOOR + "╤" +
      COLORS.get(YELLOW) + TOP + "╗" + COLORS.get(RED));

    System.out.println("║" + SPACES + SPACES + SPACES + COLORS.get(YELLOW) + SPACES + "   ║" + COLORS.get(RED));

    System.out.println("║ " +
      players(playersMap.get(Position.of(1, 0))) +
      players(playersMap.get(Position.of(1, 1))) + "  " +
      players(playersMap.get(Position.of(1, 2))) +
      players(playersMap.get(Position.of(1, 3))) +
      COLORS.get(YELLOW) + "║" + COLORS.get(RED));

    System.out.println("║ " + SPACES + SPACES + SPACES + COLORS.get(YELLOW) + SPACES + "  ║" + COLORS.get(RED));

    System.out.println("╚" + TOP + "╧" + HORIZ_DOOR + "╧" + HORIZ_WALL + "╧" + SPACES + COLORS.get(YELLOW) + "╜");

    //third line
    System.out.println(SPACES + COLORS.get(GRAY) + " ╓" + HORIZ_DOOR + "╤" + HORIZ_WALL + "╤" + COLORS.get(YELLOW) + SPACES + "╖");

    System.out.println(SPACES + COLORS.get(GRAY) + " ║ " + SPACES + SPACES + SPACES + COLORS.get(YELLOW) + " ║");

    System.out.println(SPACES + COLORS.get(GRAY) + " ║ " +
      players(playersMap.get(Position.of(2, 1))) +
      players(playersMap.get(Position.of(2, 2))) + " " +
      players(playersMap.get(Position.of(2, 3))) +
      COLORS.get(YELLOW) + "║");

    System.out.println(SPACES + COLORS.get(GRAY) + " ║ " + SPACES + SPACES + SPACES + COLORS.get(YELLOW) + " ║");

    System.out.println(SPACES + COLORS.get(GRAY) + " ╚" + TOP + "╧" + TOP + "╧" + COLORS.get(YELLOW) + TOP + "╝");

    System.out.println(COLORS.get(RESET));
  }

  public static void printMedium1Dashboard(Map<Position, List<PlayerColor>> playersMap) {
    //first line
    System.out.println(COLORS.get(CYAN) + "╔" + TOP + "╤" + TOP + "╤" + TOP + "╤" + COLORS.get(GREEN) + TOP + "╗");
    System.out.println(COLORS.get(CYAN) + "║ " + SPACES + SPACES + SPACES + SPACES + COLORS.get(GREEN) + "  ║");
    System.out.println(COLORS.get(CYAN) + "║ " + players(playersMap.get(Position.of(0, 0))) +
      players(playersMap.get(Position.of(0, 1))) + "  " +
      players(playersMap.get(Position.of(0, 2))) +
      players(playersMap.get(Position.of(0, 3))) +
      COLORS.get(GREEN) + "║");

    System.out.println(COLORS.get(CYAN) + "║ " + SPACES + SPACES + SPACES + SPACES + COLORS.get(GREEN) + "  ║");

    System.out.println(COLORS.get(CYAN) + "╙" + HORIZ_DOOR + "╧" + HORIZ_WALL + "╧" + HORIZ_DOOR + "╧" + COLORS.get(GREEN) + HORIZ_DOOR + "╜");

    //second line
    System.out.println(COLORS.get(RED) + "╓" + HORIZ_DOOR + "╤" + HORIZ_WALL + "╤" + COLORS.get(YELLOW) + HORIZ_DOOR + "╤" +
      HORIZ_DOOR + "╗" + COLORS.get(RED));

    System.out.println("║" + SPACES + VERT_WALL + SPACES + COLORS.get(YELLOW) + SPACES + " ║" + COLORS.get(RED));

    System.out.println("║ " + players(playersMap.get(Position.of(1, 0))) +
      players(playersMap.get(Position.of(1, 1))) + COLORS.get(RED) + "║" +
      players(playersMap.get(Position.of(1, 2))) + " " +
      players(playersMap.get(Position.of(1, 3))) +
      COLORS.get(YELLOW) + "║" + COLORS.get(RED));

    System.out.println("║" + SPACES + VERT_WALL + SPACES + COLORS.get(YELLOW) + SPACES + " ║" + COLORS.get(RED));

    System.out.println("╚" + TOP + "╧" + HORIZ_DOOR + "╧" + COLORS.get(YELLOW) + SPACES + "╧" + SPACES + "╜");

    //third line
    System.out.println(SPACES + COLORS.get(GRAY) + " ╓" + HORIZ_DOOR + "╤" + COLORS.get(YELLOW) + SPACES + "╤" + SPACES + "╖");

    System.out.println(SPACES + COLORS.get(GRAY) + " ║ " + SPACES + SPACES + SPACES + COLORS.get(YELLOW) + " ║");

    System.out.println(SPACES + COLORS.get(GRAY) + " ║" +
      players(playersMap.get(Position.of(2, 1))) + "  " +
      players(playersMap.get(Position.of(2, 2))) +
      players(playersMap.get(Position.of(2, 3))) +
      COLORS.get(YELLOW) + "║");

    System.out.println(SPACES + COLORS.get(GRAY) + " ║ " + SPACES + SPACES + SPACES + COLORS.get(YELLOW) + " ║");

    System.out.println(SPACES + COLORS.get(GRAY) + " ╚" + TOP + "╧" + COLORS.get(YELLOW) + TOP + "╧" + TOP + "╝");

    System.out.println(COLORS.get(RESET));
  }

  public static void printMedium2Dashboard(Map<Position, List<PlayerColor>> playersMap) {
    //first line
    System.out.println(COLORS.get(RED) + "╔" + TOP + "╤" + COLORS.get(CYAN) + TOP + "╤" + TOP + "╗");
    System.out.println(COLORS.get(RED) + "║ " + SPACES + SPACES + SPACES + COLORS.get(CYAN) + " ║");
    System.out.println(COLORS.get(RED) + "║ " + players(playersMap.get(Position.of(0, 0))) +
      players(playersMap.get(Position.of(0, 1))) + " " +
      players(playersMap.get(Position.of(0, 2))) + COLORS.get(CYAN) + "║");

    System.out.println(COLORS.get(RED) + "║ " + SPACES + SPACES + SPACES + COLORS.get(CYAN) + " ║");
    System.out.println(COLORS.get(RED) + "╙" + SPACES + "╧" + COLORS.get(CYAN) + HORIZ_DOOR + "╧" + HORIZ_DOOR + "╜");

    //second line
    System.out.println(COLORS.get(RED) + "╓" + SPACES + "╤" + COLORS.get(PURPLE) + HORIZ_DOOR + "╤" + HORIZ_DOOR + "╤" +
      COLORS.get(YELLOW) + TOP + "╗" + COLORS.get(RED));

    System.out.println("║" + SPACES + SPACES + SPACES + SPACES + COLORS.get(YELLOW) + "   ║" + COLORS.get(RED));

    System.out.println("║ " +
      players(playersMap.get(Position.of(1, 0))) +
      players(playersMap.get(Position.of(1, 1))) + "  " +
      players(playersMap.get(Position.of(1, 2))) +
      players(playersMap.get(Position.of(1, 3))) +
      COLORS.get(YELLOW) + "║" + COLORS.get(RED));

    System.out.println("║ " + SPACES + SPACES + SPACES + SPACES + COLORS.get(YELLOW) + "  ║" + COLORS.get(RED));

    System.out.println("╙" + HORIZ_DOOR + "╧" + COLORS.get(PURPLE) + HORIZ_DOOR + "╧" + HORIZ_WALL + "╧" + SPACES + COLORS.get(YELLOW) + "╜");

    //third line
    System.out.println(COLORS.get(GRAY) + "╓" + HORIZ_DOOR + "╤" + HORIZ_DOOR + "╤" + HORIZ_WALL + "╤" + COLORS.get(YELLOW) + SPACES + "╖");
    System.out.println(COLORS.get(GRAY) + "║ " + SPACES + SPACES + SPACES + SPACES + COLORS.get(YELLOW) + "  ║");

    System.out.println(COLORS.get(GRAY) + "║" +
      players(playersMap.get(Position.of(2, 0))) + " " +
      players(playersMap.get(Position.of(2, 1))) + "  " +
      players(playersMap.get(Position.of(2, 2))) +
      players(playersMap.get(Position.of(2, 3))) +
      COLORS.get(YELLOW) + "║");

    System.out.println(COLORS.get(GRAY) + "║ " + SPACES + SPACES + SPACES + SPACES + COLORS.get(YELLOW) + "  ║");
    System.out.println(COLORS.get(GRAY) + "╚" + TOP + "╧" + TOP + "╧" + TOP + "╧" + COLORS.get(YELLOW) + TOP + "╝");

    System.out.println(COLORS.get(RESET));
  }

  public static void printBigDashboard(Map<Position, List<PlayerColor>> playersMap) {
    //first line
    System.out.println(COLORS.get(RED) + "╔" + TOP + "╤" + COLORS.get(CYAN) + TOP + "╤" + TOP + "╤" + COLORS.get(GREEN) + TOP + "╗");
    System.out.println(COLORS.get(RED) + "║ " + SPACES + SPACES + SPACES + SPACES + COLORS.get(GREEN) + "  ║");
    System.out.println(COLORS.get(RED) + "║ " + players(playersMap.get(Position.of(0, 0))) +
      players(playersMap.get(Position.of(0, 1))) + "  " +
      players(playersMap.get(Position.of(0, 2))) +
      players(playersMap.get(Position.of(0, 3))) +
      COLORS.get(GREEN) + "║");

    System.out.println(COLORS.get(RED) + "║ " + SPACES + SPACES + SPACES + SPACES + COLORS.get(GREEN) + "  ║");

    System.out.println(COLORS.get(RED) + "╙" + SPACES + "╧" + COLORS.get(CYAN) + HORIZ_DOOR + "╧" + HORIZ_DOOR + "╧" + COLORS.get(GREEN) + HORIZ_DOOR + "╜");

    //second line
    System.out.println(COLORS.get(RED) + "╓" + SPACES + "╤" + COLORS.get(PURPLE) + HORIZ_DOOR + "╤" + COLORS.get(YELLOW) + HORIZ_DOOR +
      "╤" + TOP + "╗" + COLORS.get(RED));

    System.out.println("║" + "         ║" + COLORS.get(PURPLE) + "         ║" + SPACES + SPACES + COLORS.get(YELLOW) + " ║" + COLORS.get(RED));

    System.out.println("║" +
      players(playersMap.get(Position.of(1, 0))) + COLORS.get(RED) + "║" +
      players(playersMap.get(Position.of(1, 1))) + COLORS.get(PURPLE) + "║" +
      players(playersMap.get(Position.of(1, 2))) +
      players(playersMap.get(Position.of(1, 3))) +
      COLORS.get(YELLOW) + " ║" + COLORS.get(RED));

    System.out.println("║" + "         ║" + COLORS.get(PURPLE) + "         ║" + SPACES + SPACES + COLORS.get(YELLOW) + " ║" + COLORS.get(RED));

    System.out.println("╙" + HORIZ_DOOR + "╧" + COLORS.get(PURPLE) + HORIZ_DOOR + "╧" + COLORS.get(YELLOW) + SPACES + "╧" + SPACES + "╜");


    //third line
    System.out.println(COLORS.get(GRAY) + "╓" + HORIZ_DOOR + "╤" + HORIZ_DOOR + "╤" + COLORS.get(YELLOW) + SPACES + "╤" + SPACES + "╖");

    System.out.println(COLORS.get(GRAY) + "║ " + SPACES + SPACES + SPACES + SPACES + COLORS.get(YELLOW) + "  ║");

    System.out.println(COLORS.get(GRAY) + "║" +
      players(playersMap.get(Position.of(2, 0))) + " " +
      players(playersMap.get(Position.of(2, 1))) + " " +
      players(playersMap.get(Position.of(2, 2))) + " " +
      players(playersMap.get(Position.of(2, 3))) +
      COLORS.get(YELLOW) + "║");

    System.out.println(COLORS.get(GRAY) + "║ " + SPACES + SPACES + SPACES + SPACES + COLORS.get(YELLOW) + "  ║");

    System.out.println(COLORS.get(GRAY) + "╚" + TOP + "╧" + TOP + "╧" + COLORS.get(YELLOW) + TOP + "╧" + TOP + "╝");

    System.out.println(COLORS.get(RESET));
  }

  public static String players(List<PlayerColor> players) {
    String out = "";

    if (players != null) {
      for (PlayerColor p : players) {
        out = out.concat(COLORS.get(p.name()) + BOLD + "0" + COLORS.get(RESET));
      }

      for (int i = 0; i < 9 - players.size(); i++) {
        out = out.concat(" ");
      }
    } else out = SPACES;

    return out;
  }
}
