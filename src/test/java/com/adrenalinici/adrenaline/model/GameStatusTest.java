package com.adrenalinici.adrenaline.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class GameStatusTest {
    /*
     @Test
    public void addAmmoTest() {
        PlayerDashboard playerDashboard = new PlayerDashboard(false, Collections.emptyList());
        playerDashboard.addAmmmo(AmmoColor.RED);
        Assert.assertEquals(4L, (long)playerDashboard.getAmmos().size());
        MyAssertions.assertContainsExactly(AmmoColor.RED, 2, playerDashboard.getAmmos());
    }*/
    @Test
    public void addKillScoreTest() {
        // cosa voglio testare ? se ho già due punti da un giocatore possono diventare tre (praticamnete sono i proiettili ricevuti )
        // e devo testare se ricevo il cruel o no
        // gli passo il giocatore che esegue il colpo NON QUELLO CHE LO RICEVE
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
        boolean cruelKill = false;
        List<Map.Entry<PlayerColor, Boolean>> killScore = new ArrayList<>();
        List<PlayerColor> doubleKillScore = new ArrayList<>();
        GameStatus gameStatus = new GameStatus(8, playerColor);
        assertEquals(8, gameStatus.getRemainingSkulls());
        assertEquals(PlayerColor.GRAY, gameStatus.getRoundPlayer());
        assertTrue(killScore.isEmpty());
        assertTrue(doubleKillScore.isEmpty());
    }
}
