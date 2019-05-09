package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.*;
import com.adrenalinici.adrenaline.model.common.*;

import java.util.*;

import java.util.function.Consumer;

public class AvailableEnemyMovementsMessage implements OutboxMessage {

  private List<Position> positions;

  public AvailableEnemyMovementsMessage(List<Position> positions) {
    this.positions = positions;
  }

  public List<Position> getPositions() { return this.positions; }

  @Override
  public void onAvailableEnemyMovementsMessage(Consumer<AvailableEnemyMovementsMessage> c) { c.accept(this); }

}
