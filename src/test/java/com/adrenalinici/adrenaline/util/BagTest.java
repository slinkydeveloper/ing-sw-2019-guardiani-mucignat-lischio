package com.adrenalinici.adrenaline.util;

import com.adrenalinici.adrenaline.model.common.AmmoColor;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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

  @Test
  public void sumTest() {
    Bag<AmmoColor> ammos = Bag.sum(
      Arrays.asList(AmmoColor.YELLOW, AmmoColor.YELLOW, AmmoColor.RED),
      Arrays.asList(AmmoColor.BLUE, AmmoColor.RED)
    );
    assertThat(ammos.get(AmmoColor.YELLOW)).isEqualTo(2);
    assertThat(ammos.get(AmmoColor.BLUE)).isEqualTo(1);
    assertThat(ammos.get(AmmoColor.RED)).isEqualTo(2);

    List<AmmoColor> generatedAmmos = ammos.toList();
    assertThat(generatedAmmos)
      .containsExactlyInAnyOrder(AmmoColor.YELLOW, AmmoColor.YELLOW, AmmoColor.RED, AmmoColor.BLUE, AmmoColor.RED);
  }

  @Test
  public void differenceTest() {
    Bag<AmmoColor> ammos = Bag.difference(
      Arrays.asList(AmmoColor.YELLOW, AmmoColor.YELLOW, AmmoColor.RED),
      Arrays.asList(AmmoColor.YELLOW, AmmoColor.RED)
    );
    assertThat(ammos.get(AmmoColor.YELLOW)).isEqualTo(1);
    assertThat(ammos.get(AmmoColor.BLUE)).isEqualTo(0);
    assertThat(ammos.get(AmmoColor.RED)).isEqualTo(0);

    List<AmmoColor> generatedAmmos = ammos.toList();
    assertThat(generatedAmmos)
      .containsExactlyInAnyOrder(AmmoColor.YELLOW);
  }

}
