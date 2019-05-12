package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.common.*;

import java.util.*;

import java.util.function.Consumer;

public class AvailableExtraEffectsMessage implements OutboxMessage {

  private Effect firstExtraEffect;
  private Effect secondExtraEffect;

  public AvailableExtraEffectsMessage(Effect firstExtraEffect, Effect secondExtraEffect) {
    this.firstExtraEffect = firstExtraEffect;
    this.secondExtraEffect = secondExtraEffect;
  }

  public Effect getFirstExtraEffect() { return this.firstExtraEffect; }

  public Effect getSecondExtraEffect() { return this.secondExtraEffect; }

  @Override
  public void onAvailableExtraEffectsMessage(Consumer<AvailableExtraEffectsMessage> c) { c.accept(this); }

}
