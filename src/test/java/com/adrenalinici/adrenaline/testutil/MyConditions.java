package com.adrenalinici.adrenaline.testutil;

import com.adrenalinici.adrenaline.model.GameStatus;
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

  public static Condition<ModelEvent> isPlayerDashboardUpdateEvent(PlayerColor color, GameStatus status) {
    return new Condition<>(e ->
      e instanceof PlayerDashboardUpdatedEvent && ((PlayerDashboardUpdatedEvent) e).getPlayerDashboard() == status.getPlayerDashboard(color),
      "Event must be a PlayerDashboardUpdatedEvent of " + color + " player");
  }

}
