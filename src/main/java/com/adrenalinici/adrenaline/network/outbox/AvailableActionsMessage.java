package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.*;
import com.adrenalinici.adrenaline.model.common.*;

import java.util.*;

import java.util.function.Consumer;

public class AvailableActionsMessage implements OutboxMessage {

  private List<Action> actions;

  public AvailableActionsMessage(List<Action> actions) {
    this.actions = actions;
  }

  public List<Action> getActions() { return this.actions; }

  @Override
  public void onAvailableActionsMessage(Consumer<AvailableActionsMessage> c) { c.accept(this); }

}
