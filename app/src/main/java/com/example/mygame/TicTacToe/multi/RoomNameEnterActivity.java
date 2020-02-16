package com.example.mygame.TicTacToe.multi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mygame.R;

public class RoomNameEnterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_name_enter);


    }

    public void createButtonPush(View view){
        Intent intent = new Intent();
        EditText textView = (EditText) findViewById(R.id.name_field);
        String roomName = textView.getText().toString();
        intent.putExtra("roomName", roomName );
        setResult(RESULT_OK, intent);
        finish();
    }


}
