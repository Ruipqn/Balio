package com.BreakingDev.MindofBeyond;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;

import android.text.Layout;
import android.util.Log;

import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import GameFunctions.Level;
import GameFunctions.PositionMethods;
import GameFunctions.RotationMethods;

public class SinglePlayer extends AppCompatActivity {

    private Level level = Level.getInstance();

    Button button;
    //screen size
    private int screenWidth;
    private int screenHeigh;

    //layout
    private LinearLayout app_layer;
    //image
    private ImageView ball;
    private ImageView background;

    //position
    private double ballX;
    private double ballY;


    private List<Double> velocity = null;
    private double vel_multiplier= 1;

    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private Timer actionTimer = new Timer();

    //functions
    private PositionMethods pm;
    private RotationMethods rm;

    //probabilities
    private double p_nothing;
    private double p_dir;
    private double p_tp;
    private double p_slow;
    private double p_speed;
    private int action_period;

    //lifes
    private int lives;
    private ImageView life1;
    private ImageView life2;
    private ImageView life3;

    //score
    private TextView score_text;

    //settings
    private Double[][] settings  = { //[[p_nothing, p_dir, p_tp]]
            { 0.7,0.3,0.0},//10
            { 0.6,0.4,0.0},//20
            { 0.55,0.45,0.0},//30
            { 0.52,0.48,0.0},
            { 0.5,0.5,0.0},
            { 0.4,0.6,0.0} };;//70

    //cicle of actions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_player);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        ball = (ImageView) findViewById(R.id.ball);
        app_layer = (LinearLayout) findViewById(R.id.Layout);
        background = (ImageView) findViewById(R.id.background);

        //get screen size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        screenWidth = size.x;
        screenHeigh = size.y;

        pm = new PositionMethods(screenWidth, screenHeigh, ball.getWidth(), ball.getHeight());
        rm = new RotationMethods();

        life1 = (ImageView) findViewById(R.id.life1);
        life2 = (ImageView) findViewById(R.id.life2);
        life3 = (ImageView) findViewById(R.id.life3);

        score_text = (TextView) findViewById(R.id.score);
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
    }

    public void gameStart(){
        //define Level 1
        level.setL(1);
        lives = 3;

        //Array [10,20,....,70][p_nothing, p_dir, p_tp]

        //Move out of screen
        //set on click image and background TODO

        ball.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //click on ball
                timer.cancel();
                if (lives<3){
                    lives+=1;
                }
                level.setL(level.getL()+1);
                runLevel(level.getL());
            }
        });
        app_layer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                timer.cancel();
                actionTimer.cancel();
                //click on screen
                if(lives >1){
                    lives -= 1;
                }
                else{
                    lives =3;
                    level.setL(0);
                }
                runLevel(level.getL());
            }
        });

        runLevel(level.getL());
        //loop while not lost, run game

        //if lost, restart
    }
    public void runLevel(int g_level){
        //ball.setImageResource(R.drawable.myball1);


        String ball_color = "#FF333232";
        ball.getBackground().setColorFilter(Color.parseColor(ball_color), PorterDuff.Mode.ADD);
        life1.getBackground().setColorFilter(Color.parseColor(ball_color), PorterDuff.Mode.ADD);
        life2.getBackground().setColorFilter(Color.parseColor(ball_color), PorterDuff.Mode.ADD);
        life3.getBackground().setColorFilter(Color.parseColor(ball_color), PorterDuff.Mode.ADD);
        ball.getBackground().setColorFilter(Color.parseColor(ball_color), PorterDuff.Mode.ADD);



        //ball.getBackground().setColorFilter(0xf0f0f0,android.graphics.PorterDuff.Mode.SRC_IN);
        //ball.setColorFilter(0x111111,android.graphics.PorterDuff.Mode.SRC_IN);
        timer = new Timer();
        //update lives
        if(lives ==3){
            life1.setVisibility(View.VISIBLE);
            life2.setVisibility(View.VISIBLE);
            life3.setVisibility(View.VISIBLE);
        }else if(lives ==2){
            life1.setVisibility(View.VISIBLE);
            life2.setVisibility(View.VISIBLE);
            life3.setVisibility(View.INVISIBLE);
        }else if(lives ==1){
            life1.setVisibility(View.VISIBLE);
            life2.setVisibility(View.INVISIBLE);
            life3.setVisibility(View.INVISIBLE);
        }else{
            life1.setVisibility(View.INVISIBLE);
            life2.setVisibility(View.INVISIBLE);
            life3.setVisibility(View.INVISIBLE);
        }
        //update score
        String score_string= "Score: " + g_level;
        score_text.setText(score_string);


        ballX = 100000f;
        ballY= 100000f;
        ball.setX((float)ballX);
        ball.setY((float)ballY);

        try
        {
            ball.setVisibility(View.INVISIBLE);
            Thread.sleep(250);
            ball.setVisibility(View.VISIBLE);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }

        Double[] settings_used;
        //velocity
        List<Double> pos = new ArrayList<>();
        pos.add((double)ball.getX());
        pos.add((double)ball.getY());
        velocity = pm.genDirectionVector(pos);

        //define what the level will be like p_nothing, p_dir, p_tp
        if (g_level/10> settings.length -1){
            settings_used = settings[settings.length-1];
        }
        else{
            settings_used = settings[g_level/10];
        }

        p_nothing = settings_used[0];
        p_dir = settings_used[1];
        p_tp = settings_used[2];
        p_slow = 0;
        p_speed = 0;

        if (g_level<10){

            //app_layer.setBackgroundColor(Color.rgb(0x27,0x6C,0xCC));
            rm.setBaseRotation(10);
            action_period  = 200;
            vel_multiplier= 0.2 + 0.05 * g_level;
        }
        else if (g_level<20){
            rm.setBaseRotation(12);
            action_period  = 40;
            vel_multiplier= 0.2 + 0.05 * 9  +0.1*(g_level-9);
            pm.setStarting_angle(30-g_level);
        }
        else if (g_level<30){
            rm.setBaseRotation(15);
            action_period  = 20;
            vel_multiplier= 0.2 + 0.05 * 9 +0.1*(20-9);
            pm.setStarting_angle(30-g_level);
        }
        else if (g_level<40){
            rm.setBaseRotation(20);
            action_period  = 10;
            vel_multiplier= 0.2 + 0.05 * 9 +0.1*(20-9) ;
            pm.setStarting_angle(0);
        }

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

        doCicleAction(10,action_period);

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
        /*
        p_nothing
        p_dir
        p_tp
        p_slow
        p_speed
        */

        double rnd = Math.random();
        if (rnd<p_nothing){
        }
        else if(rnd-p_nothing<p_dir){
            velocity = rm.changeRotation(velocity);
        }
        else if(rnd-p_nothing-p_dir<p_tp){
            List<Double> pos = pm.genStart();
            ballX = pos.get(0);
            ballY = pos.get(1);
            //create random velocity vector
            velocity = pm.genDirectionVector(pos);
        }
        else if(rnd-p_nothing-p_dir-p_tp<p_slow){

        }
        else if(rnd-p_nothing-p_dir-p_tp-p_slow<p_speed){
        }
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
