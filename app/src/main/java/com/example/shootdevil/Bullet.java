package com.example.shootdevil;

public class Bullet {

    int bulletX , bulletY , bulletvelocity ;
    public Bullet() {
        resetBullet();
    }

    public void resetBullet() {

        bulletX  = GameView.sw/2;
        bulletY = GameView.sh - GameView.tank.getHeight();
        bulletvelocity = 40;
    }


}
