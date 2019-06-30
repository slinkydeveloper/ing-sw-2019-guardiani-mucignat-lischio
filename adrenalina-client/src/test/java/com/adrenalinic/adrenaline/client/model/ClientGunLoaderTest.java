package com.adrenalinic.adrenaline.client.model;

import com.adrenalinici.adrenaline.client.model.ClientGunLoader;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientGunLoaderTest {

  @Test
  public void testLoadGunName() {
    assertThat(ClientGunLoader.INSTANCE.getGunName("cyberblade")).isEqualTo("Cyberblade");
  }

  @Test
  public void testLoadGunNote() {
    assertThat(ClientGunLoader.INSTANCE.getGunNote("flamethrower"))
      .contains("This weapon cannot damage anyone in your square. However, it can sometimes damage a target you can't see – the flame won't go through walls, but it will go through doors. Think of it as a straight-line blast of flame that can travel 2 squares in a cardinal direction.");
  }

  @Test
  public void testGetEffectName() {
    assertThat(ClientGunLoader.INSTANCE.getGunEffectName("machine_gun", "base"))
      .isEqualTo("Basic effect");
    assertThat(ClientGunLoader.INSTANCE.getGunEffectName("machine_gun", "focus"))
      .isEqualTo("Focus shot");
    assertThat(ClientGunLoader.INSTANCE.getGunEffectName("machine_gun", "tripod"))
      .isEqualTo("Turret tripod");

    assertThat(ClientGunLoader.INSTANCE.getGunEffectName("hellion", "base"))
      .isEqualTo("Basic mode");
    assertThat(ClientGunLoader.INSTANCE.getGunEffectName("hellion", "nanoTracer"))
      .isEqualTo("Nano-tracer mode");
  }

}
