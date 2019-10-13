package com.BreakingDev.MindofBeyond;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button buttonMultiplayer;
    private Button singlePlayer;
    private Button ButtonAbout;
    private Button ButtonOptions;
    private MediaPlayer mPlayer;
    private ImageView audioOn;
    private ImageView audioOff;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mPlayer = MediaPlayer.create(this,R.raw.musicmenu);
        mPlayer.setLooping(true);
        mPlayer.start();

        buttonMultiplayer = (Button)findViewById(R.id.button_MP);
        buttonMultiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMultiplayerActivity();
            }
        });

        this.audioOn = (ImageView)findViewById(R.id.audioOn);
        this.audioOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioOn.setVisibility(View.INVISIBLE); //To set Invisible
                audioOff.setVisibility(View.VISIBLE); //To set visible


                mPlayer.pause();
            }
        });

        this.audioOff = (ImageView)findViewById(R.id.audioOff);
        this.audioOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioOff.setVisibility(View.INVISIBLE); //To set Invisible
                audioOn.setVisibility(View.VISIBLE); //To set visible
                mPlayer.setLooping(true);
                mPlayer.start();
            }
        });

        singlePlayer = (Button) findViewById(R.id.button_SP);
        singlePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSinglePlayerActivity();
            }
        });

        ButtonAbout = (Button) findViewById(R.id.button_ABT);
        ButtonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAboutActivity();
            }
        });

    }

    public void openMultiplayerActivity(){
        Intent intent = new Intent(this, Menu_multiplayer.class);
        startActivity(intent);
    }

    public void openSinglePlayerActivity() {
        Intent intent = new Intent(this, SinglePlayer.class);
        startActivity(intent);
    }

    public void openAboutActivity() {
        Intent intent = new Intent(this, AboutMenu.class);
        startActivity(intent);
    }


}
