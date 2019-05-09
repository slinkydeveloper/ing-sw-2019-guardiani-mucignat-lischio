package com.adrenalinici.adrenaline.controller.guns;

import com.adrenalinici.adrenaline.controller.DecoratedAlternativeEffectGun;
import com.adrenalinici.adrenaline.controller.GunFactory;
import com.adrenalinici.adrenaline.controller.GunLoader;
import com.adrenalinici.adrenaline.controller.nodes.guns.AlternativeEffectGunFlowState;
import com.adrenalinici.adrenaline.controller.nodes.guns.ChooseAlternativeEffectForGunFlowNode;
import com.adrenalinici.adrenaline.flow.FlowNode;
import com.adrenalinici.adrenaline.model.common.AmmoColor;
import com.adrenalinici.adrenaline.model.common.PlayerColor;
import com.adrenalinici.adrenaline.model.common.Position;
import com.adrenalinici.adrenaline.model.event.ModelEvent;
import com.adrenalinici.adrenaline.model.fat.PlayerDashboard;
import com.adrenalinici.adrenaline.view.event.AlternativeGunEffectChosenEvent;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.adrenalinici.adrenaline.testutil.MyConditions.isPlayerDashboardUpdateEvent;
import static org.assertj.core.api.Assertions.assertThat;

public class ElectroscytheTest extends BaseGunTest {
  @Override
  protected GunFactory gunFactory() {
    return new ElectroscytheGunFactory();
  }

  @Override
  protected List<FlowNode> nodes() {
    return Collections.singletonList(new ChooseAlternativeEffectForGunFlowNode());
  }

  @Override
  protected String gunId() {
    return "electroscythe";
  }

  @Test
  public void testBaseEffect() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.CYAN);

    model.getPlayerDashboard(PlayerColor.GRAY).addDamages(Collections.nCopies(10, PlayerColor.GREEN));

    PlayerDashboard killerPlayerDashboard = model.getPlayerDashboard(PlayerColor.GREEN);
    killerPlayerDashboard.addGun(gunId());

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.nextPhase(
      viewMock,
      new AlternativeEffectGunFlowState((DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun(gunId()))
    );
    assertThat(context.getKilledPlayers().isEmpty()).isTrue();
    context.handleEvent(new AlternativeGunEffectChosenEvent(false), viewMock);

    assertThat(killerPlayerDashboard.getDamages().isEmpty()).isTrue();
    assertThat(model.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .containsExactly(PlayerColor.GREEN);
    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages())
      .isEqualTo(Collections.nCopies(11, PlayerColor.GREEN));

    assertThat(model.getPlayerDashboard(PlayerColor.CYAN).getDamages().isEmpty()).isTrue();

    assertThat(context.getKilledPlayers()).containsExactly(PlayerColor.GRAY);

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW));

    assertThat(killerPlayerDashboard.getLoadedGuns())
      .doesNotContain(gunId());

    assertThat(killerPlayerDashboard.getUnloadedGuns())
      .containsExactly(gunId());

  }

  @Test
  public void testReaperEffect() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.CYAN);

    model.getPlayerDashboard(PlayerColor.GRAY).addDamages(Collections.nCopies(9, PlayerColor.GREEN));

    PlayerDashboard killerPlayerDashboard = model.getPlayerDashboard(PlayerColor.GREEN);
    killerPlayerDashboard.addGun(gunId());

    List<ModelEvent> receivedModelEvents = new ArrayList<>();
    model.registerObserver(receivedModelEvents::add);

    context.nextPhase(
      viewMock,
      new AlternativeEffectGunFlowState((DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun(gunId()))
    );
    assertThat(context.getKilledPlayers().isEmpty()).isTrue();
    context.handleEvent(new AlternativeGunEffectChosenEvent(true), viewMock);

    assertThat(killerPlayerDashboard.getAmmos()).containsOnly(AmmoColor.YELLOW);
    assertThat(killerPlayerDashboard.getDamages().isEmpty()).isTrue();

    assertThat(model.getPlayerDashboard(PlayerColor.YELLOW).getDamages())
      .isEqualTo(Collections.nCopies(2, PlayerColor.GREEN));
    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages())
      .isEqualTo(Collections.nCopies(11, PlayerColor.GREEN));

    assertThat(model.getPlayerDashboard(PlayerColor.CYAN).getDamages().isEmpty()).isTrue();

    assertThat(context.getKilledPlayers()).containsExactly(PlayerColor.GRAY);

    assertThat(receivedModelEvents)
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.GRAY))
      .haveExactly(1, isPlayerDashboardUpdateEvent(PlayerColor.YELLOW));

    assertThat(killerPlayerDashboard.getLoadedGuns())
      .doesNotContain(gunId());

    assertThat(killerPlayerDashboard.getUnloadedGuns())
      .containsExactly(gunId());
  }
}
