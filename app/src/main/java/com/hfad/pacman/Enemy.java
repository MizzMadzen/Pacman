package com.hfad.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Enemy {

    // STATICS
    // Static strings to help set direction for enemy
    public static final String ENEMY_UP = "Up";
    public static final String ENEMY_LEFT = "Left";
    public static final String ENEMY_RIGHT = "Right";
    public static final String ENEMY_DOWN = "Down";

    // MEMBER FIELDS AND PROPERTIES
    // Reference to context
    private Context context;

    // Will contain the bitmap of Pacman
    private Bitmap enemyBitmap;
    public Bitmap getEnemyBitmap() { return enemyBitmap; }
    public void setEnemyBitmap(Bitmap enemyBitmap) { this.enemyBitmap = enemyBitmap; }

    // Stores the position of the position x and y of the enemy
    private int enemyposx; private int enemyposy;
    public int getEnemyposx() { return enemyposx; }
    public void setEnemyposx(int position) { this.enemyposx = position; }
    public int getEnemyposy() { return enemyposy; }
    public void setEnemyposy(int position) { this.enemyposy = position; }

    // Stores the number of steps an Enemy should take before switching direction
    private int movedSteps;
    public int getMovedSteps() { return movedSteps; }
    public void setMovedSteps(int steps) { this.movedSteps = steps; }

    // Will contain the direction of Pacman
    private String enemyDirection;
    public String getEnemyDirection() {
        return enemyDirection;
    }
    public void setEnemyDirection(String direction) { this.enemyDirection = direction; }

    // CONSTRUCTOR
    public Enemy(Context context, int enemyposx, int enemyposy) {
        this.context = context;
        this.enemyposx = enemyposx;
        this.enemyposy = enemyposy;
        enemyBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
    }

} // End of class