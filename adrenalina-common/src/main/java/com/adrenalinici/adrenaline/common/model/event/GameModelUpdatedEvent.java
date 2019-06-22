package com.adrenalinici.adrenaline.common.model.event;

import com.adrenalinici.adrenaline.common.model.light.LightGameModel;

import java.util.function.Consumer;

public class GameModelUpdatedEvent extends BaseModelEvent {

  public GameModelUpdatedEvent(LightGameModel gameModel) {
    super(gameModel);
  }

  @Override
  public void onGameModelUpdatedEvent(Consumer<GameModelUpdatedEvent> c) {
    c.accept(this);
  }
}
