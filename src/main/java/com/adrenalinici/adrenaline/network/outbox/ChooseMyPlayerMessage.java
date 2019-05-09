package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.common.PlayerColor;

import java.util.List;
import java.util.function.Consumer;

public class ChooseMyPlayerMessage implements OutboxMessage {

  List<PlayerColor> playerColor;

  public ChooseMyPlayerMessage(List<PlayerColor> playerColor) {
    this.playerColor = playerColor;
  }

  public List<PlayerColor> getPlayerColors() {
    return playerColor;
  }

  @Override
  public void onChooseMyPlayerMessage(Consumer<ChooseMyPlayerMessage> c) {
    c.accept(this);
  }
}
