package com.adrenalinici.adrenaline.network.server;

import com.adrenalinici.adrenaline.model.common.*;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.adrenalinici.adrenaline.network.outbox.OutboxMockData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class GameViewSocketOutboxMessageTest extends BaseGameViewSocketIntegrationTest {

  @Override
  public void setUp() throws IOException, InterruptedException {
    super.setUp();

    mockedClientView.sendChosenMatch("test-match", PlayerColor.YELLOW);

    sleep(2);
  }

  @Test
  public void showAvailableActionsTest() throws IOException, InterruptedException {
    remoteView.showAvailableActions(ACTIONS);

    sleep();

    ArgumentCaptor<List<Action>> actionsCaptor = ArgumentCaptor.forClass(List.class);
    verify(mockedClientView, times(1)).showAvailableActions(actionsCaptor.capture());

    assertThat(actionsCaptor.getValue()).isEqualTo(ACTIONS);
  }

  @Test
  public void showAvailableMovementsTest() throws IOException, InterruptedException {
    remoteView.showAvailableMovements(POSITIONS);

    sleep();

    ArgumentCaptor<List<Position>> positionsCaptor = ArgumentCaptor.forClass(List.class);
    verify(mockedClientView, times(1)).showAvailableMovements(positionsCaptor.capture());

    assertThat(positionsCaptor.getValue()).isEqualTo(POSITIONS);
  }

  @Test
  public void showNextTurnTest() throws IOException, InterruptedException {
    remoteView.showNextTurn(PLAYER);

    sleep();

    ArgumentCaptor<PlayerColor> playerCaptor = ArgumentCaptor.forClass(PlayerColor.class);
    verify(mockedClientView, times(1)).showNextTurn(playerCaptor.capture());

    assertThat(playerCaptor.getValue()).isEqualTo(PLAYER);
  }

  @Test
  public void showReloadableGunsTest() throws IOException, InterruptedException {
    remoteView.showReloadableGuns(GUNS);

    sleep();

    ArgumentCaptor<Set<String>> gunsCaptor = ArgumentCaptor.forClass(Set.class);
    verify(mockedClientView, times(1)).showReloadableGuns(gunsCaptor.capture());

    assertThat(gunsCaptor.getValue()).isEqualTo(GUNS);
  }

  @Test
  public void showLoadedGunsTest() throws IOException, InterruptedException {
    remoteView.showLoadedGuns(GUNS);

    sleep();

    ArgumentCaptor<Set<String>> gunsCaptor = ArgumentCaptor.forClass(Set.class);
    verify(mockedClientView, times(1)).showLoadedGuns(gunsCaptor.capture());

    assertThat(gunsCaptor.getValue()).isEqualTo(GUNS);
  }

  @Test
  public void showBaseGunExtraEffectsTest() throws IOException, InterruptedException {
    remoteView.showBaseGunExtraEffects(EFFECTS);

    sleep();

    ArgumentCaptor<List<Effect>> effectsCaptor = ArgumentCaptor.forClass(List.class);
    verify(mockedClientView, times(1)).showBaseGunExtraEffects(effectsCaptor.capture());

    assertThat(effectsCaptor.getValue()).isEqualTo(EFFECTS);
  }

  @Test
  public void showAvailablePowerUpCardsForRespawnTest() throws IOException, InterruptedException {
    remoteView.showAvailablePowerUpCardsForRespawn(PLAYER, POWER_UP_CARDS);

    sleep();

    ArgumentCaptor<PlayerColor> playerCaptor = ArgumentCaptor.forClass(PlayerColor.class);
    ArgumentCaptor<List<PowerUpCard>> powerUpCardsCaptor = ArgumentCaptor.forClass(List.class);
    verify(mockedClientView, times(1)).showAvailablePowerUpCardsForRespawn(playerCaptor.capture(), powerUpCardsCaptor.capture());

    assertThat(playerCaptor.getValue()).isEqualTo(PLAYER);
    assertThat(powerUpCardsCaptor.getValue()).isEqualTo(POWER_UP_CARDS);
  }

  @Test
  public void showAvailableAlternativeEffectsGunTest() throws IOException, InterruptedException {
    remoteView.showAvailableAlternativeEffectsGun(FIRST_EFFECT, SECOND_EFFECT);

    sleep();

    ArgumentCaptor<Effect> firstEffectCaptor = ArgumentCaptor.forClass(Effect.class);
    ArgumentCaptor<Effect> secondEffectCaptor = ArgumentCaptor.forClass(Effect.class);
    verify(mockedClientView, times(1)).showAvailableAlternativeEffectsGun(firstEffectCaptor.capture(), secondEffectCaptor.capture());

    assertThat(firstEffectCaptor.getValue()).isEqualTo(FIRST_EFFECT);
    assertThat(secondEffectCaptor.getValue()).isEqualTo(SECOND_EFFECT);
  }

  @Test
  public void showChoosePlayerToHitTest() throws IOException, InterruptedException {
    remoteView.showChoosePlayerToHit(PLAYERS);

    sleep();

    ArgumentCaptor<List<PlayerColor>> playersCaptor = ArgumentCaptor.forClass(List.class);
    verify(mockedClientView, times(1)).showChoosePlayerToHit(playersCaptor.capture());

    assertThat(playersCaptor.getValue()).isEqualTo(PLAYERS);
  }

  @Test
  public void showChoosePlayerToMoveTest() throws IOException, InterruptedException {
    remoteView.showChoosePlayerToMove(AVAILABLE_MOVEMENTS);

    sleep();

    ArgumentCaptor<Map<PlayerColor, List<Position>>> availableMovementsCaptor = ArgumentCaptor.forClass(Map.class);
    verify(mockedClientView, times(1)).showChoosePlayerToMove(availableMovementsCaptor.capture());

    assertThat(availableMovementsCaptor.getValue()).isEqualTo(AVAILABLE_MOVEMENTS);
  }

  @Test
  public void showAvailableExtraEffectsTest() throws IOException, InterruptedException {
    remoteView.showAvailableExtraEffects(FIRST_EXTRA_EFFECT, SECOND_EXTRA_EFFECT);

    sleep();

    ArgumentCaptor<Effect> firstExtraEffectCaptor = ArgumentCaptor.forClass(Effect.class);
    ArgumentCaptor<Effect> secondExtraEffectCaptor = ArgumentCaptor.forClass(Effect.class);
    verify(mockedClientView, times(1)).showAvailableExtraEffects(firstExtraEffectCaptor.capture(), secondExtraEffectCaptor.capture());

    assertThat(firstExtraEffectCaptor.getValue()).isEqualTo(FIRST_EXTRA_EFFECT);
    assertThat(secondExtraEffectCaptor.getValue()).isEqualTo(SECOND_EXTRA_EFFECT);
  }

  @Test
  public void showAvailableGunsTest() throws IOException, InterruptedException {
    remoteView.showAvailableGuns(GUNS);

    sleep();

    ArgumentCaptor<Set<String>> gunsCaptor = ArgumentCaptor.forClass(Set.class);
    verify(mockedClientView, times(1)).showAvailableGuns(gunsCaptor.capture());

    assertThat(gunsCaptor.getValue()).isEqualTo(GUNS);
  }

  @Test
  public void showAvailableGunsToPickupTest() throws IOException, InterruptedException {
    remoteView.showAvailableGunsToPickup(GUNS);

    sleep();

    ArgumentCaptor<Set<String>> gunsCaptor = ArgumentCaptor.forClass(Set.class);
    verify(mockedClientView, times(1)).showAvailableGunsToPickup(gunsCaptor.capture());

    assertThat(gunsCaptor.getValue()).isEqualTo(GUNS);
  }

  @Test
  public void showAvailableTagbackGrenadeTest() throws IOException, InterruptedException {
    remoteView.showAvailableTagbackGrenade(PLAYER, POWER_UP_CARDS);

    sleep();

    ArgumentCaptor<PlayerColor> playerCaptor = ArgumentCaptor.forClass(PlayerColor.class);
    ArgumentCaptor<List<PowerUpCard>> powerUpCardsCaptor = ArgumentCaptor.forClass(List.class);
    verify(mockedClientView, times(1)).showAvailableTagbackGrenade(playerCaptor.capture(), powerUpCardsCaptor.capture());

    assertThat(playerCaptor.getValue()).isEqualTo(PLAYER);
    assertThat(powerUpCardsCaptor.getValue()).isEqualTo(POWER_UP_CARDS);
  }

}
