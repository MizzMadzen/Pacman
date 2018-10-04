package com.hfad.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GameView extends View {

    // MEMBER FIELDS AND PROPERTIES
    // Stores the game
    Game game;
    public void setGame(Game game)
    {
        this.game = game;
    }

    // Helps checking if coins and enemies arrays know the size of canvas
    // so if the objects are not instantiated but canvas is drawn they will be
    public boolean coinsInstantiated = false;
    public boolean enemiesInstantiated = false;

    // Stores the height and width of the view
    int h,w;

    // CONSTRUCTORS
    // The next 3 constructors are needed for the Android view system, when we have a custom view.
    public GameView(Context context) { super(context); }
    public GameView(Context context, AttributeSet attrs) {
        super(context,attrs);
    }
    public GameView(Context context, AttributeSet attrs, int defStyleAttr) { super(context,attrs,defStyleAttr); }

    // MAIN METHOD
    // Whenever we update the screen the onDraw is called and redraws the elements in it
    @Override
    protected void onDraw(Canvas canvas) {

        // The width and height of the canvas + setting the size on the view via setSize method from Game class
        w = canvas.getWidth();
        h = canvas.getHeight();
        game.setSize(h,w);

        // Making a new paint object and makes the color of the entire canvas black
        Paint paint = new Paint();
        canvas.drawColor(Color.BLACK);

        // To make sure we have actual values of width and height of gameView, when we add the coins to the View (so
        // they are placed within the gameView), we start with calling the instantiating method on coins and enemy here
        // so they are not added before the canvas is drawn - We set the boolean to true after this, so it won't be called
        // again
        if(coinsInstantiated == false) {
            game.instantiateCoins();
            coinsInstantiated = true;
        }

        if(enemiesInstantiated == false) {
            game.instantiateEnemies();
            enemiesInstantiated = true;
        }

        // Drawing the Pacman and making sure that the bitmap is set in the right direction of movement
        Matrix matrix = new Matrix();

        // Whenever a move button is clicked the pacman image will rotate or flip to set the right direction of the pacman
        switch(game.getDirection() != null ? game.getDirection() : Game.PACBITMAP_RIGHT) {
            case Game.PACBITMAP_UP:
                matrix.postRotate(270);
                break;
            case Game.PACBITMAP_LEFT:
                // If the Pacman has an eye the left button click should flip the image, so the pacman is not upsidedown
                matrix.postScale(-1, 1, game.getPacx(), game.getPacy());
                break;
            case Game.PACBITMAP_RIGHT:
                matrix.postRotate(0);
                break;
            case Game.PACBITMAP_DOWN:
                matrix.postRotate(90);
                break;
            default:
                matrix.postRotate(0);
                break;
        }

        // Setting the bitmap up so it registers the matrix, then drawing it on the canvas
        Bitmap rotatingBitmap = Bitmap.createBitmap(game.getPacBitmap(), 0, 0, game.getPacBitmap().getWidth(), game.getPacBitmap().getHeight(), matrix, true);
        canvas.drawBitmap(rotatingBitmap, game.getPacx(), game.getPacy(), paint);

        // Looping through the coins ArrayList and drawing the number of objects instantiated
        for (int i = 0; i < game.getCoins().size(); i++) {
            GoldCoin coin = game.getCoins().get(i);

            // If the specific coin's property taken equals false (aka is not taken by Pacman) then the coin should be drawn
            if (!coin.getTaken()) {
                coin.drawCoin(canvas);
            }
        }

        // Looping through the enemy ArrayList and drawing the number of enemy objects instantiated
        for (int i = 0; i < game.getEnemies().size(); i++) {
            Enemy enemy = game.getEnemies().get(i);
            canvas.drawBitmap(enemy.getEnemyBitmap(), enemy.getEnemyposx(), enemy.getEnemyposy(), paint);
        }

        super.onDraw(canvas);
    }

} // End of class