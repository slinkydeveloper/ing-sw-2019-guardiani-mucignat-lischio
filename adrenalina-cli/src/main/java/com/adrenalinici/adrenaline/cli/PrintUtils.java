package com.adrenalinici.adrenaline.cli;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.model.light.LightDashboardCell;

import java.util.*;

public class PrintUtils {
  public static final Map<String, String> COLORS = new HashMap<String, String>() {{
    put("RED", "\u001B[31m");
    put("GREEN", "\u001B[32m");
    put("CYAN", "\u001B[36m");
    put("YELLOW", "\u001B[33m");
    put("PURPLE", "\u001B[35m");
    put("GRAY", "\u001B[30;1m");
    put("RESET", "\u001B[0m");
  }};

  public static final String BOLD = "\u001b[1m";

  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_BLACK = "\u001B[30m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_YELLOW = "\u001B[33m";
  public static final String ANSI_BLUE = "\u001B[34m";
  public static final String ANSI_PURPLE = "\u001B[35m";
  public static final String ANSI_CYAN = "\u001B[36m";
  public static final String ANSI_WHITE = "\u001B[37m";

  public static final String TOP = "═════════";
  public static final String HORIZ_WALL = "─────────";
  public static final String SPACES = "         "; //9 spaces
  public static final String VERT_WALL = "          |";

  public static final String HORIZ_DOOR = "──     ──";

  public static final Map<Position, List<PlayerColor>> playersMap = new HashMap<Position, List<PlayerColor>>() {{
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

  }};

  public static void main(String[] args) {
    printSmallDashboard(playersMap);
    printMedium1Dashboard(playersMap);
  }

  public static void printSmallDashboard(Map<Position, List<PlayerColor>> playersMap) {
    //first line
    System.out.println(COLORS.get("CYAN") + "╔" + TOP + "╤" + TOP + "╤" + TOP + "╗");

    System.out.println("║ " + SPACES + SPACES + SPACES + " ║");
    System.out.println("║ " + players(playersMap.get(Position.of(0, 0))) +
      players(playersMap.get(Position.of(0, 1))) + " " +
      players(playersMap.get(Position.of(0, 2))) + COLORS.get("CYAN") + "║");

    System.out.println("║ " + SPACES + SPACES + SPACES + " ║");

    System.out.println("╙" + HORIZ_DOOR + "╧" + HORIZ_WALL + "╧" + HORIZ_DOOR + "╜");

    //second line
    System.out.println(COLORS.get("RED") + "╓" + HORIZ_DOOR + "╤" + HORIZ_WALL + "╤" + COLORS.get("PURPLE") + HORIZ_DOOR + "╤" +
      COLORS.get("YELLOW") + TOP + "╗" + COLORS.get("RED"));

    System.out.println("║" + SPACES + SPACES + SPACES + COLORS.get("YELLOW") + SPACES + "   ║" + COLORS.get("RED"));

    System.out.println("║ " + players(playersMap.get(Position.of(1, 0))) +
      players(playersMap.get(Position.of(1, 1))) + "  " +
      players(playersMap.get(Position.of(1, 2))) +
      players(playersMap.get(Position.of(1, 3))) +
      COLORS.get("YELLOW") + "║" + COLORS.get("RED"));

    System.out.println("║ " + SPACES + SPACES + SPACES + COLORS.get("YELLOW") + SPACES + "  ║" + COLORS.get("RED"));

    System.out.println("╚" + TOP + "╧" + HORIZ_DOOR + "╧" + COLORS.get("PURPLE") + HORIZ_WALL + "╧" + SPACES + COLORS.get("YELLOW") + "╜");

    //third line
    System.out.println(SPACES + COLORS.get("GRAY") + " ╓" + HORIZ_DOOR + "╤" + HORIZ_WALL + "╤" + COLORS.get("YELLOW") + SPACES + "╖");

    System.out.println(SPACES + COLORS.get("GRAY") + " ║ " + SPACES + SPACES + SPACES + COLORS.get("YELLOW") + " ║");

    System.out.println(SPACES + COLORS.get("GRAY") + " ║ " +
      players(playersMap.get(Position.of(2, 0))) +
      players(playersMap.get(Position.of(2, 1))) + " " +
      players(playersMap.get(Position.of(2, 2))) +
      COLORS.get("YELLOW") + "║");

    System.out.println(SPACES + COLORS.get("GRAY") + " ║ " + SPACES + SPACES + SPACES + COLORS.get("YELLOW") + " ║");

    System.out.println(SPACES + COLORS.get("GRAY") + " ╚" + TOP + "╧" + TOP + "╧" + COLORS.get("YELLOW") + TOP + "╝");

    System.out.println(COLORS.get("RESET"));
  }

  public static void printMedium1Dashboard(Map<Position, List<PlayerColor>> playersMap) {
    //first line
    System.out.println(COLORS.get("CYAN") + "╔" + TOP + "╤" + TOP + "╤" + TOP + "╤" + COLORS.get("GREEN") + TOP + "╗");
    System.out.println(COLORS.get("CYAN") + "║ " + SPACES + SPACES + SPACES + SPACES + COLORS.get("GREEN") + "  ║");
    System.out.println(COLORS.get("CYAN") + "║ " + players(playersMap.get(Position.of(0, 0))) +
      players(playersMap.get(Position.of(0, 1))) + "  " +
      players(playersMap.get(Position.of(0, 2))) +
      players(playersMap.get(Position.of(0, 3))) +
      COLORS.get("GREEN") + "║");

    System.out.println(COLORS.get("CYAN") + "║ " + SPACES + SPACES + SPACES + SPACES + COLORS.get("GREEN") + "  ║");

    System.out.println(COLORS.get("CYAN") + "╙" + HORIZ_DOOR + "╧" + HORIZ_WALL + "╧" + HORIZ_DOOR + "╧" + COLORS.get("GREEN") + HORIZ_DOOR + "╜");

    //second line
    System.out.println(COLORS.get("RED") + "╓" + HORIZ_DOOR + "╤" + HORIZ_WALL + "╤" + COLORS.get("YELLOW") + HORIZ_DOOR + "╤" +
      HORIZ_DOOR + "╗" + COLORS.get("RED"));

    System.out.println("║" + SPACES + VERT_WALL + SPACES + COLORS.get("YELLOW") + SPACES + " ║" + COLORS.get("RED"));

    System.out.println("║ " + players(playersMap.get(Position.of(1, 0))) +
      players(playersMap.get(Position.of(1, 1))) + COLORS.get("RED") + "|" +
      players(playersMap.get(Position.of(1, 2))) + " " +
      players(playersMap.get(Position.of(1, 3))) +
      COLORS.get("YELLOW") + "║" + COLORS.get("RED"));

    System.out.println("║" + SPACES + VERT_WALL + SPACES + COLORS.get("YELLOW") + SPACES + " ║" + COLORS.get("RED"));

    System.out.println("╚" + TOP + "╧" + HORIZ_DOOR + COLORS.get("YELLOW") + "╧" + SPACES + "╧" + SPACES + "╜");

    //third line
    System.out.println(SPACES + COLORS.get("GRAY") + " ╓" + HORIZ_DOOR + COLORS.get("YELLOW") + "╤" + SPACES + "╤" + SPACES + "╖");

    System.out.println(SPACES + COLORS.get("GRAY") + " ║ " + SPACES + SPACES + SPACES + COLORS.get("YELLOW") + " ║");

    System.out.println(SPACES + COLORS.get("GRAY") + " ║" +
      players(playersMap.get(Position.of(2, 0))) + "  " +
      players(playersMap.get(Position.of(2, 1))) +
      players(playersMap.get(Position.of(2, 2))) +
      COLORS.get("YELLOW") + "║");

    System.out.println(SPACES + COLORS.get("GRAY") + " ║ " + SPACES + SPACES + SPACES + COLORS.get("YELLOW") + " ║");

    System.out.println(SPACES + COLORS.get("GRAY") + " ╚" + TOP + "╧" + TOP + "╧" + COLORS.get("YELLOW") + TOP + "╝");

    System.out.println(COLORS.get("RESET"));
  }

  public static void printMedium2Dashboard(Map<Position, List<PlayerColor>> playersMap) {
  }

  public static void printBigDashboard(Map<Position, List<PlayerColor>> playersMap) {
  }

  public static void printSingleCell(LightDashboardCell cell) {

  }

  public static String players(List<PlayerColor> players) {
    String out = "";

    for (PlayerColor p : players) {
      out = out.concat(COLORS.get(p.name()) + BOLD + "0" + COLORS.get("RESET"));
    }

    for (int i = 0; i < 9 - players.size(); i++) {
      out = out.concat(" ");
    }

    //players.forEach(p -> out.concat(COLORS.get(p.name()) + "0" + COLORS.get("RESET")));
    //IntStream.range(0, 9 - players.size()).forEach(i -> out.concat(" "));

    return out;
    //players.forEach(p -> System.out.print( COLORS.get(p.name()) + "▒" + COLORS.get("RESET")));
    //IntStream.range(0, 9 - players.size()).forEach(i -> System.out.print(" "));
  }
}
