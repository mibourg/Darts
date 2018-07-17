package com.example.michel.darts;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int startingScore;

    int scoreForPlayerA;
    int scoreForPlayerB;

    String winner = null;

    boolean playerATurn = true;

    TextView scoreForATextView;
    TextView scoreForBTextView;

    TextView whoseTurnItIsTextView;
    EditText scoreToSubtractEditText;
    Button subtractScoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreForATextView = (TextView) findViewById(R.id.tv_a_score);
        scoreForBTextView = (TextView) findViewById(R.id.tv_b_score);

        whoseTurnItIsTextView = (TextView) findViewById(R.id.tv_whose_turn);
        scoreToSubtractEditText = (EditText) findViewById(R.id.et_score_to_subtract);
        subtractScoreButton = (Button) findViewById(R.id.btn_subtract);

        scoreToSubtractEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    subtractScoreButton.performClick();
                    return true;
                }
                return false;
            }
        });

        startingScore = 301;

        scoreForPlayerA = startingScore;
        scoreForPlayerB = startingScore;
    }

    public void removePoints(View view) {
        String textInEditText = scoreToSubtractEditText.getText().toString();
        int pointsScoredOnTurn = 0;
        if (!textInEditText.isEmpty()) {
            try {
                pointsScoredOnTurn = Integer.valueOf(textInEditText);
            } catch (NumberFormatException e) {
                Log.e("NumberFormatException", "Failed to format the entered number. Must be between −2,147,483,648 and 2,147,483,647.");
                if (playerATurn) {
                    Toast.makeText(MainActivity.this, R.string.player_a_busted, Toast.LENGTH_LONG).show();
                    playerATurn = false;
                } else {
                    Toast.makeText(MainActivity.this, R.string.player_b_busted, Toast.LENGTH_LONG).show();
                    playerATurn = true;
                }
            }
        }

        if (!textInEditText.isEmpty()) {
            if (playerATurn) {
                if (scoreForPlayerA - pointsScoredOnTurn >= 0) {
                    removePointsFromAScore(pointsScoredOnTurn);
                    checkWinForA();
                    updateScoreForATextView();
                    playerATurn = false;
                } else {
                    Toast.makeText(MainActivity.this, R.string.player_a_busted, Toast.LENGTH_LONG).show();
                    playerATurn = false;
                }
            } else {
                if (scoreForPlayerB - pointsScoredOnTurn >= 0) {
                    removePointsFromBScore(pointsScoredOnTurn);
                    checkWinForB();
                    updateScoreForBTextView();
                    playerATurn = true;
                } else {
                    Toast.makeText(MainActivity.this, R.string.player_b_busted, Toast.LENGTH_LONG).show();
                    playerATurn = true;
                }
            }
            if (winner == null) {
                updateWhoseTurnItIsTextView();
            }
            scoreToSubtractEditText.getText().clear();
        }
    }

    public void removePointsFromAScore(int pointsToRemove) {
        scoreForPlayerA -= pointsToRemove;
    }

    public void removePointsFromBScore(int pointsToRemove) {
        scoreForPlayerB -= pointsToRemove;
    }

    public void updateScoreForATextView() {
        if (winner == null) {
            scoreForATextView.setText(String.valueOf(scoreForPlayerA));
        } else if (winner.equals("A")) {
            scoreForATextView.setText("0");
        }
    }

    public void updateScoreForBTextView() {
        if (winner == null) {
            scoreForBTextView.setText(String.valueOf(scoreForPlayerB));
        } else if (winner.equals("B")) {
            scoreForBTextView.setText("0");
        }
    }

    public void updateWhoseTurnItIsTextView() {
        if (playerATurn) {
            whoseTurnItIsTextView.setText(R.string.player_a_turn);
            int colorForPlayerA = ContextCompat.getColor(MainActivity.this, R.color.colorForPlayerA);
            whoseTurnItIsTextView.setTextColor(colorForPlayerA);
            scoreToSubtractEditText.getBackground().setColorFilter(colorForPlayerA, PorterDuff.Mode.SRC_ATOP);
        } else {
            whoseTurnItIsTextView.setText(R.string.player_b_turn);
            int colorForPlayerB = ContextCompat.getColor(MainActivity.this, R.color.colorForPlayerB);
            whoseTurnItIsTextView.setTextColor(colorForPlayerB);
            scoreToSubtractEditText.getBackground().setColorFilter(colorForPlayerB, PorterDuff.Mode.SRC_ATOP);
        }
    }

    public void checkWinForA() {
        if (scoreForPlayerA == 0) {
            int playerAWonStringRes = R.string.player_a_won;
            Toast.makeText(MainActivity.this, playerAWonStringRes, Toast.LENGTH_LONG).show();
            winner = "A";
            whoseTurnItIsTextView.setText(playerAWonStringRes);
            scoreToSubtractEditText.setEnabled(false);
            subtractScoreButton.setEnabled(false);
        }
    }

    public void checkWinForB() {
        if (scoreForPlayerB == 0) {
            int playerBWonStringRes = R.string.player_b_won;
            Toast.makeText(MainActivity.this, playerBWonStringRes, Toast.LENGTH_LONG).show();
            winner = "B";
            whoseTurnItIsTextView.setText(playerBWonStringRes);
            scoreToSubtractEditText.setEnabled(false);
            subtractScoreButton.setEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.reset_game) {
            scoreForPlayerA = startingScore;
            scoreForPlayerB = startingScore;
            playerATurn = true;
            winner = null;
            updateWhoseTurnItIsTextView();
            updateScoreForATextView();
            updateScoreForBTextView();
            scoreToSubtractEditText.setEnabled(true);
            subtractScoreButton.setEnabled(true);
        }
        return true;
    }
}
