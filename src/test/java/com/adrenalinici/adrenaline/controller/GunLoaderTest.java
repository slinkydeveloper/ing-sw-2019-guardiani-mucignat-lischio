package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.model.common.AlternativeEffectGun;
import com.adrenalinici.adrenaline.model.common.BaseEffectGun;
import com.adrenalinici.adrenaline.util.JsonUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

public class GunLoaderTest {

  AtomicBoolean endCalled;

  @Before
  public void setup() {
    this.endCalled = new AtomicBoolean(false);
  }

  @Test
  public void testGunLoaderCache() {
    GunLoader.INSTANCE.getModelGun(ZX2Id());
    GunLoader.INSTANCE.getModelGun(MachineGunId());
    assertThat(GunLoader.getConfigs().get(ZX2Id()))
      .isEqualTo(
        JsonUtils.getConfigurationJSONFromClasspath("guns/" + ZX2Id() + ".json")
      );
    assertThat(GunLoader.getConfigs().get(MachineGunId()))
      .isEqualTo(
        JsonUtils.getConfigurationJSONFromClasspath("guns/" + MachineGunId() + ".json")
      );

    assertThat(GunLoader.INSTANCE.getGuns().get(ZX2Id())).isInstanceOf(AlternativeEffectGun.class);
    assertThat(GunLoader.INSTANCE.getGuns().get(MachineGunId())).isInstanceOf(BaseEffectGun.class);

    GunLoader.INSTANCE.getDecoratedGun(ZX2Id());
    assertThat(GunLoader.INSTANCE.getDecoratedGuns().get(ZX2Id()))
      .isInstanceOf(DecoratedAlternativeEffectGun.class);

    GunLoader.INSTANCE.getDecoratedGun(MachineGunId());
    assertThat(GunLoader.INSTANCE.getDecoratedGuns().get(MachineGunId()))
      .isInstanceOf(DecoratedBaseEffectGun.class);

    GunLoader.INSTANCE.getAdditionalNodes(ZX2Id());
    assertThat(GunLoader.INSTANCE.getNodes().get(ZX2Id()).size()).isEqualTo(2);
    assertThat(GunLoader.INSTANCE.getNodes().get(ZX2Id()).get(0).id())
      .isEqualTo("apply_gun_effect_zx2_base");
    assertThat(GunLoader.INSTANCE.getNodes().get(ZX2Id()).get(1).id())
      .isEqualTo("apply_gun_effect_zx2_scanner");

    GunLoader.INSTANCE.getAdditionalNodes(MachineGunId());
    assertThat(GunLoader.INSTANCE.getNodes().get(MachineGunId()).size()).isEqualTo(1);
    assertThat(GunLoader.INSTANCE.getNodes().get(MachineGunId()).get(0).id())
      .isEqualTo("apply_gun_effect_machine_gun_base");

  }

  private String ZX2Id() {
    return "zx2";
  }

  private String MachineGunId() {
    return "machine_gun";
  }
}
