package com.adrenalinici.adrenaline.common.model.event;

import com.adrenalinici.adrenaline.common.model.light.LightGameModel;

public abstract class BaseModelEvent implements ModelEvent {

  LightGameModel gameModel;

  public BaseModelEvent(LightGameModel gameModel) {
    this.gameModel = gameModel;
  }

  @Override
  public LightGameModel getGameModel() {
    return gameModel;
  }
}
