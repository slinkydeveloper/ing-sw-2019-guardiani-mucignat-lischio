package com.adrenalinici.adrenaline.common.network.outbox;

import com.adrenalinici.adrenaline.common.model.Effect;

import java.util.function.Consumer;

public class AvailableAlternativeEffectsGunMessage implements OutboxMessage {

  private Effect firstEffect;
  private Effect secondEffect;

  public AvailableAlternativeEffectsGunMessage(Effect firstEffect, Effect secondEffect) {
    this.firstEffect = firstEffect;
    this.secondEffect = secondEffect;
  }

  public Effect getFirstEffect() { return this.firstEffect; }

  public Effect getSecondEffect() { return this.secondEffect; }

  @Override
  public void onAvailableAlternativeEffectsGunMessage(Consumer<AvailableAlternativeEffectsGunMessage> c) { c.accept(this); }

}
