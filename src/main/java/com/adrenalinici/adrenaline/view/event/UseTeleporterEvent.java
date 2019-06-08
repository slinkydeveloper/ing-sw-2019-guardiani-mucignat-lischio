package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.model.common.Position;
import com.adrenalinici.adrenaline.model.common.PowerUpCard;

import java.util.function.Consumer;

public class UseTeleporterEvent implements ViewEvent {

  private Position chosenPosition;
  private PowerUpCard chosenCard;

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
  public void onUseTeleporterEvent(Consumer<UseTeleporterEvent> consumer) {
    consumer.accept(this);
  }
}
