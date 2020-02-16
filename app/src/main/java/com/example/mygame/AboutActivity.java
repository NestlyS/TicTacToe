package com.example.mygame;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mygame.TicTacToe.Game.BasicGame;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
    }


    public void onBackAboutButtonPush(View view){
        AboutActivity.this.finish();
    }
}
