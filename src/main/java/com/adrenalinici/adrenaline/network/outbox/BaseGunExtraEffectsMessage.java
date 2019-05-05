package com.adrenalinici.adrenaline.network.outbox;

import com.adrenalinici.adrenaline.model.*;

import java.util.*;

import java.util.function.Consumer;

public class BaseGunExtraEffectsMessage implements OutboxMessage {

  private List<Effect> effects;

  public BaseGunExtraEffectsMessage(List<Effect> effects) {
    this.effects = effects;
  }

  public List<Effect> getEffects() { return this.effects; }

  @Override
  public void onBaseGunExtraEffectsMessage(Consumer<BaseGunExtraEffectsMessage> c) { c.accept(this); }

}
