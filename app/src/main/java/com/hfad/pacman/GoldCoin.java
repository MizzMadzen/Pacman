package com.hfad.pacman;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.res.ResourcesCompat;

public class GoldCoin {

    // MEMBER FIELDS AND PROPERTIES
    // Context reference
    private Context context;

    // Paint object stores how to draw (e.g. color, style, line thickness)
    private Paint coinPaint = new Paint();

    // Stores the position of the x and y of the coin
    private int coinx; private int coiny;
    public int getCoinx() { return coinx; }
    public int getCoiny() { return coiny; }

    // Stores whether if a Coin has been taken by Pacman or not
    private boolean taken;
    public boolean getTaken() { return taken; }
    public void setTaken(boolean taken) { this.taken = taken; }

    // Stores the width of the coin (and the half of it to get the radius)
    private int coinWidth;
    public int getCoinHalfWidth() { return coinWidth / 2; }

    // CONSTRUCTOR
    public GoldCoin(Context context, int coinx, int coiny) {
        this.context = context;
        this.coinx = coinx;
        this.coiny = coiny;
        this.taken = false;
        setCoinColor();
    }

    // METHODS
    // Setting the color of the coin
    public void setCoinColor() {
        int coinColor = ResourcesCompat.getColor(context.getResources(), R.color.colorCoin, null);
        coinPaint.setColor(coinColor);
    }

    // MAIN METHODS
    // Drawing the coin
    public void drawCoin(Canvas canvas) {

        // We get the width of the coin and draw the circle on the canvas
        coinWidth = 50;
        canvas.drawCircle(coinx, coiny, getCoinHalfWidth(), coinPaint);
    }

} // End of class