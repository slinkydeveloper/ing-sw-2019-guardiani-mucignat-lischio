package com.adrenalinici.adrenaline.common.model.event;

import com.adrenalinici.adrenaline.common.model.light.LightGameModel;

public class GameModelUpdatedEvent extends BaseModelEvent {

  public GameModelUpdatedEvent(LightGameModel gameModel) {
    super(gameModel);
  }
}
