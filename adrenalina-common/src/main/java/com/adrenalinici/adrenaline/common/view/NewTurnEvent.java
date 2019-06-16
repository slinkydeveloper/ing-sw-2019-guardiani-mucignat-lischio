package com.adrenalinici.adrenaline.common.view;

import java.util.function.Consumer;

public class NewTurnEvent implements ViewEvent {

  @Override
  public void onNewTurnEvent(Consumer<NewTurnEvent> consumer) {
    consumer.accept(this);
  }
}
