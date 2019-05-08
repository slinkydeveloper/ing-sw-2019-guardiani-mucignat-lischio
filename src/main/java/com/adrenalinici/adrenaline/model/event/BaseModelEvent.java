package com.adrenalinici.adrenaline.model.event;

import com.adrenalinici.adrenaline.model.fat.GameModel;
import com.adrenalinici.adrenaline.model.light.LightGameModel;

public abstract class BaseModelEvent implements ModelEvent {

  LightGameModel gameModel;

  public BaseModelEvent(GameModel gameModel) {
    this.gameModel = gameModel.light();
  }

  @Override
  public LightGameModel getGameModel() {
    return gameModel;
  }
}
