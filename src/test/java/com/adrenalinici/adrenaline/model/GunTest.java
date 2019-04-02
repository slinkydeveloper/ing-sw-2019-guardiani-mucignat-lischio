package com.adrenalinici.adrenaline.model;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.adrenalinici.adrenaline.model.MyAssertions.assertListEqualsWithoutOrdering;

public class GunTest {

  @Test
  public void testGetAmmo() {
    Gun g = new BaseEffectGun(
      AmmoColor.BLUE,
      Arrays.asList(AmmoColor.RED),
      "Card 1",
      null,
      new Effect("", ""),
      null, Collections.emptyList(), null, Collections.emptyList()
    );
    assertListEqualsWithoutOrdering(Arrays.asList(AmmoColor.BLUE, AmmoColor.RED), g.getRequiredAmmoToReload());
    assertListEqualsWithoutOrdering(Arrays.asList(AmmoColor.RED), g.getRequiredAmmoToPickup());
  }

}
