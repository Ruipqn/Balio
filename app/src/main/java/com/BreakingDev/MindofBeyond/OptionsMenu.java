package com.BreakingDev.MindofBeyond;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton;

public class OptionsMenu extends AppCompatActivity {

    MediaPlayer mySong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_menu);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mySong = MediaPlayer.create(this,R.raw.musicmenu);
        Switch musicSW = (Switch) findViewById(R.id.musicSwitch);

        //Set a CheckedChange Listener for Switch Button
        musicSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on){
                if(on)
                {
                    //Do something when Switch button is on/checked
                    mySong.start();
                }
                else
                {
                    //Do something when Switch is off/unchecked
                    mySong.pause();
                }
            }
        });

    }


}

