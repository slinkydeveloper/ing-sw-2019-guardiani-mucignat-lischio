package com.adrenalinici.adrenaline.server.controller.nodes;

public enum ControllerNodes {

  FIRST_TURN,
  START_TURN,
  PICKUP,
  RELOAD,
  CHOOSE_ACTION,
  RESPAWN_KILLED_PEOPLE,
  CHOOSE_GUN,
  CHOOSE_PLAYER_TO_HIT,
  ALTERNATIVE_GUN_START,
  BASE_GUN_START,
  USE_TAGBACK_GRENADE,
  USE_SCOPE,
  CHOOSE_ROOM,
  CHOOSE_CELL,
  APPLY_GRENADE_LAUNCHER,
  ALTERNATIVE_GUN_MOVEMENT,
  APPLY_TELEPORT,
  APPLY_NEWTON;

  public static String movement(int distance) {
    return "movement_" + distance;
  }

  public static String gunMovement(int distance) {
    return "gun_movement_" + distance;
  }

  public static String gunEnemyMovement(int distance) {
    return "gun_enemy_movement_" + distance;
  }

  public static String applyGunEffect(String gunId, String effectId) {
    return "apply_gun_effect_" + gunId + "_" + effectId;
  }

}
