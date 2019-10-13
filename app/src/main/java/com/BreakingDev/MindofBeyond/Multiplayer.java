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
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import GameFunctions.Level;
import GameFunctions.Player;
import GameFunctions.PositionMethods;
import GameFunctions.RotationMethods;

public class Multiplayer extends AppCompatActivity {

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

    private Player current_player;
    private List<Player> all_players;

    //probabilities
    private double p_nothing;
    private double p_dir;
    private double p_gen_new_dir;
    private double p_tp;
    private double p_slow;
    private double p_speed;
    private int action_period;

    //settings
    private Double[][] settings  = { //[[p_nothing, p_dir, p_gen_new_dir, p_tp]]
            { 0.8,0.2,0.0,0.0},//10
            { 0.69,0.3,0.01,0.0},//20
            { 0.63,0.35,0.02,0.0},//30
            { 0.595,0.38,0.025,0.0},//40
            { 0.57,0.40,0.03,0.0},//50
            { 0.56,0.41,0.03,0.0} };;//70

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

        all_players = getIntent().getParcelableExtra("all_players");

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
        level.setL(0);

        int player_select = (int) Math.round(Math.random() * all_players.size());
        current_player = all_players.get(player_select);

        //Array [10,20,....,70][p_nothing, p_dir, p_tp]

        //Move out of screen
        //set on click image and background TODO

        ball.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //click on ball
                timer.cancel();
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

                for (Player x : all_players) {
                    if (!x.equals(current_player)) {
                        x.setPoints();
                    }
                }

                // pop_up restart button and go to menu button plus scores
            }
        });

        runLevel(level.getL());
        //loop while not lost, run game

        //if lost, restart
    }
    public void runLevel(int g_level){

        timer = new Timer();

        List<Double> pos = pm.genStart();

        ballX = pos.get(0);
        ballY = pos.get(1);

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


        Player selected_player = current_player;

        while (selected_player.equals(current_player)) {
            int player_select = (int) Math.round(Math.random() * all_players.size());
            current_player = all_players.get(player_select);
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
        p_gen_new_dir = settings_used[2];
        p_tp = settings_used[3];
        p_slow = 0;
        p_speed = 0;

        if (g_level<10){
            //app_layer.setBackgroundColor(Color.rgb(0x27,0x6C,0xCC));
            rm.setBaseRotation(10);
            action_period  = 60;
            vel_multiplier= 0.2 + 0.05 * g_level;
        }
        else if (g_level<20){
            rm.setBaseRotation(12);
            action_period  = 40;
            vel_multiplier= 0.2 + 0.05 * 9  +0.1*(g_level-9);
            pm.setStarting_angle(g_level+15);
        }
        else if (g_level<30){
            rm.setBaseRotation(15);
            action_period  = 25;
            vel_multiplier= 0.2 + 0.05 * 9 +0.1*(20-9);
            pm.setStarting_angle(g_level+15);
        }
        else if (g_level<40){
            rm.setBaseRotation(20);
            action_period  = 15;
            vel_multiplier= 0.2 + 0.05 * 9 +0.1*(23-9) ;
            pm.setStarting_angle(45);
        }

        else if (g_level<50){
            rm.setBaseRotation(20);
            action_period  = 10;
            vel_multiplier= 0.2 + 0.05 * 9 +0.1*(25-9) ;
            pm.setStarting_angle(45);
        }

        else {
            rm.setBaseRotation(20);
            action_period  = 5;
            vel_multiplier= 0.2 + 0.05 * 9 +0.1*(30-9) + 0.007*(g_level-50) ;
            pm.setStarting_angle(45);
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
        else if(rnd-p_nothing-p_dir<p_gen_new_dir){
            List<Double> pos = new ArrayList<>();
            pos.add((double)ball.getX());
            pos.add((double)ball.getY());
            velocity = pm.genDirectionVector(pos);
        }
        else if(rnd-p_nothing-p_dir-p_gen_new_dir<p_tp){
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

            if (Math.random() > 0.8) {
                int player_select = (int) Math.round(Math.random() * all_players.size());
                current_player = all_players.get(player_select);
            }

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
