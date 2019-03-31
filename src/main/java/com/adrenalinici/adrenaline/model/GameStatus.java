package com.adrenalinici.adrenaline.model;

import java.util.*;

import static java.util.Map.*;

public class GameStatus {
    private List<Map.Entry<PlayerColor, Boolean>> killScore;
    private int remainingSkulls;
    private List<PlayerColor> doubleKillScore;
    private PlayerColor roundPlayer;

    public GameStatus(int remainingSkulls, PlayerColor roundPlayer) {
        this.remainingSkulls = remainingSkulls;
        this.roundPlayer = roundPlayer;
        this.killScore = new ArrayList<>();
        this.doubleKillScore = new ArrayList<>();
    }

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
        // gli passo il giocatore che esegue il colpo NON QUELLO CHE LO RICEVE
        Entry<PlayerColor, Boolean> e = new AbstractMap.SimpleImmutableEntry<>(playerColor, cruelKill);
        killScore.add(e);
    }
}
