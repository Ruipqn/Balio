package com.BreakingDev.MindofBeyond;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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
    private ImageView car;

    //position
    private float carX;
    private float carY;

    //velocity
    private Float[] velocity = null;
    private float vel_multiplier= 1;

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pm = new PositionMethods();
        rm = new RotationMethods();

        car = (ImageView) findViewById(R.id.car);

        //get screen size

        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        screenWidth = size.x;
        screenHeigh = size.y;

        //Move out of screen
        car.setX(-80.0f);
        car.setY(-80.0f);

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

        carX += velocity[0] * vel_multiplier;
        carY += velocity[1] * vel_multiplier;


        if (carY + car.getHeight() <0 | carY + car.getHeight() > screenHeigh|
                carX+ car.getWidth() <0 |carX + car.getWidth() > screenWidth){
            //create new starting point
            Float[] new_start = genStart();
            carX = new_start[0];
            carY = new_start[1];
            //create random velocity vector
            velocity = genVelocity();
        }

        car.setX(carX);
        car.setY(carY);
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
            x = (float)Math.floor(Math.random()*(screenWidth - car.getWidth()));
        }
        else if (0.25<rnd && rnd<=0.5){
            //generate on right wall
            y = (float)Math.floor(Math.random()*(screenHeigh - car.getHeight()));
            x = screenWidth - car.getWidth();

        }
        else if (0.5<rnd && rnd<=0.75){
            //generate on bottom wall
            y = screenHeigh - car.getHeight();
            x = (float)Math.floor(Math.random()*(screenWidth - car.getWidth()));
            //car.setY((screenHeigh - car.getHeight()));
            //car.setX((float)Math.floor(Math.random()*(screenWidth - car.getWidth())));
        }
        else{
            //generate on left wall
            y = (float)Math.floor(Math.random()*(screenHeigh - car.getHeight()));
            x = 0 ;
        }
        Float[] array = new Float[2];
        array[0] = x;
        array[1] = y;
        return array;
    }
}
