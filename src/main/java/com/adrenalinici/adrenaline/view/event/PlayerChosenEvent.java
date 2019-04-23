package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.view.GameView;

import java.util.function.Consumer;

public class PlayerChosenEvent extends BaseViewEvent {

  private PlayerColor playerColor;

  public PlayerChosenEvent(GameView view, PlayerColor playerColor) {
    super(view);
    this.playerColor = playerColor;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  @Override
  public void onPlayerChosenEvent(Consumer<PlayerChosenEvent> consumer) {
    consumer.accept(this);
  }
}
