package com.adrenalinici.adrenaline.view.event;

import com.adrenalinici.adrenaline.view.GameView;

import java.util.function.Consumer;

public class BaseGunEffectChosenEvent extends BaseViewEvent {

  private boolean chosenFirstExtraEffect;
  private boolean chosenSecondExtraEffect;

  public BaseGunEffectChosenEvent(GameView view, boolean chosenFirstExtraEffect, boolean chosenSecondExtraEffect) {
    super(view);
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
}
