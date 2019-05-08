package com.adrenalinici.adrenaline.model;

import com.adrenalinici.adrenaline.model.common.AmmoColor;
import com.adrenalinici.adrenaline.model.common.BaseEffectGun;
import com.adrenalinici.adrenaline.model.common.Effect;
import com.adrenalinici.adrenaline.model.common.Gun;
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
      "Card 1",
      null,
      new Effect("", "", ""),
      null, Collections.emptyList(), null, Collections.emptyList()
    );
    assertThat(g.getRequiredAmmoToReload())
      .containsOnly(AmmoColor.BLUE, AmmoColor.RED);
    assertThat(g.getRequiredAmmoToPickup())
      .containsOnly(AmmoColor.RED);
  }

}
