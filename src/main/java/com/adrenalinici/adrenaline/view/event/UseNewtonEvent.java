package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.model.common.PowerUpCard;

import java.util.function.Consumer;

public class UseNewtonEvent implements ViewEvent {

  private PowerUpCard chosenCard;

  public UseNewtonEvent(PowerUpCard chosenCard) {
    this.chosenCard = chosenCard;
  }

  public PowerUpCard getChosenCard() {
    return chosenCard;
  }

  @Override
  public void onUseNewtonEvent(Consumer<UseNewtonEvent> consumer) {
    consumer.accept(this);
  }
}
