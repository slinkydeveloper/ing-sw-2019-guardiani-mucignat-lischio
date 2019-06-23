package com.adrenalinici.adrenaline.server.controller.nodes.guns;

import com.adrenalinici.adrenaline.common.model.*;
import com.adrenalinici.adrenaline.common.view.PlayerChosenEvent;
import com.adrenalinici.adrenaline.server.controller.DecoratedAlternativeEffectGun;
import com.adrenalinici.adrenaline.server.controller.GunLoader;
import com.adrenalinici.adrenaline.server.controller.nodes.BaseNodeTest;
import com.adrenalinici.adrenaline.server.flow.FlowNode;
import com.adrenalinici.adrenaline.server.model.PlayerDashboard;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class ApplyScopeFlowNodeTest extends BaseNodeTest {
  @Override
  public void setUp() {
    super.setUp();
  }

  @Override
  public FlowNode nodeToTest() {
    return new ApplyScopeFlowNode();
  }

  @Test
  public void scopeEffectivelyAvailableTest() {
    context.setTurnOfPlayer(PlayerColor.GREEN);

    model.getDashboard().getDashboardCell(Position.of(0, 0)).addPlayer(PlayerColor.GREEN);
    model.getDashboard().getDashboardCell(Position.of(0, 1)).addPlayer(PlayerColor.GRAY);
    model.getDashboard().getDashboardCell(Position.of(1, 1)).addPlayer(PlayerColor.YELLOW);
    model.getDashboard().getDashboardCell(Position.of(1, 2)).addPlayer(PlayerColor.CYAN);

    PlayerDashboard killerPlayerDashboard = model.getPlayerDashboard(context.getTurnOfPlayer());
    killerPlayerDashboard.removeAmmos(Arrays.asList(AmmoColor.RED, AmmoColor.BLUE, AmmoColor.YELLOW));
    killerPlayerDashboard.addAmmo(AmmoColor.YELLOW);

    PowerUpCard blueScope = new PowerUpCard(AmmoColor.RED, PowerUpType.SCOPE);
    killerPlayerDashboard.addPowerUpCard(blueScope);

    DecoratedAlternativeEffectGun gun = (DecoratedAlternativeEffectGun) GunLoader.INSTANCE.getDecoratedGun("zx2");
    AlternativeEffectGunFlowState gunFlowState =
      new AlternativeEffectGunFlowStateImpl(gun).setChosenEffect(gun.getFirstEffect(), true);

    gunFlowState.hitPlayer(PlayerColor.GRAY, 1);
    gunFlowState.hitPlayer(PlayerColor.YELLOW, 1);

    context.nextPhase(viewMock, gunFlowState);

    ArgumentCaptor<List<PlayerColor>> playersCaptor = ArgumentCaptor.forClass(List.class);
    verify(viewMock, times(1)).showScopePlayers(playersCaptor.capture());
    assertThat(playersCaptor.getValue())
      .containsExactlyInAnyOrder(PlayerColor.GRAY, PlayerColor.YELLOW);

    context.handleEvent(new PlayerChosenEvent(PlayerColor.GRAY), viewMock);

    assertThat(model.getPlayerDashboard(PlayerColor.GRAY).getDamages())
      .isEqualTo(Collections.nCopies(1, PlayerColor.GREEN));

    assertThat(killerPlayerDashboard.getAmmos().isEmpty()).isTrue();
    assertThat(killerPlayerDashboard.getPowerUpCards().isEmpty()).isTrue();

    checkEndCalled();
  }


}
