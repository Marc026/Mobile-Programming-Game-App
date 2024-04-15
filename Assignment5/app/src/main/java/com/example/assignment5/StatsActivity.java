package com.example.assignment5;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class StatsActivity extends AppCompatActivity {

    private TextView statsTextView;
    private Button playAgainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        statsTextView = findViewById(R.id.statsTextView);
        playAgainButton = findViewById(R.id.playAgainButton);

        PlayerRecords playerRecords = (PlayerRecords) getIntent().getSerializableExtra("playerRecords");

        updateStatsView(playerRecords);

        playAgainButton.setOnClickListener(v -> {
            startActivity(new Intent(StatsActivity.this, MainActivity.class));
            finish();
        });
    }

    private void updateStatsView(PlayerRecords playerRecords) {
        if (playerRecords != null) {
            List<PlayerRecord> records = playerRecords.getPlayerRecords();
            if (records != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for (PlayerRecord playerRecord : records) {
                    stringBuilder.append(getString(R.string.player_name)).append(playerRecord.getPlayerName()).append("\n")
                            .append(getString(R.string.wins)).append(playerRecord.getWins()).append("\n")
                            .append(getString(R.string.losses)).append(playerRecord.getLosses()).append("\n")
                            .append(getString(R.string.times_played)).append(playerRecord.getTimesPlayed()).append("\n\n");
                    Log.d("PlayerStats", "Player: " + playerRecord.getPlayerName() + ", Times Played: " + playerRecord.getTimesPlayed() + ", Wins: " + playerRecord.getWins() + ", Losses: " + playerRecord.getLosses());
                }
                statsTextView.setText(stringBuilder.toString());
            } else {
                Log.e("StatsActivity", "Player records list is null.");
                statsTextView.setText(getString(R.string.no_player_records_found));
            }
        } else {
            Log.e("StatsActivity", "Player records object is null.");
            statsTextView.setText(getString(R.string.no_player_records_found));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PlayerRecords playerRecords = (PlayerRecords) getIntent().getSerializableExtra("playerRecords");
        updateStatsView(playerRecords);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Lifecycle", "StatsActivity: onPause");
    }
}
