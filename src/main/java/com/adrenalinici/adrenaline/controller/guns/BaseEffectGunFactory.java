package com.adrenalinici.adrenaline.controller.guns;

import com.adrenalinici.adrenaline.controller.DecoratedBaseEffectGun;
import com.adrenalinici.adrenaline.controller.DecoratedEffect;
import com.adrenalinici.adrenaline.controller.GunFactory;
import com.adrenalinici.adrenaline.model.common.BaseEffectGun;
import com.adrenalinici.adrenaline.util.JsonUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static com.adrenalinici.adrenaline.util.JsonUtils.*;

public abstract class BaseEffectGunFactory implements GunFactory {

  @Override
  public BaseEffectGun getModelGun(String key, ObjectNode config) {
    return new BaseEffectGun(
      key,
      parseAmmoColor(config.get("firstAmmo")),
      parseListAmmoColor(config.get("extraAmmo")),
      config.get("name").asText(),
      config.has("note") ? config.get("note").asText() : null,
      parseEffect(config.get("baseEffect")),
      parseEffect(config.get("firstExtraEffect")),
      parseEffectCost(config.get("firstExtraEffect")),
      parseEffect(config.get("secondExtraEffect")),
      parseEffectCost(config.get("secondExtraEffect"))
    );
  }

  @Override
  public DecoratedBaseEffectGun getDecoratedGun(String key, ObjectNode config) {
    BaseEffectGun modelGun = getModelGun(key, config);
    ObjectNode baseEffectConfig = (ObjectNode) config.get("baseEffect");
    ObjectNode firstExtraEffectConfig = (ObjectNode) config.get("firstExtraEffect");
    ObjectNode secondExtraEffectConfig = (ObjectNode) config.get("secondExtraEffect");
    return new DecoratedBaseEffectGun(
      modelGun,
      JsonUtils.parseListString(config, "phases"),
      new DecoratedEffect(
        modelGun.getBaseEffect(),
        JsonUtils.parseListString(baseEffectConfig, "phases"),
        null
      ),
      new DecoratedEffect(
        modelGun.getFirstExtraEffect(),
        JsonUtils.parseListString(firstExtraEffectConfig, "phases"),
        modelGun.getFirstExtraEffectCost()
      ),
      new DecoratedEffect(
        modelGun.getSecondExtraEffect(),
        JsonUtils.parseListString(secondExtraEffectConfig, "phases"),
        modelGun.getSecondExtraEffectCost()
      )
    );
  }
}
