package com.BreakingDev.MindofBeyond;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button buttonMultiplayer;
    private Button singlePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        buttonMultiplayer = (Button)findViewById(R.id.button_MP);
        buttonMultiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMultiplayerActivity();
            }
        });

        singlePlayer = (Button) findViewById(R.id.button_SP);
        singlePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSinglePlayerActivity();
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

}
