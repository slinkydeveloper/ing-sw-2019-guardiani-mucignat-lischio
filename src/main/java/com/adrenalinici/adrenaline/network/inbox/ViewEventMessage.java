package com.adrenalinici.adrenaline.network.inbox;

import com.adrenalinici.adrenaline.view.event.ViewEvent;

import java.util.function.Consumer;

public class ViewEventMessage implements InboxMessage {

  private final ViewEvent viewEvent;

  public ViewEventMessage(ViewEvent viewEvent) {
    this.viewEvent = viewEvent;
  }

  public ViewEvent getViewEvent() {
    return viewEvent;
  }

  @Override
  public void onViewEventMessage(Consumer<ViewEventMessage> consumer) {
    consumer.accept(this);
  }
}
