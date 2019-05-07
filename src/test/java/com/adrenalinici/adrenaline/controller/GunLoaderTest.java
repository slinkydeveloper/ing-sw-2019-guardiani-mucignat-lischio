package com.adrenalinici.adrenaline.controller;

import com.adrenalinici.adrenaline.controller.guns.MachineGunGunFactory;
import com.adrenalinici.adrenaline.controller.guns.ZX2GunFactory;
import com.adrenalinici.adrenaline.model.Gun;
import com.adrenalinici.adrenaline.util.JsonUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

  //ZX2GunFactory zx2GunFactory = new ZX2GunFactory();

  @Before
  public void setup() {
    this.endCalled = new AtomicBoolean(false);
    this.gunLoader = createGunLoader();
  }

  @Test
  public void testGunLoaderCache() {
    assertThat(gunLoader.getGuns().isEmpty()).isTrue();
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

    //gunLoader.getDecoratedGun(ZX2Id());
    //gunLoader.getDecoratedGun(MachineGunId());
  }

  private GunLoader createGunLoader() {
    return GunLoader.getGunLoaderInstance();
  }

  private String ZX2Id() {
    return "zx2";
  }

  private String MachineGunId() {
    return "machine_gun";
  }
}
