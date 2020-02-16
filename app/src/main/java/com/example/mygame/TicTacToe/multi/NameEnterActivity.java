package com.example.mygame.TicTacToe.multi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.mygame.R;

public class NameEnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_enter);
    }


    public void createButtonPush(View view){
        Intent intent = new Intent();
        EditText textView = (EditText) findViewById(R.id.name_field);
        String firstPlayerName = textView.getText().toString();
        intent.putExtra("firstPlayerName", firstPlayerName );
        setResult(RESULT_OK, intent);
        finish();
    }
}
