package com.adrenalinici.adrenaline.model.event;

import com.adrenalinici.adrenaline.model.GameModel;

public class GameModelUpdatedEvent extends BaseModelEvent {

  public GameModelUpdatedEvent(GameModel gameModel) {
    super(gameModel);
  }
}
