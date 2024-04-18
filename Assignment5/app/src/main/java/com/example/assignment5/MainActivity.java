package com.example.assignment5;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final int[] images = {
            R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4,
            R.drawable.image5, R.drawable.image6, R.drawable.image7, R.drawable.image8,
            R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4,
            R.drawable.image5, R.drawable.image6, R.drawable.image7, R.drawable.image8
    };

    private ImageView[] cardViews = new ImageView[16];
    private int[] cardValues = new int[16];
    private int card1Index = -1;
    private int card2Index = -1;
    private int pairsFound = 0;

    // Player records
    private PlayerRecords playerRecords = new PlayerRecords();

    // Timer
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve the GridLayout from the layout
        GridLayout gridLayout = findViewById(R.id.gridLayout);

        // Set up the cards and grid layout
        setupGridLayout(gridLayout);

        // Start the game
        startGame();
    }

    private void setupGridLayout(GridLayout gridLayout) {
        // Set up the GridLayout parameters
        gridLayout.post(new Runnable() {
            @Override
            public void run() {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int screenWidth = displayMetrics.widthPixels;
                int screenHeight = displayMetrics.heightPixels;
                int cardWidth = screenWidth / 4; // 4 columns
                int cardHeight = screenHeight / 4; // 4 rows
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = GridLayout.LayoutParams.WRAP_CONTENT;
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.setMargins(8, 8, 8, 8);

                // Create and add ImageView objects to the GridLayout
                for (int i = 0; i < 16; i++) {
                    ImageView imageView = new ImageView(MainActivity.this);
                    imageView.setImageResource(R.drawable.placeholder_image);
                    imageView.setLayoutParams(new GridLayout.LayoutParams(
                            GridLayout.spec(GridLayout.UNDEFINED, 1f),
                            GridLayout.spec(GridLayout.UNDEFINED, 1f)));
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                    imageView.setOnClickListener(new CardClickListener(i));
                    cardViews[i] = imageView;
                    gridLayout.addView(imageView);
                }
            }
        });
    }

    private void startGame() {
        shuffleCards();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Lifecycle", "MainActivity: onResume - playerRecords size: " + playerRecords.getPlayerRecords().size());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Lifecycle", "MainActivity: onPause");
    }

    private void shuffleCards() {
        // Fisher-Yates shuffle algorithm
        Random rand = new Random();
        for (int i = images.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            int temp = images[index];
            images[index] = images[i];
            images[i] = temp;
        }

        // Assigning image indices
        for (int i = 0; i < 16; i++) {
            cardValues[i] = i % 8;
        }
    }

    private void endGame(boolean playerWins) {
        String message;
        if (playerWins) {
            message = "You win!";
        } else {
            message = "You lose!";
        }

        // Display the message with an animation
        TextView messageTextView = findViewById(R.id.messageTextView);
        messageTextView.setText(message);
        messageTextView.setVisibility(View.VISIBLE); // Make the text view visible

        // Apply animation to the text view
        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.text_scale_animation);
        messageTextView.startAnimation(animation);

        // Delay showing the prompt for player name
        new Handler().postDelayed(() -> promptForPlayerName(playerWins), 2000); // Delay for 2 seconds (adjust as needed)
    }



    private void promptForPlayerName(boolean playerWins) {
        // Prompt for player name
        EditText input = new EditText(this);
        input.setHint("Enter your name");
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setView(input);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", (dialog, which) -> {
            String playerName = input.getText().toString().trim();
            updatePlayerRecords(playerName, playerWins);
            showStatsActivity();
        });
        builder.show();
    }

    private void updatePlayerRecords(String playerName, boolean playerWins) {
        PlayerRecord playerRecord = playerRecords.getPlayerRecord(playerName);

        if (playerRecord != null) {
            playerRecord.incrementTimesPlayed();
            if (playerWins) {
                playerRecord.incrementWins();
            } else {
                playerRecord.incrementLosses();
            }
        } else {
            // If the player record doesn't exist, create a new one
            playerRecord = new PlayerRecord(playerName);
            if (playerWins) {
                playerRecord.incrementWins();
            } else {
                playerRecord.incrementLosses();
            }
            playerRecords.addPlayerRecord(playerRecord); // Add the new player record to the playerRecords object
        }
        // Log the updates for verification
        Log.d("PlayerRecords", "Player: " + playerName + ", Times Played: " + playerRecord.getTimesPlayed() + ", Wins: " + playerRecord.getWins() + ", Losses: " + playerRecord.getLosses());
    }


    private void showStatsActivity() {
        Intent intent = new Intent(this, StatsActivity.class);
        intent.putExtra("playerRecords", playerRecords);
        startActivity(intent);
    }

    private void checkMatch() {
        if (card1Index != -1 && card2Index != -1) {
            if (cardValues[card1Index] == cardValues[card2Index]) {
                pairsFound++;
                if (pairsFound == 8) {
                    endGame(true);
                }
            } else {
                // Cards don't match, revert the images back to placeholder after a delay
                new Handler().postDelayed(() -> {
                    cardViews[card1Index].setImageResource(R.drawable.placeholder_image);
                    cardViews[card2Index].setImageResource(R.drawable.placeholder_image);
                }, 1000); // Delay for 1 second (adjust as needed)
            }
            card1Index = -1;
            card2Index = -1;
        }
    }

    private class CardClickListener implements View.OnClickListener {
        private int index;

        CardClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            if (card1Index == index || card2Index == index) {
                // Ignore if the same card is clicked twice
                return;
            }

            if (card1Index == -1) {
                card1Index = index;
                cardViews[index].setImageResource(images[cardValues[index]]);
            } else if (card2Index == -1) {
                card2Index = index;
                cardViews[index].setImageResource(images[cardValues[index]]);
                checkMatch();
            }

            // Apply animation to the clicked card view
            Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.button_scale_animation);
            v.startAnimation(animation);
        }
    }
}
