package com.adrenalinici.adrenaline.network.server;

import com.adrenalinici.adrenaline.model.*;
import com.adrenalinici.adrenaline.model.common.*;
import com.adrenalinici.adrenaline.network.outbox.*;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.*;

import static com.adrenalinici.adrenaline.network.outbox.OutboxMockData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class GameViewSocketOutboxMessageTest extends BaseGameViewSocketIntegrationTest {

  @Test
  public void showAvailableActionsTest() throws IOException, InterruptedException {
    serverGameView.showAvailableActions(ACTIONS);

    Thread.sleep(100);

    ArgumentCaptor<List<Action>> actionsCaptor = ArgumentCaptor.forClass(List.class);
    verify(mockedClientView, times(1)).showAvailableActions(actionsCaptor.capture());

    assertThat(actionsCaptor.getValue()).isEqualTo(ACTIONS);
  }

  @Test
  public void showAvailableMovementsTest() throws IOException, InterruptedException {
    serverGameView.showAvailableMovements(POSITIONS);

    Thread.sleep(100);

    ArgumentCaptor<List<Position>> positionsCaptor = ArgumentCaptor.forClass(List.class);
    verify(mockedClientView, times(1)).showAvailableMovements(positionsCaptor.capture());

    assertThat(positionsCaptor.getValue()).isEqualTo(POSITIONS);
  }

  @Test
  public void showNextTurnTest() throws IOException, InterruptedException {
    serverGameView.showNextTurn(PLAYER);

    Thread.sleep(100);

    ArgumentCaptor<PlayerColor> playerCaptor = ArgumentCaptor.forClass(PlayerColor.class);
    verify(mockedClientView, times(1)).showNextTurn(playerCaptor.capture());

    assertThat(playerCaptor.getValue()).isEqualTo(PLAYER);
  }

  @Test
  public void showReloadableGunsTest() throws IOException, InterruptedException {
    serverGameView.showReloadableGuns(GUNS);

    Thread.sleep(100);

    ArgumentCaptor<Set<String>> gunsCaptor = ArgumentCaptor.forClass(Set.class);
    verify(mockedClientView, times(1)).showReloadableGuns(gunsCaptor.capture());

    assertThat(gunsCaptor.getValue()).isEqualTo(GUNS);
  }

  @Test
  public void showLoadedGunsTest() throws IOException, InterruptedException {
    serverGameView.showLoadedGuns(GUNS);

    Thread.sleep(100);

    ArgumentCaptor<Set<String>> gunsCaptor = ArgumentCaptor.forClass(Set.class);
    verify(mockedClientView, times(1)).showLoadedGuns(gunsCaptor.capture());

    assertThat(gunsCaptor.getValue()).isEqualTo(GUNS);
  }

  @Test
  public void showBaseGunExtraEffectsTest() throws IOException, InterruptedException {
    serverGameView.showBaseGunExtraEffects(EFFECTS);

    Thread.sleep(100);

    ArgumentCaptor<List<Effect>> effectsCaptor = ArgumentCaptor.forClass(List.class);
    verify(mockedClientView, times(1)).showBaseGunExtraEffects(effectsCaptor.capture());

    assertThat(effectsCaptor.getValue()).isEqualTo(EFFECTS);
  }

  @Test
  public void showAvailableRespawnLocationsTest() throws IOException, InterruptedException {
    serverGameView.showAvailableRespawnLocations(RESPAWN_LOCATIONS);

    Thread.sleep(100);

    ArgumentCaptor<List<AmmoColor>> respawnLocationsCaptor = ArgumentCaptor.forClass(List.class);
    verify(mockedClientView, times(1)).showAvailableRespawnLocations(respawnLocationsCaptor.capture());

    assertThat(respawnLocationsCaptor.getValue()).isEqualTo(RESPAWN_LOCATIONS);
  }

  @Test
  public void showAvailableAlternativeEffectsGunTest() throws IOException, InterruptedException {
    serverGameView.showAvailableAlternativeEffectsGun(FIRST_EFFECT, SECOND_EFFECT);

    Thread.sleep(100);

    ArgumentCaptor<Effect> firstEffectCaptor = ArgumentCaptor.forClass(Effect.class);
    ArgumentCaptor<Effect> secondEffectCaptor = ArgumentCaptor.forClass(Effect.class);
    verify(mockedClientView, times(1)).showAvailableAlternativeEffectsGun(firstEffectCaptor.capture(), secondEffectCaptor.capture());

    assertThat(firstEffectCaptor.getValue()).isEqualTo(FIRST_EFFECT);
    assertThat(secondEffectCaptor.getValue()).isEqualTo(SECOND_EFFECT);
  }

  @Test
  public void showChoosePlayerToHitTest() throws IOException, InterruptedException {
    serverGameView.showChoosePlayerToHit(PLAYERS);

    Thread.sleep(100);

    ArgumentCaptor<List<PlayerColor>> playersCaptor = ArgumentCaptor.forClass(List.class);
    verify(mockedClientView, times(1)).showChoosePlayerToHit(playersCaptor.capture());

    assertThat(playersCaptor.getValue()).isEqualTo(PLAYERS);
  }

  @Test
  public void showChoosePlayerToMoveTest() throws IOException, InterruptedException {
    serverGameView.showChoosePlayerToMove(AVAILABLE_MOVEMENTS);

    Thread.sleep(100);

    ArgumentCaptor<Map<PlayerColor, List<Position>>> availableMovementsCaptor = ArgumentCaptor.forClass(Map.class);
    verify(mockedClientView, times(1)).showChoosePlayerToMove(availableMovementsCaptor.capture());

    assertThat(availableMovementsCaptor.getValue()).isEqualTo(AVAILABLE_MOVEMENTS);
  }

  @Test
  public void showAvailableExtraEffectsTest() throws IOException, InterruptedException {
    serverGameView.showAvailableExtraEffects(FIRST_EXTRA_EFFECT, SECOND_EXTRA_EFFECT);

    Thread.sleep(100);

    ArgumentCaptor<Effect> firstExtraEffectCaptor = ArgumentCaptor.forClass(Effect.class);
    ArgumentCaptor<Effect> secondExtraEffectCaptor = ArgumentCaptor.forClass(Effect.class);
    verify(mockedClientView, times(1)).showAvailableExtraEffects(firstExtraEffectCaptor.capture(), secondExtraEffectCaptor.capture());

    assertThat(firstExtraEffectCaptor.getValue()).isEqualTo(FIRST_EXTRA_EFFECT);
    assertThat(secondExtraEffectCaptor.getValue()).isEqualTo(SECOND_EXTRA_EFFECT);
  }

  @Test
  public void showAvailableVenomGranadesTest() throws IOException, InterruptedException {
    serverGameView.showAvailableVenomGranades(PLAYER);

    Thread.sleep(100);

    ArgumentCaptor<PlayerColor> playerCaptor = ArgumentCaptor.forClass(PlayerColor.class);
    verify(mockedClientView, times(1)).showAvailableVenomGranades(playerCaptor.capture());

    assertThat(playerCaptor.getValue()).isEqualTo(PLAYER);
  }

  @Test
  public void showAvailablePowerUpCardsTest() throws IOException, InterruptedException {
    serverGameView.showAvailablePowerUpCards(PLAYER);

    Thread.sleep(100);

    ArgumentCaptor<PlayerColor> playerCaptor = ArgumentCaptor.forClass(PlayerColor.class);
    verify(mockedClientView, times(1)).showAvailablePowerUpCards(playerCaptor.capture());

    assertThat(playerCaptor.getValue()).isEqualTo(PLAYER);
  }

  @Test
  public void showAvailableEnemyMovementsTest() throws IOException, InterruptedException {
    serverGameView.showAvailableEnemyMovements(POSITIONS);

    Thread.sleep(100);

    ArgumentCaptor<List<Position>> positionsCaptor = ArgumentCaptor.forClass(List.class);
    verify(mockedClientView, times(1)).showAvailableEnemyMovements(positionsCaptor.capture());

    assertThat(positionsCaptor.getValue()).isEqualTo(POSITIONS);
  }

  @Test
  public void showAvailableGunsTest() throws IOException, InterruptedException {
    serverGameView.showAvailableGuns(GUNS);

    Thread.sleep(100);

    ArgumentCaptor<Set<String>> gunsCaptor = ArgumentCaptor.forClass(Set.class);
    verify(mockedClientView, times(1)).showAvailableGuns(gunsCaptor.capture());

    assertThat(gunsCaptor.getValue()).isEqualTo(GUNS);
  }

  @Test
  public void showAvailableGunsToPickupTest() throws IOException, InterruptedException {
    serverGameView.showAvailableGunsToPickup(GUNS);

    Thread.sleep(100);

    ArgumentCaptor<Set<String>> gunsCaptor = ArgumentCaptor.forClass(Set.class);
    verify(mockedClientView, times(1)).showAvailableGunsToPickup(gunsCaptor.capture());

    assertThat(gunsCaptor.getValue()).isEqualTo(GUNS);
  }

}
