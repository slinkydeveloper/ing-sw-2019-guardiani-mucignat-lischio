package com.adrenalinici.adrenaline.common.view;

import com.adrenalinici.adrenaline.common.model.Position;
import com.adrenalinici.adrenaline.common.model.PowerUpCard;

import java.util.Objects;
import java.util.function.Consumer;

public class UseTeleporterEvent implements ViewEvent {

  private final Position chosenPosition;
  private final PowerUpCard chosenCard;

  public UseTeleporterEvent(Position chosenPosition, PowerUpCard chosenCard) {
    this.chosenPosition = chosenPosition;
    this.chosenCard = chosenCard;
  }

  public Position getChosenPosition() {
    return chosenPosition;
  }

  public PowerUpCard getChosenCard() {
    return chosenCard;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UseTeleporterEvent that = (UseTeleporterEvent) o;
    return Objects.equals(chosenPosition, that.chosenPosition) &&
      Objects.equals(chosenCard, that.chosenCard);
  }

  @Override
  public int hashCode() {
    return Objects.hash(chosenPosition, chosenCard);
  }

  @Override
  public void onUseTeleporterEvent(Consumer<UseTeleporterEvent> consumer) {
    consumer.accept(this);
  }
}
