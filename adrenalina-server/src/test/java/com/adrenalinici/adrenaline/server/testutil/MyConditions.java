package com.adrenalinici.adrenaline.server.testutil;

import com.adrenalinici.adrenaline.common.model.Gun;
import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.model.event.DashboardCellUpdatedEvent;
import com.adrenalinici.adrenaline.common.model.event.GameModelUpdatedEvent;
import com.adrenalinici.adrenaline.common.model.event.ModelEvent;
import com.adrenalinici.adrenaline.common.model.event.PlayerDashboardUpdatedEvent;
import com.adrenalinici.adrenaline.server.model.DashboardCell;
import com.adrenalinici.adrenaline.server.model.PickupDashboardCell;
import com.adrenalinici.adrenaline.server.model.RespawnDashboardCell;
import org.assertj.core.api.Condition;

import java.util.function.Predicate;

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

  public static Predicate<DashboardCell> EMPTY_CELL_PREDICATE = e -> (
      (e instanceof RespawnDashboardCell && ((RespawnDashboardCell)e).getAvailableGuns().isEmpty()) ||
      (e instanceof PickupDashboardCell && !((PickupDashboardCell)e).getAmmoCard().isPresent())
    );

  public static Condition<DashboardCell> emptyDashboardCell() {
    return new Condition<>(EMPTY_CELL_PREDICATE, "Dashboard cell must be empty");
  }

}
