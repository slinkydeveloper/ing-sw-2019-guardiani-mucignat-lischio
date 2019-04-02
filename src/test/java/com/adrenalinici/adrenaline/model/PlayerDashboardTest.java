package com.adrenalinici.adrenaline.model;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.adrenalinici.adrenaline.model.MyAssertions.*;
import static org.junit.Assert.assertEquals;

public class PlayerDashboardTest {

  @Test
  public void addAmmoTest() {
    PlayerDashboard playerDashboard = new PlayerDashboard(false, Collections.emptyList());
    playerDashboard.addAmmo(AmmoColor.RED);
    assertEquals(4, playerDashboard.getAmmos().size());
    assertContainsExactly(AmmoColor.RED, 2, playerDashboard.getAmmos());
  }

  @Test
  public void newPlayerDashboardMustContainOneAmmoForType() {
    PlayerDashboard playerDashboard = new PlayerDashboard(false, Collections.emptyList());
    assertEquals(3, playerDashboard.getAmmos().size());
    assertContainsExactly(AmmoColor.RED, 1, playerDashboard.getAmmos());
    assertContainsExactly(AmmoColor.BLUE, 1, playerDashboard.getAmmos());
    assertContainsExactly(AmmoColor.YELLOW, 1, playerDashboard.getAmmos());
  }

  @Test
  public void addAmmoMustThrowExceptionTest() {
    PlayerDashboard playerDashboard = new PlayerDashboard(false, Collections.emptyList());
    playerDashboard.addAmmo(AmmoColor.RED);
    playerDashboard.addAmmo(AmmoColor.RED);
    assertContainsExactly(AmmoColor.RED, 3, playerDashboard.getAmmos());
    assertThatCodeThrowsExceptionOfType(() -> playerDashboard.addAmmo(AmmoColor.RED), IllegalStateException.class);
    assertContainsExactly(AmmoColor.RED, 3, playerDashboard.getAmmos());
  }

  @Test
  public void removeMarksTest() {
    PlayerDashboard playerDashboard = new PlayerDashboard(false, Collections.emptyList());
    List<PlayerColor> playerColors = Arrays.asList(
      PlayerColor.PURPLE,
      PlayerColor.PURPLE,
      PlayerColor.GREEN
    );
    playerDashboard.addMarks(playerColors);
    playerDashboard.removeMarks(playerColors);
    assertListEqualsWithoutOrdering(Collections.emptyList(), playerDashboard.getMarks());
  }

  @Test
  public void removeAmmosTest() {
    PlayerDashboard playerDashboard = new PlayerDashboard(false, Collections.emptyList());
    playerDashboard.addAmmo(AmmoColor.RED);
    playerDashboard.removeAmmos(Arrays.asList(AmmoColor.RED, AmmoColor.BLUE));
    assertListEqualsWithoutOrdering(Arrays.asList(AmmoColor.YELLOW, AmmoColor.RED), playerDashboard.getAmmos());
  }

  @Test
  public void getKillDamageTest() {
    PlayerDashboard playerDashboard = new PlayerDashboard(false, Collections.emptyList());
    List<PlayerColor> damages = Arrays.asList(
      PlayerColor.PURPLE,
      PlayerColor.PURPLE,
      PlayerColor.PURPLE,
      PlayerColor.PURPLE,
      PlayerColor.YELLOW,
      PlayerColor.YELLOW,
      PlayerColor.YELLOW,
      PlayerColor.CYAN,
      PlayerColor.CYAN,
      PlayerColor.CYAN,
      PlayerColor.CYAN,
      PlayerColor.GREEN
    );
    assertEquals(Optional.empty(), playerDashboard.getKillDamage());
    playerDashboard.addDamages(damages);
    assertEquals(Optional.of(PlayerColor.CYAN), playerDashboard.getKillDamage());
  }

  @Test
  public void getCruelDamageTest() {
    PlayerDashboard playerDashboard = new PlayerDashboard(false, Collections.emptyList());
    List<PlayerColor> damages = Arrays.asList(
      PlayerColor.PURPLE,
      PlayerColor.PURPLE,
      PlayerColor.PURPLE,
      PlayerColor.PURPLE,
      PlayerColor.YELLOW,
      PlayerColor.YELLOW,
      PlayerColor.YELLOW,
      PlayerColor.CYAN,
      PlayerColor.CYAN,
      PlayerColor.CYAN,
      PlayerColor.CYAN,
      PlayerColor.GREEN
    );
    assertEquals(Optional.empty(), playerDashboard.getCruelDamage());
    playerDashboard.addDamages(damages);
    assertEquals(Optional.of(PlayerColor.GREEN), playerDashboard.getCruelDamage());
  }

  @Test
  public void getFirstDamageTest() {
    PlayerDashboard playerDashboard = new PlayerDashboard(false, Collections.emptyList());
    List<PlayerColor> damages = Arrays.asList(
      PlayerColor.PURPLE,
      PlayerColor.PURPLE,
      PlayerColor.PURPLE,
      PlayerColor.PURPLE,
      PlayerColor.YELLOW,
      PlayerColor.YELLOW,
      PlayerColor.YELLOW,
      PlayerColor.CYAN,
      PlayerColor.CYAN,
      PlayerColor.CYAN,
      PlayerColor.CYAN,
      PlayerColor.GREEN
    );
    assertEquals(Optional.empty(), playerDashboard.getFirstDamage());
    playerDashboard.addDamages(damages);
    assertEquals(Optional.of(PlayerColor.PURPLE), playerDashboard.getFirstDamage());
  }
}
