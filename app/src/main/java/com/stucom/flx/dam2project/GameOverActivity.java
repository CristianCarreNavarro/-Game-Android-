package com.stucom.flx.dam2project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    private Button startGameAgain;
    private TextView displayScore;
    private String score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        score = getIntent().getExtras().get("score").toString();
        startGameAgain = (Button) findViewById(R.id.play_again);
        displayScore = (TextView) findViewById(R.id.displayScore) ;

        startGameAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to gameOverIntent when lose
                Intent gameOverIntent = new Intent (GameOverActivity.this,MainActivity.class);
                startActivity(gameOverIntent);
            }
        });
        displayScore.setText("Your Score: " + score);
    }
}
