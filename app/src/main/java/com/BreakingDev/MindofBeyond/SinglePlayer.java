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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    private double ballX;
    private double ballY;

    //velocity
    private List<Double> velocity = null;
    private double vel_multiplier= 1;

    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private Timer actionTimer = new Timer();

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

        pm = new PositionMethods(screenWidth, screenHeigh, ball.getWidth(), ball.getHeight());
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
        vel_multiplier = 0.004 * g_level;
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

    }
    public void doOneAction(int delay){
        actionTimer.cancel();
        actionTimer = new Timer();
        actionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        action();
                    }
                });
            }
        }, delay);
    }
    public void doCicleAction(int delay,int period) {

        actionTimer.cancel();
        actionTimer = new Timer();
        actionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        action();
                    }
                });
            }
        }, delay, period);
    }

    public void action(){
        //mudar propriedades da bola
        List<Double> pos = new ArrayList<>();
        pos.add((double)ball.getX());
        pos.add((double)ball.getY());
        velocity = pm.genDirectionVector(pos);

    }

    public void moveBall(double prob_change_direction){

        // if passed wall, set beginning position
        if(velocity ==null){
            List<Double> pos = new ArrayList<>();
            pos.add((double)ball.getX());
            pos.add((double)ball.getY());
            velocity = pm.genDirectionVector(pos);
        }

        if (ballY + ball.getHeight() < 0 | ballY > screenHeigh |
                ballX + ball.getWidth() < 0 | ballX > screenWidth) {
            //create new starting point
            doOneAction(1000);
            List<Double> pos = pm.genStart();
            ballX = pos.get(0);
            ballY = pos.get(1);
            //create random velocity vector
            velocity = pm.genDirectionVector(pos);
        }
        ballX += velocity.get(0) * vel_multiplier;
        ballY += velocity.get(1) * vel_multiplier;


        ball.setX((float)ballX);
        ball.setY((float)ballY);

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


}
