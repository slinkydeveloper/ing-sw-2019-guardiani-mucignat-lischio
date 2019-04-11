package com.adrenalinici.adrenaline.util;

import com.adrenalinici.adrenaline.model.AmmoColor;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class BagTest {

  @Test
  public void createBagTest() {
    Bag<AmmoColor> ammoBag = new Bag<>();
    assertThat(ammoBag.totalItems()).isEqualTo(0);
    assertThat(ammoBag.differentItems()).isEqualTo(0);
  }

  @Test
  public void createFromListTest() {
    Bag<AmmoColor> ammoBag = Bag.from(Arrays.asList(AmmoColor.RED, AmmoColor.RED, AmmoColor.BLUE, AmmoColor.RED));
    assertThat(ammoBag.differentItems()).isEqualTo(2);
    assertThat(ammoBag.totalItems()).isEqualTo(4);
    assertThat(ammoBag.get(AmmoColor.RED)).isEqualTo(3);
    assertThat(ammoBag.get(AmmoColor.BLUE)).isEqualTo(1);
    assertThat(ammoBag.get(AmmoColor.YELLOW)).isEqualTo(0);
  }

  @Test
  public void containsTest() {
    Bag<AmmoColor> ammoBag = Bag.from(Arrays.asList(AmmoColor.RED, AmmoColor.RED, AmmoColor.BLUE, AmmoColor.RED));
    assertThat(ammoBag.contains(Arrays.asList(AmmoColor.RED, AmmoColor.RED, AmmoColor.RED, AmmoColor.RED))).isEqualTo(false);
    assertThat(ammoBag.contains(Arrays.asList(AmmoColor.RED, AmmoColor.RED, AmmoColor.BLUE, AmmoColor.RED, AmmoColor.YELLOW))).isEqualTo(false);
    assertThat(ammoBag.contains(Arrays.asList(AmmoColor.RED, AmmoColor.RED, AmmoColor.BLUE, AmmoColor.RED))).isEqualTo(true);
  }

}
