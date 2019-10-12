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

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import GameFunctions.Level;
import GameFunctions.PositionMethods;
import GameFunctions.RotationMethods;

public class SinglePlayer extends AppCompatActivity {

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
    private List<Double> velocity = null;
    private double vel_multiplier= 1;

    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private Timer timerWeirdMov = new Timer();

    //functions
    private PositionMethods pm;
    private RotationMethods rm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_player);

        ball = (ImageView) findViewById(R.id.ball);

        //get screen size

        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        screenWidth = size.x;
        screenHeigh = size.y;

        //pm = new PositionMethods( screenWidth, screenHeigh, (double) ball.getX(), (double) ball.getY());
        rm = new RotationMethods();

        //Start the game
        gameStart();

        /*
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
         */
        button=(Button)findViewById(R.id.moveball);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                timer.cancel();
            }
        });
    }

    public void gameStart(){
        //define Level 1
        level.setL(1);

        //Move out of screen
        ball.setX(-80.0f);
        ball.setY(-80.0f);
        //set on click image and background TODO

        ball.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                timer.cancel();
            }
        });


        runLevel(level.getL());
        //loop while not lost, run game

        //if lost, restart
    }
    public void runLevel(int g_level){
        //define what the level will be like
        vel_multiplier= 3 * g_level;
        final double prob_change_direction = 0.4;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        moveBall(prob_change_direction);
                    }
                });
            }
        }, 0, 1);

        timerWeirdMov.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        weirdMovements(prob_change_direction);
                    }
                });
            }
        }, 50, 1000);

    }
    public void weirdMovements(double probabilities){
        //mudar propriedades da bola
        Float[] a = genVelocity();
        velocity = new ArrayList<Double>();
        velocity.add((double)a[0]);
        velocity.add((double)a[1]);

    }

    public void moveBall(double prob_change_direction){

        // if passed wall, set beginning position
        if(velocity ==null){
            //velocity = pm.genDirectionVector();
            Float[] a = genVelocity();
            velocity = new ArrayList<Double>();
            velocity.add((double)a[0]);
            velocity.add((double)a[1]);
        }

        if (ballY + ball.getHeight() < 0 | ballY > screenHeigh |
                ballX + ball.getWidth() < 0 | ballX > screenWidth) {
            //create new starting point
            List<Float> new_start = genStart();
            ballX = new_start.get(0);
            ballY = new_start.get(1);
            //create random velocity vector


            //velocity = pm.genDirectionVector();
            Float[] a = genVelocity();
            velocity = new ArrayList<Double>();
            velocity.add((double)a[0]);
            velocity.add((double)a[1]);
        }
        ballX += velocity.get(0) * vel_multiplier;
        ballY += velocity.get(1) * vel_multiplier;


        ball.setX(ballX);
        ball.setY(ballY);

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

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }


    public List<Float> genStart(){
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
        }
        else{
            //generate on left wall
            y = (float)Math.floor(Math.random()*(screenHeigh - ball.getHeight()));
            x = 0 ;
        }
        List<Float> array =  new ArrayList<Float>();
        array.add(x);
        array.add(y);
        return array;
    }
}
