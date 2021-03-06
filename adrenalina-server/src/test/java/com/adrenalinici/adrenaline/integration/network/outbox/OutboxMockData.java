package com.adrenalinici.adrenaline.integration.network.outbox;

import com.adrenalinici.adrenaline.common.model.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OutboxMockData {

  public static final List<Action> ACTIONS = Arrays.asList(Action.MOVE_MOVE_MOVE, Action.SHOOT, Action.MOVE_PICKUP);
  public static final List<Position> POSITIONS = Arrays.asList(Position.of(2, 3), Position.of(1, 4));
  public static final PlayerColor PLAYER = PlayerColor.GREEN;
  public static final Set<String> GUNS = new HashSet<>(Arrays.asList("zx2", "machine_gun"));
  public static final Effect FIRST_EFFECT = new Effect("aaa", "aaa");
  public static final Effect SECOND_EFFECT = new Effect("bbb", "aaa");
  public static final Effect FIRST_EXTRA_EFFECT = FIRST_EFFECT;
  public static final Effect SECOND_EXTRA_EFFECT = SECOND_EFFECT;
  public static final List<PowerUpCard> POWER_UP_CARDS = Arrays.asList(new PowerUpCard(AmmoColor.RED, PowerUpType.TAGBACK_GRENADE), new PowerUpCard(AmmoColor.BLUE, PowerUpType.NEWTON));
  public static final List<PlayerColor> PLAYERS = Arrays.asList(PlayerColor.YELLOW, PlayerColor.GREEN);
  public static final Set<CellColor> ROOMS = new HashSet<>(Arrays.asList(CellColor.YELLOW, CellColor.GREEN));
  public static final Set<Position> CELLS = new HashSet<>(Arrays.asList(Position.of(0, 0), Position.of(0, 1)));
}
