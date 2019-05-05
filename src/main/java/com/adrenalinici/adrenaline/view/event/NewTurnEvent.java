package com.adrenalinici.adrenaline.view.event;

import java.util.function.Consumer;

public class NewTurnEvent implements ViewEvent {

  @Override
  public void onNewTurnEvent(Consumer<NewTurnEvent> consumer) {
    consumer.accept(this);
  }
}
