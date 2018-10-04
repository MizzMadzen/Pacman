package com.hfad.pacman;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Game {

    // STATICS
    // Static Strings to help set directions for Pacman
    public static final String PACBITMAP_UP = "Up";
    public static final String PACBITMAP_LEFT = "Left";
    public static final String PACBITMAP_RIGHT = "Right";
    public static final String PACBITMAP_DOWN = "Down";

    // Static strings to help setting state of pacman image
    public static final String PACMAN_OPEN = "Open";
    public static final String PACMAN_CLOSE = "Close";

    // MEMBER FIELDS AND PROPERTIES
    // Context is a reference to the activity
    private Context context;

    // A reference to the game view
    private GameView gameView;

    // Height and width of screen
    private int screenHeight, screenWidth;

    // Will contain the bitmap of Pacman
    private Bitmap pacBitmap;
    public Bitmap getPacBitmap() { return pacBitmap; }
    public void setPacBitmap(Bitmap pacBitmap) { this.pacBitmap = pacBitmap; }

    // Will contain the direction of Pacman
    private String direction;
    public String getDirection() {
        return direction;
    }
    public void setDirection(String direction) { this.direction = direction; }

    // Will contain the state of Pacman image
    private String pacmanState;
    public String getPacmanState() { return pacmanState; }
    public void setPacmanState(String pacmanState) { this.pacmanState = pacmanState; }

    // Contains the value that helps making the Pacman Open and close bitmaps wait for a few bits
    private int bitmapWait;
    public int getBitmapWait() { return bitmapWait; }
    public void setBitmapWait(int bitmapWait) { this.bitmapWait = bitmapWait; }

    // Contains the points for each level and total points and the Textview reference to points
    private int points = 0;
    public int getPoints() { return points; }
    private int totalPoints = 0;
    public int getTotalPoints() { return totalPoints; }
    public void setTotalPoints(int totalPoints) { this.totalPoints = totalPoints; }
    private TextView pointsView;

    // The declaration of the value the gameTimer should countdown from and the Textview reference to seconds
    private int seconds;
    private TextView timerView;

    // The ints that contains the position of Pacman
    private int pacx, pacy;
    public int getPacx() { return pacx; }
    public int getPacy() { return pacy; }

    // The pixellength that Pacman and Enemies should move on timer
    private int pixels = 20;
    public int getPixels() { return pixels; }
    public void setPixels(int pixels) { this.pixels = pixels; }

    // The list of gold coins - initially empty
    private ArrayList<GoldCoin> coins = new ArrayList<>();
    public ArrayList<GoldCoin> getCoins() { return coins; }

    // The list of enemies - initially empty
    private ArrayList<Enemy> enemies = new ArrayList<>();
    public ArrayList<Enemy> getEnemies() { return enemies; }

    // Setting up the timer for moving Pacman and a boolean to check if Pacman is running
    public Timer pacmanTimer;
    public boolean running = false;

    // Setting up the gameTimer
    public Timer gameTimer;

    // Containing the level
    private int level;
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    private TextView levelText;


    // CONSTRUCTOR
    public Game(Context context, TextView textView, TextView timerView, TextView levelText) {
        this.context = context;
        this.pointsView = textView;
        this.timerView = timerView;
        this.levelText = levelText;
        pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_open);
    }


    // METHODS
    // Sets the view of the Game
    public void setGameView(GameView view)
    {
        this.gameView = view;
    }

    // Sets the height and width of canvas from gameView
    public void setSize(int h, int w) {
        this.screenHeight = h;
        this.screenWidth = w;
    }

    // Creating a random position both in width and height to place the coins and enemies at random places within the screen
    public int getRandomWidth() {
        Random rand = new Random();
        int maxXPos = this.screenWidth;
        int minXPos = 100;
        int randomWidth = rand.nextInt(maxXPos - 200) + minXPos;

        return randomWidth;
    }
    public int getRandomHeight() {
        Random rand = new Random();
        int maxYPos = this.screenHeight;
        int minYPos = 100;
        int randomHeight = rand.nextInt(maxYPos - 200) + minYPos;

        return randomHeight;
    }

    // Checking if objects (e.g. enemies and coins) are within Pacmans area (so they do not spawn on Pacman)
    public boolean isInsidePacman(Object object) {
        boolean insidePacman = false;

        if(object instanceof GoldCoin) {
            if(((GoldCoin) object).getCoinx() > (pacx - 50) && ((GoldCoin) object).getCoinx() < (pacx + getPacBitmap().getWidth() + 50)
                    && ((GoldCoin) object).getCoiny() > (pacy - 50) && ((GoldCoin) object).getCoiny() < (pacy + getPacBitmap().getHeight() + 50)) {
                insidePacman = true;
            }
        } else if(object instanceof Enemy) {
            if(((Enemy) object).getEnemyposx() > (pacx - ((Enemy) object).getEnemyBitmap().getWidth()) && ((Enemy) object).getEnemyposx() < (pacx + getPacBitmap().getWidth() + ((Enemy) object).getEnemyBitmap().getWidth())
                    && ((Enemy) object).getEnemyposy() > (pacy - (((Enemy) object).getEnemyBitmap().getHeight() *2)) && ((Enemy) object).getEnemyposy() < (pacy + getPacBitmap().getHeight() + ((Enemy) object).getEnemyBitmap().getHeight())) {
                insidePacman = true;
            }
        }

        return insidePacman;
    }

    // Creating coin objects and adding them to the GoldCoin ArrayList - They get random positions
    public void instantiateCoins(){

        int numberOfCoins = 0;

        // Setting how many coins should be in each level
        if(getLevel() == 1) {
            numberOfCoins = 10;
        } else if (getLevel() == 2) {
            numberOfCoins = 15;
        } else if (getLevel() == 3) {
            numberOfCoins = 20;
        }

        // Looping through the number of coins adding them to the array list only if they don't spawn on Pacman
        for (int i = 0; i < numberOfCoins; i++) {
            GoldCoin coin = new GoldCoin(this.context, getRandomWidth(), getRandomHeight());

            while(isInsidePacman(coin)){
                coin = new GoldCoin(this.context, getRandomWidth(), getRandomHeight());
            }

            coins.add(coin);
        }
    }

    // Creating enemy objects and adding them to the GoldCoin ArrayList - They get random positions
    public void instantiateEnemies() {

        int numberOfEnemies = 0;

        // Setting how many enemies should be in each level
        if(getLevel() == 1) {
            numberOfEnemies = 1;
        } else if (getLevel() == 2) {
            numberOfEnemies = 2;
        } else if (getLevel() == 3) {
            numberOfEnemies = 3;
        }

        // Looping through the number of enemies adding them to the array list only if they don't spawn on Pacman
        for(int i = 0; i < numberOfEnemies; i++) {
            Enemy enemy = new Enemy(this.context, getRandomWidth(), getRandomHeight());

            while(isInsidePacman(enemy)){
                enemy = new Enemy(this.context, getRandomWidth(), getRandomHeight());
            }

            enemies.add(enemy);
        }
    }

    // Changing the Pacman Bitmap by checking the pacman image state, then setting it
    public void changePacmanImage() {

        Bitmap pacmanOpen = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_open);
        Bitmap pacmanClose = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_close);

        if(getPacmanState().equals(PACMAN_OPEN)) {
            setPacBitmap(pacmanClose);
            setPacmanState(PACMAN_CLOSE);
        } else {
            setPacBitmap(pacmanOpen);
            setPacmanState(PACMAN_OPEN);
        }
    }

    // Share score
    public void shareScore() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "I just got " + getTotalPoints() + " points in Pacman! You can't beat me!");
        shareIntent.setType("text/plain");
        ((MainActivity)context).startActivity(Intent.createChooser(shareIntent, "Share your score!"));
    }



    // MAIN METHODS

    // NEW GAME
    public void newGame()
    {
        // Enable gamebuttons to true if false
        ((MainActivity)context).enableButtons();

        // Making sure that the PacmanTimer is not running, and then starting
        cancelPacmanTimer();
        startPacmanTimer();

        // Setting the level and the level textView
        level = getLevel();
        levelText.setText("Level " + " " + getLevel());

        // Resetting the points and seconds depending on which level user is on and adding it to text views
        points = 0;
        if(getLevel() == 1) { seconds = 25;
        } else if (getLevel() == 2){ seconds = 30;
        } else if (getLevel() == 3) { seconds = 40; }

        pointsView.setText(context.getResources().getString(R.string.points) + " " + points);
        timerView.setText(context.getResources().getString(R.string.time) + " " + seconds + " seconds");

        // Clearing the coin and enemy ArrayList for objects if any
        coins.clear();
        enemies.clear();

        // The start coordinates of Pacman
        pacx = 50;
        pacy = 400;

        // Checking whether the gameView has been drawn, then instantiate them.
        if(gameView.coinsInstantiated) { instantiateCoins(); }
        if(gameView.enemiesInstantiated) { instantiateEnemies(); }

        // Setting the start State of Pacman Image by calling changePacmanImage method and make sure the state is set to the opposite
        setPacmanState(Game.PACMAN_CLOSE);
        changePacmanImage();

        // Setting the start direction of Pacman to right
        setDirection(PACBITMAP_RIGHT);

        // Starting the gameTimer
        cancelGameTimer();
        startGameTimer();

        // Redrawing the screen
        gameView.invalidate();
    }



    // THE GAME TIMER
    public void cancelGameTimer(){
        if(gameTimer != null){
            running = false;
            gameTimer.cancel();
            gameTimer = null;
        }
    }

    public void startGameTimer() {

        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {

            @Override
            public void run() {

                // If the boolean running is true the timer should start such as the PacmanTimer also is running
                if (running) {

                    ((MainActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            seconds--;
                            timerView.setText(context.getResources().getString(R.string.time) + " " + seconds + " seconds");

                            if (seconds == 0) {
                                // Stop timers
                                cancelGameTimer();
                                cancelPacmanTimer();

                                // Add points to total
                                totalPoints += points;

                                // Open dialog
                                lostDialog();
                            }
                        }
                    });
                }
            } // end of run
        }, 0,1000);
    } // end of StartGameTimer



    // MOVING THE PACMAN AND ENEMY PART 1 - The PacmanTimer

    // Method for checking whether PacmanTimer is running and if it's running then cancel it
    public void cancelPacmanTimer(){
        if(pacmanTimer != null){
            running = false;
            pacmanTimer.cancel();
            pacmanTimer = null;
        }
    }

    // Creating the PacmanTimer (that also moves enemies at the moment)
    public void startPacmanTimer() {
        pacmanTimer = new Timer();
        pacmanTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(running) {
                    PacmanTimerMethod();
                }
            }
        }, 0, 1*100);
    }

    // Make sure the PacmanTimer TimerMethod runs on UI Thread
    private void PacmanTimerMethod() {
        ((MainActivity)context).runOnUiThread(Timer_Pacman);
    }

    // In the method MovePacman() is called setting the direction and the pixels + making sure the Pacman open/close bitmaps wait a bit
    // before changing
    // MoveEnemy() is also called
    public Runnable Timer_Pacman = new Runnable() {
        @Override
        public void run() {
            movePacman(direction, pixels);
            moveEnemy();

            if(bitmapWait == 0) {
                setBitmapWait(3);
                changePacmanImage();
            } else {
                setBitmapWait(getBitmapWait() - 1);
            }
        }
    };

    // MOVING THE PACMAN AND ENEMY PART 2 - The Enemy

    // MOVING THE ENEMY
    public void moveEnemy() {
        for(int i = 0; i < getEnemies().size(); i++) {
            Enemy enemy = getEnemies().get(i);

            // Getting the different bitmaps for the enemy's eyes
            Bitmap enemyUp = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_up);
            Bitmap enemyLeft = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_left);
            Bitmap enemyRight = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_right);
            Bitmap enemyDown = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_down);

            Random randNum = new Random();

            // Checking if enemy has moved 5 steps, if it has it should switch direction, if it hasn't 1 step should be counted
            if (enemy.getMovedSteps() == 0) {
                enemy.setMovedSteps(5);

                // Getting a random number between 0 and 3
                int randomDirection = randNum.nextInt(4);

                // Depending on the number, choose the direction enemy should go in
                switch (randomDirection) {
                    case 0:
                        enemy.setEnemyDirection(Enemy.ENEMY_UP);
                        enemy.setEnemyBitmap(enemyUp);
                        break;
                    case 1:
                        enemy.setEnemyDirection(Enemy.ENEMY_LEFT);
                        enemy.setEnemyBitmap(enemyLeft);
                        break;
                    case 2:
                        enemy.setEnemyDirection(Enemy.ENEMY_RIGHT);
                        enemy.setEnemyBitmap(enemyRight);
                        break;
                    case 3:
                        enemy.setEnemyDirection(Enemy.ENEMY_DOWN);
                        enemy.setEnemyBitmap(enemyDown);
                        break;
                }
            } else {
                enemy.setMovedSteps(enemy.getMovedSteps() - 1);
            }

            // Call the direction (from the first switch-statement) and call the right movement method
            switch (enemy.getEnemyDirection()) {
                case Enemy.ENEMY_UP:
                    moveEnemyUp(enemy, pixels);
                    break;
                case Enemy.ENEMY_LEFT:
                    moveEnemyLeft(enemy, pixels);
                    break;
                case Enemy.ENEMY_RIGHT:
                    moveEnemyRight(enemy, pixels);
                    break;
                case Enemy.ENEMY_DOWN:
                    moveEnemyDown(enemy, pixels);
                    break;
            }
        }
    }

    // THE MOVEMENT METHODS OF ENEMY
    // UP
    public void moveEnemyUp(Enemy enemy, int pixels)
    {
        // If position y minus the pixels we want to move the enemy is bigger than 0
        // it should subtract the pixels from the current position
        // Else enemyposy should move to position 0 aka the top of the screen
        if (enemy.getEnemyposy() - pixels > 0) {
            enemy.setEnemyposy(enemy.getEnemyposy() - pixels);
        } else {
            enemy.setEnemyposy(0);
        }
    }

    // LEFT
    public void moveEnemyLeft(Enemy enemy, int pixels)
    {
        // If position y minus the pixels we want to move the enemy is bigger than 0
        // it should subtract the pixels from the current position
        // Else enemyposy should move to position 0 aka the top of the screen
        if (enemy.getEnemyposx() - pixels > 0) {
            enemy.setEnemyposx(enemy.getEnemyposx() - pixels);
        } else {
            enemy.setEnemyposx(0);
        }
    }

    // RIGHT
    public void moveEnemyRight(Enemy enemy, int pixels)
    {
        // If position y minus the pixels we want to move the enemy is bigger than 0
        // it should subtract the pixels from the current position
        // Else enemyposy should move to position 0 aka the top of the screen
        if (enemy.getEnemyposx() + pixels + enemy.getEnemyBitmap().getWidth() < screenWidth) {
            enemy.setEnemyposx(enemy.getEnemyposx() + pixels);
        } else {
            enemy.setEnemyposx(screenWidth - enemy.getEnemyBitmap().getWidth());
        }
    }

    // DOWN
    public void moveEnemyDown(Enemy enemy, int pixels)
    {
        // If position y minus the pixels we want to move the enemy is bigger than 0
        // it should subtract the pixels from the current position
        // Else enemyposy should move to position 0 aka the top of the screen
        if (enemy.getEnemyposy() + pixels + enemy.getEnemyBitmap().getHeight() < screenHeight) {
            enemy.setEnemyposy(enemy.getEnemyposy() + pixels);
        } else {
            enemy.setEnemyposy(screenHeight - enemy.getEnemyBitmap().getHeight());
        }
    }

    // MOVING THE PACMAN AND ENEMY PART 2 - Pacman

    // Get the direction and set the movement method
    public void movePacman(String direction, int pixels){
        switch(direction) {
            case Game.PACBITMAP_UP:
                movePacmanUp(pixels);
                break;
            case Game.PACBITMAP_LEFT:
                movePacmanLeft(pixels);
                break;
            case Game.PACBITMAP_RIGHT:
                movePacmanRight(pixels);
                break;
            case Game.PACBITMAP_DOWN:
                movePacmanDown(pixels);
                break;
            default:
                break;
        }
    }

    // THE MOVEMENT METHODS OF PACMAN
    // UP
    public void movePacmanUp(int pixels)
    {
        // If position y minus the pixels we want to move the pacman is bigger than 0
        // it should subtract the pixels from the current position
        // Else pacy should move to position 0 aka the top of the screen
        if (pacy - pixels > 0) {
            pacy = pacy - pixels;
            doCollisionCheck();
            gameView.invalidate();
        } else {
            pacy = 0;
            doCollisionCheck();
            gameView.invalidate();
        }
    }

    // LEFT
    public void movePacmanLeft(int pixels)
    {
        // If position x minus the pixels we want to move the pacman is bigger than 0
        // it should subtract the pixels from the current position
        // Else pacx should move to position 0 aka the very left of the screen
        if (pacx - pixels > 0) {
            pacx = pacx - pixels;
            doCollisionCheck();
            gameView.invalidate();
        } else {
            pacx = 0;
            doCollisionCheck();
            gameView.invalidate();
        }
    }

    // RIGHT
    public void movePacmanRight(int pixels)
    {
        // If position pacx minus the pixels we want to move the pacman minus the width of Pacman
        // is not bigger than the width of the screen it should add the pixels to the current position
        // Else pacx should move to the position of the screenwidth minus the width of the pacman
        // aka the very right of the screen
        if (pacx + pixels + pacBitmap.getWidth() < screenWidth) {
            pacx = pacx + pixels;
            doCollisionCheck();
            gameView.invalidate();
        } else {
            pacx = screenWidth - pacBitmap.getWidth();
            doCollisionCheck();
            gameView.invalidate();
        }
    }

    // DOWN
    public void movePacmanDown(int pixels)
    {
        // If position pacy minus the pixels we want to move the pacman minus the height of Pacman
        // is not bigger than the height of the screen it should add the pixels to the current position
        // Else pacy should move to the position of the screenheight minus the height of the pacman
        // aka the bottom of the screen
        if (pacy + pixels + pacBitmap.getHeight() < screenHeight) {
            pacy = pacy + pixels;
            doCollisionCheck();
            gameView.invalidate();
        } else {
            pacy = screenHeight - pacBitmap.getHeight();
            doCollisionCheck();
            gameView.invalidate();
        }
    }



    // CHECK COLLISION
    // Helper method to find distance between Pacman and coins
    public int getDistance(GoldCoin coin) {

        // Finding the center spot of Pacman
        int halfWidth = getPacBitmap().getWidth() / 2;
        int halfHeight = getPacBitmap().getHeight() / 2;

        // Calculating the distance to the coins from the center of pacman
        int distanceX = (int) Math.pow((getPacx() + halfWidth) - coin.getCoinx(), 2);
        int distanceY = (int) Math.pow((getPacy() + halfHeight) - coin.getCoiny(), 2);

        int distance = (int) Math.sqrt((distanceX + distanceY));

        return distance;
    }

    // Helper method to find distance between Pacman and enemy
    public int getDistanceEnemy(Enemy enemy) {
        int distanceX = (int) Math.pow(getPacx() - enemy.getEnemyposx(), 2);
        int distanceY = (int) Math.pow(getPacy() - enemy.getEnemyposy(), 2);

        // Calculating the distance to the enemy
        int distance = (int) Math.sqrt((distanceX + distanceY));

        return distance;
    }

    // The method that checks the collision, removes coins and add points
    public void doCollisionCheck() {

        // Looping through the enemy Array to get the index of the Enemy
        for(int i = 0; i < getEnemies().size(); i++) {
            Enemy enemy = getEnemies().get(i);

            // Making sure that Pacman dies if Enemy and Pacman's Bitmap sides touches
            if(getDistanceEnemy(enemy) < 114) {

                // Stop timers
                cancelPacmanTimer();
                cancelGameTimer();

                // Add points to total points
                totalPoints += points;

                // Open dialog
                lostDialog();
            }
        }

        // Looping through the coins Array to get the index of the coin
        for (int i = 0; i < getCoins().size(); i++) {
            GoldCoin coin = getCoins().get(i);

            // If coin is not taken then the distance to the coin should be calculated
            if(coin.getTaken() == false) {

                // By getting the radius of Pacman image, we are able to collect the coins when the coins are a coins width
                // into the Pacman image
                if (getDistance(coin) < (getPacBitmap().getWidth() / 2) ) {

                    // The coin boolean taken is set to true, we add a point and set the pointView text again
                    coin.setTaken(true);
                    points++;
                    pointsView.setText(context.getResources().getString(R.string.points) + " " + points);
                }
            }
        }

        // If all coins has been (aka if the getCoins Array size equals to the amount of points that has been obtained)
        // then the game has been won and an alert is showing
        if(getCoins().size() == points) {

            // Stop timers
            cancelPacmanTimer();
            cancelGameTimer();

            // Add points to totalpoints
            totalPoints += points;

            // You have won alert
            wonDialog();
        }
    }



    // ALERTDIALOGS
    // WON DIALOG
    public void wonDialog() {
        // ALERT that tells the user that the game has been won, shows the points and ask for a new game or close
        final AlertDialog.Builder gameWonAlertBuilder = new AlertDialog.Builder(context);
        gameWonAlertBuilder.setCancelable(false); // Should not be able to dismiss alert by clicking outsite the box

        if(getLevel() == 3) {
            gameWonAlertBuilder.setMessage("Yay! You won the game! Total points: " + getTotalPoints() + "");
            gameWonAlertBuilder.setPositiveButton("New game", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    setTotalPoints(0);
                    setLevel(1);
                    newGame();
                }
            });
        } else {
            gameWonAlertBuilder.setMessage("Yay! You won level " + getLevel() + " with " + seconds + " seconds left and you're up at a total of " + getTotalPoints() + " points!");
            gameWonAlertBuilder.setPositiveButton("Next Level", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    setLevel(getLevel() + 1);
                    newGame();
                }
            });
        }

        gameWonAlertBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                ((MainActivity)context).disableButtons();
                dialog.dismiss();
            }
        });

        if(getLevel() == 3) {
            gameWonAlertBuilder.setNeutralButton("Share", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((MainActivity)context).disableButtons();
                    shareScore();
                }
            });
        }

        AlertDialog gameWonAlert = gameWonAlertBuilder.create();
        gameWonAlert.show();
    }

    // LOST DIALOG
    public void lostDialog() {
        // ALERT that tells the user that the game has been won, shows the points and ask for a new game or close
        final AlertDialog.Builder gameLostAlertBuilder = new AlertDialog.Builder(context);
        gameLostAlertBuilder.setCancelable(false); // Should not be able to dismiss alert by clicking outsite the box
        gameLostAlertBuilder.setMessage("Game over! Points: " + getTotalPoints());
        gameLostAlertBuilder.setPositiveButton("New game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                setLevel(1);
                setTotalPoints(0);
                newGame();
            }
        });

        gameLostAlertBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                ((MainActivity)context).disableButtons();
                dialog.dismiss();
            }
        });

        gameLostAlertBuilder.setNeutralButton("Share", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((MainActivity)context).disableButtons();
                shareScore();
            }
        });

        AlertDialog gameLostAlert = gameLostAlertBuilder.create();
        gameLostAlert.show();
    }

} // End of class