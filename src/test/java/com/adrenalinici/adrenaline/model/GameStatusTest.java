package com.adrenalinici.adrenaline.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameStatusTest {
  @Test
  public void addKillScoreTest() {
    GameStatus gameStatus = new GameStatus(8, null, null);
    gameStatus.addKillScore(PlayerColor.CYAN, false);
    assertEquals(1, gameStatus.getKillScore().size());
    assertEquals(PlayerColor.CYAN, gameStatus.getKillScore().get(0).getKey());
    assertEquals(false, gameStatus.getKillScore().get(0).getValue());
  }

  @Test
  public void inizializationTest() {
    GameStatus gameStatus = new GameStatus(8, null, null);
    assertEquals(8, gameStatus.getRemainingSkulls());
    assertTrue(gameStatus.getKillScore().isEmpty());
    assertTrue(gameStatus.getDoubleKillScore().isEmpty());
  }
}
