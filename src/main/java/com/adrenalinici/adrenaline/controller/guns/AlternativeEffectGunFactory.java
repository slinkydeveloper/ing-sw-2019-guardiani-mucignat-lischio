package com.adrenalinici.adrenaline.controller.guns;

import com.adrenalinici.adrenaline.controller.DecoratedAlternativeEffectGun;
import com.adrenalinici.adrenaline.controller.DecoratedEffect;
import com.adrenalinici.adrenaline.controller.GunFactory;
import com.adrenalinici.adrenaline.model.common.AlternativeEffectGun;
import com.adrenalinici.adrenaline.util.JsonUtils;
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

  @Override
  public DecoratedAlternativeEffectGun getDecoratedGun(String key, ObjectNode config) {
    AlternativeEffectGun modelGun = getModelGun(key, config);
    ObjectNode firstEffectConfig = (ObjectNode) config.get("firstEffect");
    ObjectNode secondEffectConfig = (ObjectNode) config.get("secondEffect");
    return new DecoratedAlternativeEffectGun(
      modelGun,
      JsonUtils.parseListString(config, "phases"),
      new DecoratedEffect(
        modelGun.getFirstEffect(),
        JsonUtils.parseListString(firstEffectConfig, "phases"),
        null
      ),
      new DecoratedEffect(
        modelGun.getSecondEffect(),
        JsonUtils.parseListString(secondEffectConfig, "phases"),
        modelGun.getSecondEffectCost()
      )
    );
  }
}
