package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.view.GameView;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;

public class BaseGunEffectChosenEvent implements ViewEvent {

  private boolean chosenFirstExtraEffect;
  private boolean chosenSecondExtraEffect;

  public BaseGunEffectChosenEvent(boolean chosenFirstExtraEffect, boolean chosenSecondExtraEffect) {
    this.chosenFirstExtraEffect = chosenFirstExtraEffect;
    this.chosenSecondExtraEffect = chosenSecondExtraEffect;
  }

  public boolean isChosenFirstExtraEffect() {
    return chosenFirstExtraEffect;
  }

  public boolean isChosenSecondExtraEffect() {
    return chosenSecondExtraEffect;
  }

  @Override
  public void onBaseGunEffectChosenEvent(Consumer<BaseGunEffectChosenEvent> consumer) {
    consumer.accept(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BaseGunEffectChosenEvent that = (BaseGunEffectChosenEvent) o;
    return chosenFirstExtraEffect == that.chosenFirstExtraEffect &&
      chosenSecondExtraEffect == that.chosenSecondExtraEffect;
  }

  @Override
  public int hashCode() {
    return Objects.hash(chosenFirstExtraEffect, chosenSecondExtraEffect);
  }
}
