package com.adrenalinici.adrenaline.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameStatusTest {
  @Test
  public void addKillScoreTest() {
    PlayerColor playerColor = PlayerColor.CYAN;
    boolean cruelKill = false;
    GameStatus gameStatus = new GameStatus(8, playerColor);
    gameStatus.addKillScore(playerColor, cruelKill);
    assertEquals(1, gameStatus.getKillScore().size());
    assertEquals(PlayerColor.CYAN, gameStatus.getKillScore().get(0).getKey());
    assertEquals(false, gameStatus.getKillScore().get(0).getValue());
  }

  @Test
  public void inizializationTest() {
    PlayerColor playerColor = PlayerColor.GRAY;
    GameStatus gameStatus = new GameStatus(8, playerColor);
    assertEquals(8, gameStatus.getRemainingSkulls());
    assertEquals(PlayerColor.GRAY, gameStatus.getRoundPlayer());
    assertTrue(gameStatus.getKillScore().isEmpty());
    assertTrue(gameStatus.getDoubleKillScore().isEmpty());
  }
}
