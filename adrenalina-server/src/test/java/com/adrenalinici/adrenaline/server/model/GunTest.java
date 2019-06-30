package com.adrenalinici.adrenaline.server.model;

import com.adrenalinici.adrenaline.common.model.AmmoColor;
import com.adrenalinici.adrenaline.common.model.BaseEffectGun;
import com.adrenalinici.adrenaline.common.model.Effect;
import com.adrenalinici.adrenaline.common.model.Gun;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;


public class GunTest {

  @Test
  public void testGetAmmo() {
    Gun g = new BaseEffectGun(
      "",
      AmmoColor.BLUE,
      Arrays.asList(AmmoColor.RED),
      new Effect("", ""),
      null, Collections.emptyList(), null, Collections.emptyList()
    );
    assertThat(g.getRequiredAmmoToReload())
      .containsOnly(AmmoColor.BLUE, AmmoColor.RED);
    assertThat(g.getRequiredAmmoToPickup())
      .containsOnly(AmmoColor.RED);
  }

}
