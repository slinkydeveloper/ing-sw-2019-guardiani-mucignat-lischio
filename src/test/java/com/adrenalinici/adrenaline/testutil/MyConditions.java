package com.adrenalinici.adrenaline.testutil;

import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.model.common.Gun;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.Position;
import com.adrenalinici.adrenaline.model.event.DashboardCellUpdatedEvent;
import com.adrenalinici.adrenaline.model.event.GameModelUpdatedEvent;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.model.event.PlayerDashboardUpdatedEvent;
import org.assertj.core.api.Condition;

public class MyConditions {

  public static Condition<ModelEvent> isDashboardCellUpdatedEvent(int line, int cell) {
    return new Condition<>(e ->
      e instanceof DashboardCellUpdatedEvent && ((DashboardCellUpdatedEvent) e).getCellPosition().equals(Position.of(line, cell)),
      "Event must be a DashboardCellUpdatedEvent with cell line " + line + " and row " + cell
    );
  }

  public static Condition<ModelEvent> isPlayerDashboardUpdateEvent(PlayerColor color) {
    return new Condition<>(e ->
      e instanceof PlayerDashboardUpdatedEvent && ((PlayerDashboardUpdatedEvent) e).getPlayerColor() == color,
      "Event must be a PlayerDashboardUpdatedEvent of " + color + " player");
  }

  public static Condition<ModelEvent> isGameModelUpdatedEvent() {
    return new Condition<>(e ->
      e instanceof GameModelUpdatedEvent,
      "Event must be a GameModelUpdatedEvent");
  }

  public static Condition<Gun> gunWithId(String id) {
    return new Condition<>(g ->
      g.getId().equals(id),
      "Gun must have id " + id);
  }


}
