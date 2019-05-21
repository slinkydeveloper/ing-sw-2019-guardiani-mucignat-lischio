package com.adrenalinici.adrenaline.model.common;

import java.io.Serializable;

public enum CellColor implements Serializable {
  CYAN(AmmoColor.BLUE),
  RED(AmmoColor.RED),
  YELLOW(AmmoColor.YELLOW),
  GRAY(null),
  GREEN(null),
  PURPLE(null);

  private AmmoColor color;

  CellColor(AmmoColor color) {
    this.color = color;
  }

  public boolean matchesAmmoColor(AmmoColor ammoColor) {
    return ammoColor.equals(color);
  }
}
