package com.adrenalinici.adrenaline.network.inbox;

import com.adrenalinici.adrenaline.model.common.PlayerColor;

import java.util.function.Consumer;

public class ChosenMyPlayerColorMessage implements InboxMessage {

  private PlayerColor color;

  public ChosenMyPlayerColorMessage(PlayerColor color) {
    this.color = color;
  }

  public PlayerColor getColor() {
    return color;
  }

  @Override
  public void onChosenMyPlayerColorMessage(Consumer<ChosenMyPlayerColorMessage> consumer) {
    consumer.accept(this);
  }
}
