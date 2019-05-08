package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.AlternativeEffectGun;
import com.adrenalinici.adrenaline.model.BaseEffectGun;
import com.adrenalinici.adrenaline.util.JsonUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

public class GunLoaderTest {

  GunLoader gunLoader;

  AtomicBoolean endCalled;

  @Before
  public void setup() {
    this.endCalled = new AtomicBoolean(false);
    this.gunLoader = GunLoader.INSTANCE;
  }

  @Test
  public void testGunLoaderCache() {
    gunLoader.getModelGun(ZX2Id());
    gunLoader.getModelGun(MachineGunId());
    assertThat(GunLoader.getConfigs().get(ZX2Id()))
      .isEqualTo(
        JsonUtils.getConfigurationJSONFromClasspath(ZX2Id() + ".json")
      );
    assertThat(GunLoader.getConfigs().get(MachineGunId()))
      .isEqualTo(
        JsonUtils.getConfigurationJSONFromClasspath(MachineGunId() + ".json")
      );

    assertThat(gunLoader.getGuns().get(ZX2Id())).isInstanceOf(AlternativeEffectGun.class);
    assertThat(gunLoader.getGuns().get(MachineGunId())).isInstanceOf(BaseEffectGun.class);

    gunLoader.getDecoratedGun(ZX2Id());
    assertThat(gunLoader.getDecoratedGuns().get(ZX2Id())).isInstanceOf(DecoratedAlternativeEffectGun.class);

    gunLoader.getDecoratedGun(MachineGunId());
    assertThat(gunLoader.getDecoratedGuns().get(MachineGunId())).isInstanceOf(DecoratedBaseEffectGun.class);

    gunLoader.getAdditionalNodes(ZX2Id());
    assertThat(gunLoader.getNodes().get(ZX2Id()).size()).isEqualTo(2);
    assertThat(gunLoader.getNodes().get(ZX2Id()).get(0).id())
      .isEqualTo("apply_gun_effect_zx2_base");
    assertThat(gunLoader.getNodes().get(ZX2Id()).get(1).id())
      .isEqualTo("apply_gun_effect_zx2_scanner");

    gunLoader.getAdditionalNodes(MachineGunId());
    assertThat(gunLoader.getNodes().get(MachineGunId()).size()).isEqualTo(1);
    assertThat(gunLoader.getNodes().get(MachineGunId()).get(0).id())
      .isEqualTo("apply_gun_effect_machine_gun_base");

  }

  private String ZX2Id() {
    return "zx2";
  }

  private String MachineGunId() {
    return "machine_gun";
  }
}
