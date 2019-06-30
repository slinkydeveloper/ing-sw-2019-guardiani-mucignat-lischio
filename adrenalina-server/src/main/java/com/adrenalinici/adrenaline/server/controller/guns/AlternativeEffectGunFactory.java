package com.adrenalinici.adrenaline.server.controller.guns;

import com.adrenalinici.adrenaline.common.model.AlternativeEffectGun;
import com.adrenalinici.adrenaline.server.JsonUtils;
import com.adrenalinici.adrenaline.server.controller.DecoratedAlternativeEffectGun;
import com.adrenalinici.adrenaline.server.controller.DecoratedEffect;
import com.adrenalinici.adrenaline.server.controller.GunFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static com.adrenalinici.adrenaline.server.JsonUtils.*;

public abstract class AlternativeEffectGunFactory implements GunFactory {

  @Override
  public AlternativeEffectGun getModelGun(String key, ObjectNode config) {
    return new AlternativeEffectGun(
      key,
      parseAmmoColor(config.get("firstAmmo")),
      parseListAmmoColor(config.get("extraAmmo")),
      parseEffect(config.get("firstEffect"), key),
      parseEffect(config.get("secondEffect"), key),
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
