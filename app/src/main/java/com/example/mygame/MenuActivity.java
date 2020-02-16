package com.example.mygame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.mygame.TicTacToe.TicTacToeActivity;
import com.example.mygame.TicTacToe.multi.MultiplayerActivity;

public class MenuActivity extends BaseActivity {

    AnimatedBackground animatedBackground;
    private NavController navController;


    private int NUMBER_OF_SQUARES;
    private int NUMBER_OF_PLAYERS;
    private int DIFFICULTY;


    private String APP_SETTINGS;
    private String NUMBER_OF_SQUARES_SETTINGS;
    private String NUMBER_OF_PLAYER_SETTINGS;
    private String DIFFICULTY_STATE;
    final private String NOS_SETTINGS_TO_FRAG = "fieldSize";
    final private String NOP_SETTINGS_TO_FRAG = "numPlayers";
    final private String DIF_SETTINGS_TO_FRAG = "diff";
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);

        APP_SETTINGS = getString(R.string.APP_SETTINGS);
        DIFFICULTY_STATE = getString(R.string.DIFFICULTY_STATE);
        NUMBER_OF_SQUARES_SETTINGS = getString(R.string.NUMBER_OF_SQUARES_SETTINGS);
        NUMBER_OF_PLAYER_SETTINGS = getString(R.string.NUMBER_OF_PLAYER_SETTINGS);
        sharedPreferences = getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);

        if(savedInstanceState != null){
            NUMBER_OF_SQUARES = savedInstanceState.getInt("numberOfSquares");
        }

        final ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.layout);
        animatedBackground = new AnimatedBackground();
        animatedBackground.animate(constraintLayout, this);
        navController = Navigation.findNavController(this, R.id.fragment);

    }




    public void saveSettingsFromFragment(Bundle bundle){
        NUMBER_OF_SQUARES = bundle.getInt(NOS_SETTINGS_TO_FRAG, 3);
        NUMBER_OF_PLAYERS = bundle.getInt(NOP_SETTINGS_TO_FRAG, 2);
        DIFFICULTY = bundle.getInt(DIF_SETTINGS_TO_FRAG, 1);
    }

    public Bundle getSettingsFromFragment(){
        Bundle bundle = new Bundle();
        bundle.putInt(NOS_SETTINGS_TO_FRAG, NUMBER_OF_SQUARES);
        bundle.putInt(NOP_SETTINGS_TO_FRAG, NUMBER_OF_PLAYERS);
        bundle.putInt(DIF_SETTINGS_TO_FRAG, DIFFICULTY);
        return bundle;
    }

    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(NUMBER_OF_SQUARES_SETTINGS, NUMBER_OF_SQUARES);

        editor.putInt(NUMBER_OF_PLAYER_SETTINGS, NUMBER_OF_PLAYERS);

        editor.putInt(DIFFICULTY_STATE, DIFFICULTY);

        editor.apply();

        //Toast.makeText(getApplicationContext(), "onSave", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume(){
        super.onResume();


        if(sharedPreferences.contains(NUMBER_OF_SQUARES_SETTINGS))
            NUMBER_OF_SQUARES = sharedPreferences.getInt(NUMBER_OF_SQUARES_SETTINGS, 0);
        else
            NUMBER_OF_SQUARES = 3;

        if(sharedPreferences.contains(NUMBER_OF_PLAYER_SETTINGS))
            NUMBER_OF_PLAYERS = sharedPreferences.getInt(NUMBER_OF_PLAYER_SETTINGS, 0);
        else
            NUMBER_OF_PLAYERS = 2;

        if(sharedPreferences.contains(DIFFICULTY_STATE))
            DIFFICULTY = sharedPreferences.getInt(DIFFICULTY_STATE, 1);
        else
            DIFFICULTY = 0;



        //Toast.makeText(getApplicationContext(), "onRestore", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onGameButtonPush(View view){
        if(NUMBER_OF_PLAYERS == -1){
            startActivity(new Intent( MenuActivity.this , MultiplayerActivity.class));
            animatedBackground.stop();
            MenuActivity.this.finish();
        } else {
            startActivity(new Intent(MenuActivity.this, TicTacToeActivity.class));
            animatedBackground.stop();
            MenuActivity.this.finish();
        }
    }

    public void onExitButtonPush(View view){
        MenuActivity.this.finish();
    }

    public void onAboutButtonPush(View view){
        startActivity(new Intent(MenuActivity.this, AboutActivity.class));
        animatedBackground.stop();
    }

}
