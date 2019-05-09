package com.adrenalinici.adrenaline.model.light;

import com.adrenalinici.adrenaline.model.common.AmmoCard;
import com.adrenalinici.adrenaline.model.common.PlayerColor;

import java.util.List;
import java.util.function.Consumer;

public class LightPickupDashboardCell extends LightDashboardCell {

  private AmmoCard ammoCard;

  public LightPickupDashboardCell(List<PlayerColor> playersInCell, AmmoCard ammoCard) {
    super(playersInCell);
    this.ammoCard = ammoCard;
  }

  public AmmoCard getAmmoCard() {
    return ammoCard;
  }

  @Override
  public void visit(Consumer<LightRespawnDashboardCell> visitRespawnDashboardCell, Consumer<LightPickupDashboardCell> visitPickupDashboardCell) {
    visitPickupDashboardCell.accept(this);
  }
}
