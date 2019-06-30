package com.adrenalinici.adrenaline.server.controller.guns;

import com.adrenalinici.adrenaline.common.model.BaseEffectGun;
import com.adrenalinici.adrenaline.server.JsonUtils;
import com.adrenalinici.adrenaline.server.controller.DecoratedBaseEffectGun;
import com.adrenalinici.adrenaline.server.controller.DecoratedEffect;
import com.adrenalinici.adrenaline.server.controller.GunFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static com.adrenalinici.adrenaline.server.JsonUtils.*;

public abstract class BaseEffectGunFactory implements GunFactory {

  @Override
  public BaseEffectGun getModelGun(String key, ObjectNode config) {
    return new BaseEffectGun(
      key,
      parseAmmoColor(config.get("firstAmmo")),
      parseListAmmoColor(config.get("extraAmmo")),
      parseEffect(config.get("baseEffect"), key),
      parseEffect(config.get("firstExtraEffect"), key),
      parseEffectCost(config.get("firstExtraEffect")),
      parseEffect(config.get("secondExtraEffect"), key),
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
