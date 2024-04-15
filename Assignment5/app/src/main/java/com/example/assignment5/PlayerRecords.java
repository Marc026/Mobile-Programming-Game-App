package com.example.assignment5;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayerRecords implements Serializable {
    private List<PlayerRecord> playerRecords;

    public PlayerRecords() {
        this.playerRecords = new ArrayList<>();
    }

    public void addPlayerRecord(PlayerRecord playerRecord) {
        playerRecords.add(playerRecord);
    }

    public PlayerRecord getPlayerRecord(String playerName) {
        for (PlayerRecord record : playerRecords) {
            if (record.getPlayerName().equals(playerName)) {
                return record;
            }
        }
        return null; // Player record not found
    }
    public List<PlayerRecord> getPlayerRecords() {
        return playerRecords;
    }
}