package com.example.shootdevil;

import static android.media.CamcorderProfile.get;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GameView extends View  {




    int points = 0;
    static int sw, sh;
    int tX, tY;
    long UPDATE_MILLS = 30;
    Bitmap bg, balloon, balloonBad, cross;
    static Bitmap tank;
    Runnable runnable;
    Handler handler;
    Context context;
    ArrayList<Bullet> bullets = new ArrayList<>();
    ArrayList<BalloonBad> balloonsBad = new ArrayList<>();
    ArrayList<Balloon> balloons = new ArrayList<>();


    Paint bulletPaint, scorePaint;
    Rect dest;
    final int TEXT_SIZE = 80;
    boolean win = false;


    public GameView(Context context) {
        super(context);



        this.context = context;


        bg = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg);
        balloon = BitmapFactory.decodeResource(context.getResources(), R.drawable.p);
        balloonBad = BitmapFactory.decodeResource(context.getResources(), R.drawable.n);
        tank = BitmapFactory.decodeResource(context.getResources(), R.drawable.tank);
        cross = BitmapFactory.decodeResource(context.getResources(), R.drawable.cross);


        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        sw = point.x;
        sh = point.y;
        dest = new Rect(0, 0, sw, sh);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                invalidate();

            }
        };

        for (int i = 0; i < 2; i++) {
            Balloon balloon = new Balloon();
            balloons.add(balloon);
        }
        for (int i = 0; i < 2; i++) {
            BalloonBad devil = new BalloonBad();
            balloonsBad.add(devil);
        }

        bulletPaint = new Paint();
        bulletPaint.setColor(Color.BLACK);
        scorePaint = new Paint();
        scorePaint.setColor(Color.BLACK);

        scorePaint.setTextSize(TEXT_SIZE);
        scorePaint.setTextAlign(Paint.Align.LEFT);

        ((Activity) context).setContentView(R.layout.anim);

    }

    @Override
    protected void onDraw(Canvas canvas)

    {
        super.onDraw(canvas);
        canvas.drawBitmap(bg, null, dest, null);


        for (int i = 0; i < balloons.size(); i++) {
            balloons.get(i).bX += balloons.get(i).bSpeedX;
            balloons.get(i).bY += balloons.get(i).bSpeedY;

            if (balloons.get(i).bY > 250) {
                balloons.get(i).bSpeedY *= -1;
            }
            if (balloons.get(i).bY < 0) {
                balloons.get(i).bSpeedY *= -1;
            }
            if (balloons.get(i).bX < -balloon.getWidth() - 200 || balloons.get(i).bX > sw + 200) {
                balloons.get(i).resetBalloon();
            }


            balloonsBad.get(i).bX += balloonsBad.get(i).bSpeedX;
            balloonsBad.get(i).bY += balloonsBad.get(i).bSpeedY;

            if (balloonsBad.get(i).bY > 350) {
                balloonsBad.get(i).bSpeedY *= -1;
            }
            if (balloonsBad.get(i).bY < 0) {
                balloonsBad.get(i).bSpeedY *= -1;
            }
            if (balloonsBad.get(i).bX < -balloonBad.getWidth() - 200 || balloonsBad.get(i).bX > sw + 200) {
                balloonsBad.get(i).resetBalloon();
            }

        }

        for (int i = 0; i < balloons.size(); i++) {
            canvas.drawBitmap(balloon, balloons.get(i).bX, balloons.get(i).bY, null);
            canvas.drawBitmap(balloonBad, balloonsBad.get(i).bX, balloonsBad.get(i).bY, null);
        }
        for (int i = 0; i < bullets.size(); i++) {
            if (bullets.get(i).bulletY > -bullets.get(i).bulletY) {
                bullets.get(i).bulletY -= bullets.get(i).bulletvelocity;
                canvas.drawCircle(bullets.get(i).bulletX, bullets.get(i).bulletY, 10, bulletPaint);


            } else {
                bullets.remove(i);
            }
        }


        for (int i = 0; i < bullets.size(); i++) {

            for (int j = 0; j < balloons.size(); j++) {

                if (bullets.get(i).bulletX >= balloonsBad.get(j).bX
                        && bullets.get(i).bulletX <= balloonsBad.get(j).bX + balloonBad.getWidth()

                        && bullets.get(i).bulletY >= balloonsBad.get(j).bY

                        && bullets.get(i).bulletY <= balloonsBad.get(j).bY + balloonBad.getHeight()) {
                    balloonsBad.get(j).resetBalloon();
                    points++;

                    if (points > 5) {

                        Intent intent = new Intent(getContext(), GameOver.class);


                        win = true;
                        intent.putExtra("win", win);

                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }

                } else if (bullets.get(i).bulletX >= balloons.get(j).bX
                        && bullets.get(i).bulletX <= balloons.get(j).bX + balloon.getWidth()

                        && bullets.get(i).bulletY >= balloons.get(j).bY

                        && bullets.get(i).bulletY <= balloons.get(j).bY + balloon.getHeight()) {
                    balloons.get(j).resetBalloon();
                    points--;

                    if (points < -3) {

                        Intent intent = new Intent(getContext(), GameOver.class);


                        win = false;
                        intent.putExtra("win", win);

                        context.startActivity(intent);
                        ((Activity) context).finish();

                    }
                }

            }


        }


        canvas.drawText("Pt: " + points, 50, TEXT_SIZE + 30, scorePaint);
        canvas.drawBitmap(tank, sw / 2 - tank.getWidth() / 2, sh - tank.getHeight(), null);
        canvas.drawBitmap(cross, sw - cross.getWidth(), 0, null);


        handler.postDelayed(runnable, UPDATE_MILLS);




    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        tX = (int) event.getX();
        tY = (int) event.getY();
        int action = event.getAction();
        if (tX >= sw / 2 - tank.getWidth() / 2
                && tX <= sw / 2 + tank.getWidth() / 2
                && tY >= sh - tank.getHeight())
        {
            if (bullets.size() < 4) {
                Bullet bullet = new Bullet();
                bullets.add(bullet);
            }
        }


        if (tX >= sw - cross.getWidth() && tY <= cross.getHeight())

        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ((Activity) context).finish();

                        }
                    })

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }


        return super.onTouchEvent(event);
    }





        }






