package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.view.GameView;

import java.util.function.Consumer;

public class AlternativeGunEffectChosenEvent extends BaseViewEvent {

  boolean chosenSecondEffect;

  public AlternativeGunEffectChosenEvent(GameView view, boolean chooseSecondaryEffect) {
    super(view);
    this.chosenSecondEffect = chooseSecondaryEffect;
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
}
