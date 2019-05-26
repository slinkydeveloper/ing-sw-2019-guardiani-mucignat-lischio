package com.adrenalinici.adrenaline.model;

import com.adrenalinici.adrenaline.model.common.AmmoColor;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.PowerUpCard;
import com.adrenalinici.adrenaline.model.common.PowerUpType;
import com.adrenalinici.adrenaline.model.fat.PlayerDashboard;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class PlayerDashboardTest {

  @Test
  public void addAmmoTest() {
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.YELLOW, Collections.emptyList());
    playerDashboard.addAmmo(AmmoColor.RED);

    assertThat(playerDashboard.getAmmos()).hasSize(4);
    assertThat(playerDashboard.getAmmos())
      .containsOnly(AmmoColor.RED, AmmoColor.RED, AmmoColor.YELLOW, AmmoColor.BLUE);
  }

  @Test
  public void newPlayerDashboardMustContainOneAmmoForType() {
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.YELLOW, Collections.emptyList());
    assertThat(playerDashboard.getAmmos()).hasSize(3);
    assertThat(playerDashboard.getAmmos())
      .containsOnly(AmmoColor.RED, AmmoColor.BLUE, AmmoColor.YELLOW);
  }

  @Test
  public void addAmmoMustThrowExceptionTest() {
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.YELLOW, Collections.emptyList());
    playerDashboard.addAmmo(AmmoColor.RED);
    playerDashboard.addAmmo(AmmoColor.RED);

    assertThat(playerDashboard.getAmmos())
      .containsOnly(AmmoColor.RED, AmmoColor.RED, AmmoColor.RED, AmmoColor.YELLOW, AmmoColor.BLUE);
    assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> playerDashboard.addAmmo(AmmoColor.RED));
    assertThat(playerDashboard.getAmmos())
      .containsOnly(AmmoColor.RED, AmmoColor.RED, AmmoColor.RED, AmmoColor.YELLOW, AmmoColor.BLUE);
  }

  @Test
  public void marksTest() {
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.YELLOW, Collections.emptyList());
    List<PlayerColor> playerColors = Arrays.asList(
      PlayerColor.PURPLE,
      PlayerColor.PURPLE,
      PlayerColor.GREEN
    );

    playerDashboard.addMarks(playerColors);
    assertThat(playerDashboard.getMarks())
      .containsOnly(PlayerColor.PURPLE, PlayerColor.PURPLE, PlayerColor.GREEN);

    playerDashboard.removeMarks(playerColors);
    assertThat(playerDashboard.getMarks())
      .isEmpty();
  }

  @Test
  public void removeAmmosTest() {
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.YELLOW, Collections.emptyList());
    playerDashboard.addAmmo(AmmoColor.RED);
    playerDashboard.removeAmmos(Arrays.asList(AmmoColor.RED, AmmoColor.BLUE));
    assertThat(playerDashboard.getAmmos())
      .containsOnly(AmmoColor.YELLOW, AmmoColor.RED);
  }

  @Test
  public void removeAmmosIncludingPowerupsTest() {
    PowerUpCard blueKineticRay = new PowerUpCard(AmmoColor.BLUE, PowerUpType.KINETIC_RAY);
    PowerUpCard blueScope = new PowerUpCard(AmmoColor.BLUE, PowerUpType.SCOPE);
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.YELLOW,
      Arrays.asList(blueKineticRay, blueScope));

    List<AmmoColor> ammosToRemove = Arrays.asList(AmmoColor.RED, AmmoColor.BLUE, AmmoColor.BLUE);

    playerDashboard.removeAmmosIncludingPowerups(ammosToRemove);
    assertThat(playerDashboard.getPowerUpCards()).containsOnly(blueScope);
  }

  @Test
  public void removeAmmosIncludingPowerupsWithoutNeedOfPowerupsTest() {
    PowerUpCard blueKineticRay = new PowerUpCard(AmmoColor.BLUE, PowerUpType.KINETIC_RAY);
    PowerUpCard yellowScope = new PowerUpCard(AmmoColor.YELLOW, PowerUpType.SCOPE);
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.YELLOW,
      Arrays.asList(blueKineticRay, yellowScope));

    List<AmmoColor> ammosToRemove = Arrays.asList(AmmoColor.BLUE, AmmoColor.YELLOW);

    playerDashboard.removeAmmosIncludingPowerups(ammosToRemove);
    assertThat(playerDashboard.getPowerUpCards()).containsExactlyInAnyOrder(blueKineticRay, yellowScope);
    assertThat(playerDashboard.getAmmos()).containsOnly(AmmoColor.RED);
  }

  @Test
  public void getKillDamageTest() {
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.YELLOW, Collections.emptyList());
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
    assertThat(playerDashboard.getKillDamage()).isEqualTo(Optional.empty());
    playerDashboard.addDamages(damages);
    assertThat(playerDashboard.getKillDamage()).isEqualTo(Optional.of(PlayerColor.CYAN));
  }

  @Test
  public void getCruelDamageTest() {
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.YELLOW, Collections.emptyList());
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
    assertThat(playerDashboard.getCruelDamage()).isEqualTo(Optional.empty());
    playerDashboard.addDamages(damages);
    assertThat(playerDashboard.getCruelDamage()).isEqualTo(Optional.of(PlayerColor.GREEN));
  }

  @Test
  public void getFirstDamageTest() {
    PlayerDashboard playerDashboard = new PlayerDashboard(PlayerColor.YELLOW, Collections.emptyList());
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
    assertThat(playerDashboard.getFirstDamage()).isEqualTo(Optional.empty());
    playerDashboard.addDamages(damages);
    assertThat(playerDashboard.getFirstDamage()).isEqualTo(Optional.of(PlayerColor.PURPLE));
  }
}
