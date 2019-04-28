package com.adrenalinici.adrenaline.testutil;

import com.adrenalinici.adrenaline.model.GameModel;
import com.adrenalinici.adrenaline.model.Gun;
import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.model.event.DashboardCellUpdatedEvent;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.model.event.PlayerDashboardUpdatedEvent;
import org.assertj.core.api.Condition;

public class MyConditions {

  public static Condition<ModelEvent> isDashboardCellUpdatedEvent(int line, int cell) {
    return new Condition<>(e ->
      e instanceof DashboardCellUpdatedEvent && ((DashboardCellUpdatedEvent) e).getCell().getLine() == line && ((DashboardCellUpdatedEvent) e).getCell().getCell() == cell,
      "Event must be a DashboardCellUpdatedEvent with cell line " + line + " and row " + cell
    );
  }

  public static Condition<ModelEvent> isPlayerDashboardUpdateEvent(PlayerColor color, GameModel status) {
    return new Condition<>(e ->
      e instanceof PlayerDashboardUpdatedEvent && ((PlayerDashboardUpdatedEvent) e).getPlayerDashboard() == status.getPlayerDashboard(color),
      "Event must be a PlayerDashboardUpdatedEvent of " + color + " player");
  }

  public static Condition<Gun> gunWithId(String id) {
    return new Condition<>(g ->
      g.getId().equals(id),
      "Gun must have id " + id);
  }


}
