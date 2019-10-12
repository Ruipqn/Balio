package com.BreakingDev.MindofBeyond;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import GameFunctions.Level;
import GameFunctions.PositionMethods;
import GameFunctions.RotationMethods;

public class SinglePlayer extends AppCompatActivity {

    private PositionMethods pm;
    private RotationMethods rm;
    private Level level = Level.getInstance();

    Button button;
    //screen size
    private int screenWidth;
    private int screenHeigh;

    //image
    private ImageView ball;

    //position
    private float ballX;
    private float ballY;

    //velocity
    private Float[] velocity = null;
    private float vel_multiplier= 1;

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_player);

        pm = new PositionMethods();
        rm = new RotationMethods();

        ball = (ImageView) findViewById(R.id.ball);

        //get screen size

        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        screenWidth = size.x;
        screenHeigh = size.y;

        //Move out of screen
        ball.setX(-80.0f);
        ball.setY(-80.0f);

        //Start the timer

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        gameRun();
                    }
                });
            }
        }, 0, 1);

        button=(Button)findViewById(R.id.moveball);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                level.setL(level.getL()+1);
                reload();
            }
        });

    }

    public void gameRun(){
        // if passed wall, set beginning position
        vel_multiplier = 2;
        if(velocity ==null){
            velocity = genVelocity();
        }

        ballX += velocity[0] * vel_multiplier;
        ballY += velocity[1] * vel_multiplier;


        if (ballY + ball.getHeight() < 0 | ballY > screenHeigh |
                ballX + ball.getWidth() < 0 | ballX > screenWidth) {
            //create new starting point
            Float[] new_start = genStart();
            ballX = new_start[0];
            ballY = new_start[1];
            //create random velocity vector
            velocity = genVelocity();
        }

        ball.setX(ballX);
        ball.setY(ballY);
    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    public Float[] genVelocity() {
        //we should add a rule so the velocity doesn't go inside the wall right away
        float rnd1 = (float)Math.random();
        float rnd2 = (float)Math.random();
        float vx;
        float vy;
        if (rnd1<=0.5){
            vx = (float)Math.random();
        }
        else{
            vx = -(float)Math.random();
        }
        if (rnd2<=0.5){
            vy = (float)Math.random();
        }
        else{
            vy = -(float)Math.random();
        }
        Float[] velocity= new Float[2];
        velocity[0] = vx;
        velocity[1] = vy;
        return velocity;
    }

    public Float[] genStart(){
        double rnd = Math.random();
        float x;
        float y;
        if(rnd<=0.25){
            //generate on top wall
            y = 0;
            x = (float)Math.floor(Math.random()*(screenWidth - ball.getWidth()));
        }
        else if (0.25<rnd && rnd<=0.5){
            //generate on right wall
            y = (float)Math.floor(Math.random()*(screenHeigh - ball.getHeight()));
            x = screenWidth - ball.getWidth();

        }
        else if (0.5<rnd && rnd<=0.75){
            //generate on bottom wall
            y = screenHeigh - ball.getHeight();
            x = (float)Math.floor(Math.random()*(screenWidth - ball.getWidth()));
            //car.setY((screenHeigh - car.getHeight()));
            //car.setX((float)Math.floor(Math.random()*(screenWidth - car.getWidth())));
        }
        else{
            //generate on left wall
            y = (float)Math.floor(Math.random()*(screenHeigh - ball.getHeight()));
            x = 0 ;
        }
        Float[] array = new Float[2];
        array[0] = x;
        array[1] = y;
        return array;
    }
}
