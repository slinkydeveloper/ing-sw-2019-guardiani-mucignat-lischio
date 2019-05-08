package com.adrenalinici.adrenaline.model.event;

import com.adrenalinici.adrenaline.model.fat.GameModel;

public class GameModelUpdatedEvent extends BaseModelEvent {

  public GameModelUpdatedEvent(GameModel gameModel) {
    super(gameModel);
  }
}
