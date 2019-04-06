package com.adrenalinici.adrenaline.model.event;

import com.adrenalinici.adrenaline.model.GameStatus;

public abstract class BaseModelEvent implements ModelEvent {

  GameStatus gameStatus;

  public BaseModelEvent(GameStatus gameStatus) {
    this.gameStatus = gameStatus;
  }

  @Override
  public GameStatus getGameStatus() {
    return gameStatus;
  }
}
