package com.adrenalinici.adrenaline.controller.guns;

import com.adrenalinici.adrenaline.controller.GunFactory;
import com.adrenalinici.adrenaline.model.AlternativeEffectGun;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static com.adrenalinici.adrenaline.util.JsonUtils.*;

public abstract class AlternativeEffectGunFactory implements GunFactory {

  @Override
  public AlternativeEffectGun getModelGun(String key, ObjectNode config) {
    return new AlternativeEffectGun(
      key,
      parseAmmoColor(config.get("firstAmmo")),
      parseListAmmoColor(config.get("extraAmmo")),
      config.get("name").asText(),
      config.has("note") ? config.get("note").asText() : null,
      parseEffect(config.get("firstEffect")),
      parseEffect(config.get("secondEffect")),
      parseEffectCost(config.get("secondEffect"))
    );
  }
}
