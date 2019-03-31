package com.adrenalinici.adrenaline.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Map.*;

public class GameStatus {
    // TODO ci devo pure fare i testtttt
    private List<Map.Entry<PlayerColor, Boolean>> killScore;
    private int remainingSkulls;
    private List<PlayerColor> doubleKillScore;
    private PlayerColor roundPlayer;
    private Object Entry;

    public int getRemainingSkulls() {
        return remainingSkulls;
    }

    public List<PlayerColor> getDoubleKillScore() {
        return doubleKillScore;
    }

    public PlayerColor getRoundPlayer() {
        return roundPlayer;
    }

    public void setRoundPlayer(PlayerColor roundPlayer) {
        this.roundPlayer = roundPlayer;
    }

    public List<Map.Entry<PlayerColor, Boolean>> getKillScore() {
        return killScore;
    }

    public int decrementSkulls() {
        return remainingSkulls -= 1;
    }

    public void addDoubleKillScore(PlayerColor playerColor) {
        doubleKillScore.add(playerColor);
    }

    // +void addKillScore(PlayerColor playerColor, boolean cruelKill)
    public void addKillScore(PlayerColor playerColor, boolean cruelKill) {
        if (killScore.contains(playerColor)) {
            Set<Map.Entry<PlayerColor, Boolean>> entrySet = new Set<Map.Entry<PlayerColor, Boolean>>[];
            // TODO

        } else killScore.add(Map.Entry < playerColor, cruelKill >);
    }
}
