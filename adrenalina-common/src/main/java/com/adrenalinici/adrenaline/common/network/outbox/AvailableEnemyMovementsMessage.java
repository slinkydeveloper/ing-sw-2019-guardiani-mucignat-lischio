package com.adrenalinici.adrenaline.common.network.outbox;

import com.adrenalinici.adrenaline.common.model.Position;

import java.util.List;
import java.util.function.Consumer;

public class AvailableEnemyMovementsMessage implements OutboxMessage {

  private List<Position> positions;

  public AvailableEnemyMovementsMessage(List<Position> positions) {
    this.positions = positions;
  }

  public List<Position> getPositions() {
    return positions;
  }

  @Override
  public void onAvailableEnemyMovementsMessage(Consumer<AvailableEnemyMovementsMessage> c) {
    c.accept(this);
  }
}
