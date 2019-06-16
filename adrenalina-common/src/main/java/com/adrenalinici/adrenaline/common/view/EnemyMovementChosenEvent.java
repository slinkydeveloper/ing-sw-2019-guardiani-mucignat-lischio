package com.adrenalinici.adrenaline.common.view;

import com.adrenalinici.adrenaline.common.model.PlayerColor;
import com.adrenalinici.adrenaline.common.model.Position;

import java.util.Objects;
import java.util.function.Consumer;

public class EnemyMovementChosenEvent implements ViewEvent {

  Position coordinates;
  PlayerColor enemy;

  public EnemyMovementChosenEvent(Position coordinates, PlayerColor enemy) {
    this.coordinates = coordinates;
    this.enemy = enemy;
  }

  public Position getCoordinates() {
    return coordinates;
  }

  public PlayerColor getEnemy() {
    return enemy;
  }

  @Override
  public void onEnemyMovementChosenEvent(Consumer<EnemyMovementChosenEvent> consumer) {
    consumer.accept(this);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coordinates, enemy);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EnemyMovementChosenEvent that = (EnemyMovementChosenEvent) o;
    return Objects.equals(coordinates, that.coordinates) && Objects.equals(enemy, that.enemy);
  }
}
