package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.view.GameView;

public abstract class BaseViewEvent implements ViewEvent {

  GameView view;

  public BaseViewEvent(GameView view) {
    this.view = view;
  }

  @Override
  public GameView getView() {
    return view;
  }
}
