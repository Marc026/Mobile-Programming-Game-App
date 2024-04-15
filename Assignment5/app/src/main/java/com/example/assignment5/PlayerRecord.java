package com.example.assignment5;

import java.io.Serializable;

public class PlayerRecord implements Serializable {
    private String playerName;
    private int wins;
    private int losses;
    private int timesPlayed;

    public PlayerRecord(String playerName) {
        this.playerName = playerName;
        this.wins = 0;
        this.losses = 0;
        this.timesPlayed = 0;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getWins() {
        return wins;
    }

    public void incrementWins() {
        this.wins++;
    }

    public int getLosses() {
        return losses;
    }

    public void incrementLosses() {
        this.losses++;
    }

    public int getTimesPlayed() {
        return timesPlayed;
    }

    public void incrementTimesPlayed() {
        this.timesPlayed++;
    }
}
