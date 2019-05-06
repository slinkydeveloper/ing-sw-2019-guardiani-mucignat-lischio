package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.controller.guns.MachineGunGunFactory;
import com.adrenalinici.adrenaline.controller.guns.ZX2GunFactory;
import com.adrenalinici.adrenaline.util.JsonUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

public class GunLoaderTest {

  GunLoader gunLoader;

  AtomicBoolean endCalled;

  @Before
  public void setup() {
    this.endCalled = new AtomicBoolean(false);
    this.gunLoader = createGunLoader();
  }

  @Test
  public void testGunLoaderCacheOnZX2() {
    assertThat(gunLoader.getGuns().isEmpty()).isTrue();
    gunLoader.getModelGun(ZX2Id());
    gunLoader.getModelGun(MachineGunId());
    assertThat(gunLoader.getConfigs().size()).isEqualTo(2);
    assertThat(gunLoader.getConfigs().get(0))
      .isEqualTo(new AbstractMap.SimpleImmutableEntry<>(
        ZX2Id(),
        JsonUtils.getConfigurationJSONFromClasspath(ZX2Id() + ".json")
      ));
    assertThat(gunLoader.getConfigs().get(1))
      .isEqualTo(new AbstractMap.SimpleImmutableEntry<>(
        MachineGunId(),
        JsonUtils.getConfigurationJSONFromClasspath(MachineGunId() + ".json")
      ));
    assertThat(gunLoader.getGuns().size()).isEqualTo(2);

    gunLoader.getModelGun(ZX2Id());
    assertThat(gunLoader.getConfigs().size()).isEqualTo(2);
    assertThat(gunLoader.getGuns().size()).isEqualTo(2);
  }

  private GunLoader createGunLoader() {
    return new GunLoader(Arrays.asList(new ZX2GunFactory(), new MachineGunGunFactory()));
  }

  private String ZX2Id() {
    return "zx2";
  }

  private String MachineGunId() {
    return "machine_gun";
  }
}
