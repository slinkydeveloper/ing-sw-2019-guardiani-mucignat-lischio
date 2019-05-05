package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.event.ModelEvent;

import java.util.function.Consumer;

public class ModelEventMessage implements OutboxMessage {

  private ModelEvent modelEvent;

  public ModelEventMessage(ModelEvent modelEvent) {
    this.modelEvent = modelEvent;
  }

  public ModelEvent getModelEvent() {
    return modelEvent;
  }

  @Override
  public void onModelEventMessage(Consumer<ModelEventMessage> c) {
    c.accept(this);
  }
}
