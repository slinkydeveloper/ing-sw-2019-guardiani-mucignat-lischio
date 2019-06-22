package com.adrenalinici.adrenaline.common.model.event;

import com.adrenalinici.adrenaline.common.model.light.LightGameModel;

import java.io.Serializable;
import java.util.function.Consumer;

public interface ModelEvent extends Serializable {

  LightGameModel getGameModel();

  default void onDashboardCellUpdatedEvent(Consumer<DashboardCellUpdatedEvent> c) {};
  default void onPlayerDashboardUpdatedEvent(Consumer<PlayerDashboardUpdatedEvent> c) {};
  default void onGameModelUpdatedEvent(Consumer<GameModelUpdatedEvent> c) {};

}
