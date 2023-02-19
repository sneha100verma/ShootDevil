package com.example.shootdevil;

public class BalloonBad extends Balloon{

    public BalloonBad() {
         resetBalloon();
    }

    @Override
    public void resetBalloon() {
        super.resetBalloon();

        if(Math.random() < 0.5) {
            bX = -200;
            bSpeedX = 6 + (int) (Math.random() * 15);
        }
        else
        {
            bX = GameView.sw+200;
            bSpeedX = -1*(6+(int)(Math.random()*15));
        }
        bY = (int)(Math.random() * 200);
        if(Math.random() < 0.5)
        {
            bSpeedY = 3+ (int)(Math.random()*6);
        }
        else
        {
            bSpeedY = -1 * (3+(int)(Math.random()*6));
        }
    }
}
