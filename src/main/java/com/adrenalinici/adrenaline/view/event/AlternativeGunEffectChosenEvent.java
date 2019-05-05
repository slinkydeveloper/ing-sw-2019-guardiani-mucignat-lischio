package com.adrenalinici.adrenaline.view.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;

public class AlternativeGunEffectChosenEvent implements ViewEvent {

  private boolean chosenSecondEffect;

  public AlternativeGunEffectChosenEvent(boolean chosenSecondEffect) {
    this.chosenSecondEffect = chosenSecondEffect;
  }

  public boolean chosenFirstEffect() {
    return !chosenSecondEffect;
  }

  public boolean chosenSecondEffect() {
    return chosenSecondEffect;
  }

  @Override
  public void onAlternativeGunEffectChosenEvent(Consumer<AlternativeGunEffectChosenEvent> consumer) {
    consumer.accept(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AlternativeGunEffectChosenEvent that = (AlternativeGunEffectChosenEvent) o;
    return chosenSecondEffect == that.chosenSecondEffect;
  }

  @Override
  public int hashCode() {
    return Objects.hash(chosenSecondEffect);
  }
}
