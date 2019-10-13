package com.BreakingDev.MindofBeyond;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

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
    ArrayList<Player> all_players;
    int addOk=1;
    int count = 1;
    private String color;
    private int numColors = 10;
    ArrayList<String>colors_aux = new ArrayList<>();
    private int posToRem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_multiplayer);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        colors_aux.add("#FFA500");
        colors_aux.add("#FF4500");
        colors_aux.add("#276ccc");
        colors_aux.add("#FFD555");
        colors_aux.add("#d6ff00");
        colors_aux.add("#c303b1");
        colors_aux.add("#A7A7A7");
        colors_aux.add("#fca4da");
        colors_aux.add("#b77231");
        colors_aux.add("#808000");

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

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(color != null && editText.getText().toString().length()>0)
                    btAdd.setVisibility(View.VISIBLE); //To set visible
                else
                    btAdd.setVisibility(View.INVISIBLE); //To set visible

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        btAdd=(Button)findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count <= 4 && addOk == 1) {

                    String counter = String.valueOf(count);
                    count += 1;
                    String newItem = editText.getText().toString();
                    if (newItem.length()>0){
                        String result = counter + ". " + newItem;

                        Player player = new Player(newItem, getColor());
                        all_players.add(player);

                        // add result to arraylist
                        itemList.add(result);

                        editText.getText().clear();
                        addOk=0;
                        if(count ==3)start_game.setVisibility(View.VISIBLE); //To set visible
                        removeColor(posToRem);
                        color = null;
                        btAdd.setVisibility(View.INVISIBLE); //To set visible

                    }

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
                if (listV.getAdapter().getCount() > 1) {
                    openMultiplayer();
                }
            }
        });

    }

    public void openMultiplayer() {
        Intent intent = new Intent(this, Multiplayer.class);
        Integer counter = 0;
        for (Player x : all_players) {
            counter += 1;
            intent.putExtra(counter.toString() + "_name", x.getName());
            intent.putExtra(counter.toString() + "_color", x.getColor());
        }
        intent.putExtra("counter", counter.toString());
        startActivity(intent);
    }

    public void opencolorpicker(){
        final ColorPicker colorPicker = new ColorPicker(this);
        ArrayList<String>colors = new ArrayList<>();

        for(int i = 0; i<this.numColors;i++){
            colors.add(colors_aux.get(i));
        }

        colorPicker.setColors(colors)
                .setColumns(5)
                .setRoundColorButton(true)
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        addOk = 1;
                        posToRem = position;
                        setColor(color);
                        if(editText.getText().toString().length()>0)
                            btAdd.setVisibility(View.VISIBLE); //To set visible

                    }

                    @Override
                    public void onCancel() {

                    }
                })
                .show();
    }

    public String getColor() {
        return color;
    }

    public void removeColor(int i) {
        colors_aux.remove(i);
        numColors -=1;
    }

    public void setColor(Integer color) {
        this.color = color.toString();
    }
}

