package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.model.Action;
import com.adrenalinici.adrenaline.view.GameView;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;

public class ActionChosenEvent implements ViewEvent {

  private Action action;

  public ActionChosenEvent(Action action) {
    this.action = action;
  }

  public Action getAction() {
    return action;
  }

  @Override
  public void onActionChosenEvent(Consumer<ActionChosenEvent> consumer) {
    consumer.accept(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ActionChosenEvent that = (ActionChosenEvent) o;
    return action == that.action;
  }

  @Override
  public int hashCode() {
    return Objects.hash(action);
  }
}
