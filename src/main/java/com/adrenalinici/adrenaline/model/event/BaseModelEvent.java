package com.adrenalinici.adrenaline.model.event;

import com.adrenalinici.adrenaline.model.GameModel;

public abstract class BaseModelEvent implements ModelEvent {

  GameModel gameModel;

  public BaseModelEvent(GameModel gameModel) {
    this.gameModel = gameModel;
  }

  @Override
  public GameModel getGameModel() {
    return gameModel;
  }
}
