package com.adrenalinici.adrenaline.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class GameStatusTest {
  @Test
  public void addKillScoreTest() {
    GameStatus gameStatus = new GameStatus(8, null, null);
    gameStatus.addKillScore(PlayerColor.CYAN, false);
    assertThat(gameStatus.getKillScore()).hasSize(1);
    assertThat(gameStatus.getKillScore().get(0).getKey()).isEqualTo(PlayerColor.CYAN);
    assertThat(gameStatus.getKillScore().get(0).getValue()).isEqualTo(false);
  }

  @Test
  public void inizializationTest() {
    GameStatus gameStatus = new GameStatus(8, null, null);
    assertThat(gameStatus.getRemainingSkulls()).isEqualTo(8);
    assertThat(gameStatus.getKillScore().isEmpty()).isTrue();
    assertThat(gameStatus.getDoubleKillScore().isEmpty()).isTrue();
  }
}
