package com.BreakingDev.MindofBeyond;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import GameFunctions.Player;
import petrov.kristiyan.colorpicker.ColorPicker;

public class Menu_multiplayer extends AppCompatActivity {

    ArrayAdapter adapter;
    EditText editText;
    ArrayList<String> itemList;
    EditText input;
    Button btAdd;
    Button cor;
    Button start_game;
    List<Player> all_players;
    int addOk=1;
    int count = 1;
    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_multiplayer);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String[] items={};

        all_players = new ArrayList<>(4);

        itemList=new ArrayList<String>(Arrays.asList(items));
        adapter=new ArrayAdapter<>(this,R.layout.list_user,R.id.txtview,itemList);
        final ListView listV=(ListView)findViewById(R.id.listUsers);
        listV.setAdapter(adapter);
        listV.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent , View view , int position , long id) {
                view.setBackgroundColor(getColor(R.color. colorPrimaryDark )) ;
            }
        }) ;
        editText=(EditText)findViewById(R.id.newPlayerInput);
        btAdd=(Button)findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count <= 4 && addOk == 1) {

                    String counter = String.valueOf(count);
                    count += 1;
                    String newItem = editText.getText().toString();
                    String result = counter + ". " + newItem;

                    Player player = new Player(newItem, getColor());
                    all_players.add(player);

                    // add result to arraylist
                    itemList.add(result);

                    editText.getText().clear();
                    addOk=0;
                    btAdd.setVisibility(View.INVISIBLE); //To set visible

                }
                // notify listview of data changed
                adapter.notifyDataSetChanged();
            }

        });




        cor = (Button)findViewById(R.id.Buttoncor);
        cor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                opencolorpicker();
            }
        });

        start_game = findViewById(R.id.buttonStartMP);
        start_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listV.getAdapter().getCount() != 0) {
                    openMultiplayer();
                }
            }
        });

    }

    public void openMultiplayer() {
        Intent intent = new Intent(this, Multiplayer.class);
        intent.putExtra("all_players", (Parcelable) all_players);
        startActivity(intent);
    }

    public void opencolorpicker(){
        final ColorPicker colorPicker = new ColorPicker(this);
        ArrayList<String>colors = new ArrayList<>();
        colors.add("#F94444");
        colors.add("#FF4500");
        colors.add("#276ccc");
        colors.add("#FFD555");
        colors.add("#d6ff00");
        colors.add("#c303b1");
        colors.add("#A7A7A7");
        colors.add("#fca4da");
        colors.add("#b77231");
        colors.add("#808000");
        colorPicker.setColors(colors)
                .setColumns(5)
                .setRoundColorButton(true)
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        addOk = 1;
                        setColor(color);
                        btAdd.setVisibility(View.VISIBLE); //To set visible

                    }

                    @Override
                    public void onCancel() {

                    }
                })
                .show();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}

