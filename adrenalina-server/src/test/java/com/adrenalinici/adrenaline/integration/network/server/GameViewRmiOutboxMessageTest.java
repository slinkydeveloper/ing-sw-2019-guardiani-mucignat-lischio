package com.adrenalinici.adrenaline.integration.network.server;

import com.adrenalinici.adrenaline.common.model.*;
import com.adrenalinici.adrenaline.common.network.outbox.InfoType;
import com.adrenalinici.adrenaline.integration.network.outbox.OutboxMockData;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class GameViewRmiOutboxMessageTest extends BaseGameViewRmiIntegrationTest {

  @Override
  public void setUp() throws IOException, InterruptedException {
    super.setUp();

    mockedClientView.sendChosenMatch("test-match", PlayerColor.YELLOW);

    sleep(2);
  }

  @Test
  public void showAvailableActionsTest() throws IOException, InterruptedException {
    remoteView.showAvailableActions(OutboxMockData.ACTIONS);

    sleep();

    ArgumentCaptor<List<Action>> actionsCaptor = ArgumentCaptor.forClass(List.class);
    verify(mockedClientView, times(1)).showAvailableActions(actionsCaptor.capture());

    assertThat(actionsCaptor.getValue()).isEqualTo(OutboxMockData.ACTIONS);
  }

  @Test
  public void showAvailableMovementsTest() throws IOException, InterruptedException {
    remoteView.showAvailableMovements(OutboxMockData.POSITIONS);

    sleep();

    ArgumentCaptor<List<Position>> positionsCaptor = ArgumentCaptor.forClass(List.class);
    verify(mockedClientView, times(1)).showAvailableMovements(positionsCaptor.capture());

    assertThat(positionsCaptor.getValue()).isEqualTo(OutboxMockData.POSITIONS);
  }

  @Test
  public void showReloadableGunsTest() throws IOException, InterruptedException {
    remoteView.showReloadableGuns(OutboxMockData.GUNS);

    sleep();

    ArgumentCaptor<Set<String>> gunsCaptor = ArgumentCaptor.forClass(Set.class);
    verify(mockedClientView, times(1)).showReloadableGuns(gunsCaptor.capture());

    assertThat(gunsCaptor.getValue()).isEqualTo(OutboxMockData.GUNS);
  }

  @Test
  public void showAvailablePowerUpCardsForRespawnTest() throws IOException, InterruptedException {
    remoteView.showAvailablePowerUpCardsForRespawn(OutboxMockData.PLAYER, OutboxMockData.POWER_UP_CARDS);

    sleep();

    ArgumentCaptor<PlayerColor> playerCaptor = ArgumentCaptor.forClass(PlayerColor.class);
    ArgumentCaptor<List<PowerUpCard>> powerUpCardsCaptor = ArgumentCaptor.forClass(List.class);
    verify(mockedClientView, times(1)).showAvailablePowerUpCardsForRespawn(playerCaptor.capture(), powerUpCardsCaptor.capture());

    assertThat(playerCaptor.getValue()).isEqualTo(OutboxMockData.PLAYER);
    assertThat(powerUpCardsCaptor.getValue()).isEqualTo(OutboxMockData.POWER_UP_CARDS);
  }

  @Test
  public void showAvailableAlternativeEffectsGunTest() throws IOException, InterruptedException {
    remoteView.showAvailableAlternativeEffectsGun(OutboxMockData.FIRST_EFFECT, OutboxMockData.SECOND_EFFECT);

    sleep();

    ArgumentCaptor<Effect> firstEffectCaptor = ArgumentCaptor.forClass(Effect.class);
    ArgumentCaptor<Effect> secondEffectCaptor = ArgumentCaptor.forClass(Effect.class);
    verify(mockedClientView, times(1)).showAvailableAlternativeEffectsGun(firstEffectCaptor.capture(), secondEffectCaptor.capture());

    assertThat(firstEffectCaptor.getValue()).isEqualTo(OutboxMockData.FIRST_EFFECT);
    assertThat(secondEffectCaptor.getValue()).isEqualTo(OutboxMockData.SECOND_EFFECT);
  }

  @Test
  public void showChoosePlayerToHitTest() throws IOException, InterruptedException {
    remoteView.showChoosePlayerToHit(OutboxMockData.PLAYERS);

    sleep();

    ArgumentCaptor<List<PlayerColor>> playersCaptor = ArgumentCaptor.forClass(List.class);
    verify(mockedClientView, times(1)).showChoosePlayerToHit(playersCaptor.capture());

    assertThat(playersCaptor.getValue()).isEqualTo(OutboxMockData.PLAYERS);
  }

  @Test
  public void showAvailableExtraEffectsTest() throws IOException, InterruptedException {
    remoteView.showAvailableExtraEffects(OutboxMockData.FIRST_EXTRA_EFFECT, OutboxMockData.SECOND_EXTRA_EFFECT);

    sleep();

    ArgumentCaptor<Effect> firstExtraEffectCaptor = ArgumentCaptor.forClass(Effect.class);
    ArgumentCaptor<Effect> secondExtraEffectCaptor = ArgumentCaptor.forClass(Effect.class);
    verify(mockedClientView, times(1)).showAvailableExtraEffects(firstExtraEffectCaptor.capture(), secondExtraEffectCaptor.capture());

    assertThat(firstExtraEffectCaptor.getValue()).isEqualTo(OutboxMockData.FIRST_EXTRA_EFFECT);
    assertThat(secondExtraEffectCaptor.getValue()).isEqualTo(OutboxMockData.SECOND_EXTRA_EFFECT);
  }

  @Test
  public void showAvailableGunsTest() throws IOException, InterruptedException {
    remoteView.showAvailableGuns(OutboxMockData.GUNS);

    sleep();

    ArgumentCaptor<Set<String>> gunsCaptor = ArgumentCaptor.forClass(Set.class);
    verify(mockedClientView, times(1)).showAvailableGuns(gunsCaptor.capture());

    assertThat(gunsCaptor.getValue()).isEqualTo(OutboxMockData.GUNS);
  }

  @Test
  public void showAvailableGunsToPickupTest() throws IOException, InterruptedException {
    remoteView.showAvailableGunsToPickup(OutboxMockData.GUNS);

    sleep();

    ArgumentCaptor<Set<String>> gunsCaptor = ArgumentCaptor.forClass(Set.class);
    verify(mockedClientView, times(1)).showAvailableGunsToPickup(gunsCaptor.capture());

    assertThat(gunsCaptor.getValue()).isEqualTo(OutboxMockData.GUNS);
  }

  @Test
  public void showAvailableTagbackGrenadeTest() throws IOException, InterruptedException {
    remoteView.showAvailableTagbackGrenade(OutboxMockData.PLAYER, OutboxMockData.POWER_UP_CARDS);

    sleep();

    ArgumentCaptor<PlayerColor> playerCaptor = ArgumentCaptor.forClass(PlayerColor.class);
    ArgumentCaptor<List<PowerUpCard>> powerUpCardsCaptor = ArgumentCaptor.forClass(List.class);
    verify(mockedClientView, times(1)).showAvailableTagbackGrenade(playerCaptor.capture(), powerUpCardsCaptor.capture());

    assertThat(playerCaptor.getValue()).isEqualTo(OutboxMockData.PLAYER);
    assertThat(powerUpCardsCaptor.getValue()).isEqualTo(OutboxMockData.POWER_UP_CARDS);
  }

  @Test
  public void showAvailableRoomsTest() throws IOException, InterruptedException {
    remoteView.showAvailableRooms(OutboxMockData.ROOMS);

    sleep();
    //Thread.sleep(500);

    ArgumentCaptor<Set<CellColor>> roomsCaptor = ArgumentCaptor.forClass(Set.class);
    verify(mockedClientView, times(1)).showAvailableRooms(roomsCaptor.capture());

    assertThat(roomsCaptor.getValue()).isEqualTo(OutboxMockData.ROOMS);
  }

  @Test
  public void showAvailableCellsToHitTest() throws IOException, InterruptedException {
    remoteView.showAvailableCellsToHit(OutboxMockData.CELLS);

    sleep();
    //Thread.sleep(500);

    ArgumentCaptor<Set<Position>> cellsCaptor = ArgumentCaptor.forClass(Set.class);
    verify(mockedClientView, times(1)).showAvailableCellsToHit(cellsCaptor.capture());

    assertThat(cellsCaptor.getValue()).isEqualTo(OutboxMockData.CELLS);
  }

  @Test
  public void showInfoMessageERROR() throws IOException, InterruptedException {
    reset(mockedClientView);

    mockedClientView.sendStartNewMatch("test-match", DashboardChoice.SMALL, PlayersChoice.THREE, RulesChoice.SIMPLE);

    sleep(2);

    ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<InfoType> infoTypeCaptor = ArgumentCaptor.forClass(InfoType.class);
    verify(mockedClientView, times(1))
      .showInfoMessage(stringCaptor.capture(), infoTypeCaptor.capture());

    assertThat(infoTypeCaptor.getValue()).isEqualTo(InfoType.ERROR);
  }

}
