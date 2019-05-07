package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.*;

import java.util.*;

public class OutboxMockData {

  public static final List<Action> ACTIONS = Arrays.asList(Action.MOVE_MOVE_MOVE, Action.SHOOT, Action.MOVE_PICKUP);
  public static final List<Position> POSITIONS = Arrays.asList(Position.of(2, 3), Position.of(1, 4));
  public static final PlayerColor PLAYER = PlayerColor.GREEN;
  public static final Set<String> GUNS = new HashSet<>(Arrays.asList("zx2", "machine_gun")); //TODO add some data
  public static final Effect FIRST_EFFECT = new Effect("aaa", "aaaaa", "aaaaaaaaa");
  public static final Effect SECOND_EFFECT = new Effect("bbb", "bbbbb", "bbbbbbbbbb");
  public static final Effect FIRST_EXTRA_EFFECT = FIRST_EFFECT;
  public static final Effect SECOND_EXTRA_EFFECT = SECOND_EFFECT;
  public static final List<Effect> EFFECTS = Arrays.asList(FIRST_EFFECT, SECOND_EFFECT);
  public static final List<AmmoColor> AMMO_COLORS = Arrays.asList(AmmoColor.YELLOW, AmmoColor.BLUE);
  public static final List<AmmoColor> RESPAWN_LOCATIONS = AMMO_COLORS;
  public static final List<PlayerColor> PLAYERS = Arrays.asList(PlayerColor.YELLOW, PlayerColor.GREEN);
  public static final Map<PlayerColor, List<Position>> AVAILABLE_MOVEMENTS = new HashMap<>(); //TODO add some data

}
