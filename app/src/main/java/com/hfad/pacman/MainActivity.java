package com.hfad.pacman;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // MEMBER FIELDS AND PROPERTIES
    // Reference to the main view
    GameView gameView;

    // Reference to the game class.
    Game game;

    // Stores the buttons from the layout
    public Button buttonUp;
    public Button buttonLeft;
    public Button buttonRight;
    public Button buttonDown;

    public ImageButton pauseButton;
    public ImageButton continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The screen should only show in portrait mode (not horizontal as well)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Setting the layout
        setContentView(R.layout.activity_main);

        // Variables storing the gameView and textviews in the layout
        gameView =  findViewById(R.id.gameView);
        TextView textView = findViewById(R.id.points);
        TextView timeText = findViewById(R.id.gameTimer);
        TextView levelText = findViewById(R.id.level);

        // Creating a new game - sets the context and the textviews
        game = new Game(this, textView, timeText, levelText);

        // Associating the gameView with the newly created game
        game.setGameView(gameView);
        gameView.setGame(game);

        // Setting the level
        game.setLevel(1);

        // Starting PacmanTimer
        game.startPacmanTimer();

        // Finding the pause and continue button
        pauseButton = findViewById(R.id.pauseButton);
        continueButton = findViewById(R.id.continueButton);

        // ClickListeners of the pause and continue buttons
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.running = false;
            }
        });
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.running = true;
            }
        });

        // Finding the buttons that moves Pacman
        buttonLeft = findViewById(R.id.moveLeft);
        buttonUp = findViewById(R.id.moveUp);
        buttonRight = findViewById(R.id.moveRight);
        buttonDown = findViewById(R.id.moveDown);

        // ClickListeners of the move buttons that move Pacman
        buttonUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // When Up button is clicked, direction is set to UP (the pacman image rotates + PacmanTimer goes in that direction)
                game.setDirection(Game.PACBITMAP_UP);
                game.running = true;
            }
        });
        buttonLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // When Left button is clicked, direction is set to LEFT (the pacman image rotates + PacmanTimer goes in that direction)
                game.setDirection(Game.PACBITMAP_LEFT);
                game.running = true;
            }
        });
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When Right button is clicked, direction is set to RIGHT (the pacman image rotates + PacmanTimer goes in that direction)
                game.setDirection(Game.PACBITMAP_RIGHT);
                game.running = true;
            }
        });
        buttonDown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // When Down button is clicked, direction is set to DOWN (the pacman image rotates + PacmanTimer goes in that direction)
                game.setDirection(Game.PACBITMAP_DOWN);
                game.running = true;
            }
        });

        // Calling the method that starts a new game
        game.newGame();
    }

    // DISABLE AND ENABLE BUTTONS ON CLOSE IN ALERTBOXES
    public void disableButtons() {
        buttonUp.setEnabled(false);
        buttonLeft.setEnabled(false);
        buttonRight.setEnabled(false);
        buttonDown.setEnabled(false);

        pauseButton.setEnabled(false);
        continueButton.setEnabled(false);
    }

    public void enableButtons() {
        buttonUp.setEnabled(true);
        buttonLeft.setEnabled(true);
        buttonRight.setEnabled(true);
        buttonDown.setEnabled(true);

        pauseButton.setEnabled(true);
        continueButton.setEnabled(true);
    }

    // The options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Making sure the game is paused when clicking the menu
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        game.running = false;
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this,"settings clicked",Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_newGame) {
            // Resetting the game and starting the new game
            game.setLevel(1);
            game.setTotalPoints(0);
            game.cancelPacmanTimer();
            game.cancelGameTimer();
            game.newGame();
            return true;
        } else if (id == R.id.action_shareScore) {
            // Calling the method for sharing score
            game.shareScore();
        }
        return super.onOptionsItemSelected(item);
    }
}
