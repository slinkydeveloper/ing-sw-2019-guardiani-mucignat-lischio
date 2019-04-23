package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.model.PlayerColor;
import com.adrenalinici.adrenaline.view.GameView;

import java.util.function.Consumer;

public class NewTurnEvent extends BaseViewEvent {

  PlayerColor player;

  public NewTurnEvent(GameView view, PlayerColor player) {
    super(view);
    this.player = player;
  }

  public PlayerColor getPlayer() {
    return player;
  }

  @Override
  public void onNewTurnEvent(Consumer<NewTurnEvent> consumer) {
    consumer.accept(this);
  }

}
