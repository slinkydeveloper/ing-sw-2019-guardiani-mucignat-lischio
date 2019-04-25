package com.adrenalinici.adrenaline.controller.nodes;

public enum ControllerNodes {

  START_TURN,
  PICKUP,
  RELOAD,
  CHOOSE_ACTION,
  RESPAWN_KILLED_PEOPLE,
  CHOOSE_GUN,
  CHOOSE_PLAYER_TO_HIT,
  ALTERNATIVE_GUN_START;

  public static String movement(int distance) {
    return "movement_" + distance;
  }

  public static String applyGunEffect(String gunId) {
    return "apply_gun_effect_" + gunId;
  }

  public static String applyGunEffect(String gunId, String effectId) {
    return "apply_gun_effect_" + gunId + "_" + effectId;
  }

}
