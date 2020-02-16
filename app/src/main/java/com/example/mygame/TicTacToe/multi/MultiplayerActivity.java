package com.example.mygame.TicTacToe.multi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.mygame.AnimatedBackground;
import com.example.mygame.BaseActivity;
import com.example.mygame.MenuActivity;
import com.example.mygame.TicTacToe.Game.Game;
import com.example.mygame.TicTacToe.Game.Messangers.MessageReciever;
import com.example.mygame.TicTacToe.Game.Messangers.MessageSender;
import com.example.mygame.TicTacToe.TicTacToeActivity;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mygame.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MultiplayerActivity extends BaseActivity {

    private int NUMBER_OF_SQUARES;
    private String playerName = "Kazan";
    private String secondPlayerName = "Andrey";

    private MessageSender messageSender;
    private MessageReciever messageReciever;


    private String APP_SETTINGS;
    private String NUMBER_OF_SQUARES_SETTINGS;
    private final String PLAYER_NAME_SETTINGS = "playerName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);


        APP_SETTINGS = getString(R.string.APP_SETTINGS);
        NUMBER_OF_SQUARES_SETTINGS = getString(R.string.NUMBER_OF_SQUARES_SETTINGS);

        SharedPreferences sharedPreferences = getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(NUMBER_OF_SQUARES_SETTINGS)){
            NUMBER_OF_SQUARES = sharedPreferences.getInt(NUMBER_OF_SQUARES_SETTINGS, 0);
        }
        if(sharedPreferences.contains(PLAYER_NAME_SETTINGS)){
            playerName = sharedPreferences.getString(PLAYER_NAME_SETTINGS, "Kazan");
            secondPlayerName = sharedPreferences.getString(PLAYER_NAME_SETTINGS, "Andrey");
            TextView playerN = (TextView)findViewById(R.id.playerName);
            playerN.setText(playerName);
        }

        ConstraintLayout bgLay = findViewById(R.id.bg_mult_l);
        final AnimatedBackground animatedBackground = new AnimatedBackground();
        animatedBackground.animate(bgLay, this);

        Button fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MultiplayerActivity.this, MenuActivity.class));
                animatedBackground.stop();
                MultiplayerActivity.this.finish();
            }
        });
        Button fab2 = findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MultiplayerActivity.this, RoomNameEnterActivity.class), 1 );
            }
        });
        Button fab3 = findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MultiplayerActivity.this,NameEnterActivity.class), 1);
            }
        });
        displayRooms();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null) return;
        final String roomName = data.getStringExtra("roomName");
        String name = data.getStringExtra("firstPlayerName");
        if(roomName != null){
            CheckForExceptions check = new CheckForExceptions();
            if(!check.sameRoomExists(roomName)){
                if(!roomName.equals("")) {
                    FirebaseDatabase
                            .getInstance()
                            .getReference("rooms")
                            .child(roomName)
                            .setValue(new RoomParams(playerName, NUMBER_OF_SQUARES, 0, roomName));
                    Intent intent = new Intent(MultiplayerActivity.this, TicTacToeActivity.class);
                    intent.putExtra("roomName", roomName);
                    intent.putExtra("playerName", playerName);
                    intent.putExtra("numberOfPlayers", -1);
                    startActivity(intent);
                    finish();
                }else {
                    cancelled("Name couldn't be empty.");
                }
            } else {
                cancelled("That game already exists.");
            }
        }
        if(name != null){
            if(!name.equals("")) {
                playerName = name;
                secondPlayerName = name;
                TextView playerN = (TextView) findViewById(R.id.playerName);
                playerN.setText(playerName);
                SharedPreferences.Editor editor = getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE).edit();
                editor.putString(PLAYER_NAME_SETTINGS, playerName);
                editor.apply();
            }else {
                cancelled("Name couldn't be empty.");
            }
        }
    }


    class CheckForExceptions {

        boolean isExists = false;
        boolean isHaveAnotherPlayer = false;

        private void setExists(boolean set) {
            isExists = set;
        }

        private void setHAP(boolean set) {
            isHaveAnotherPlayer = set;
        }

        protected boolean sameRoomExists(String roomName) {
            FirebaseDatabase
                    .getInstance()
                    .getReference("rooms")
                    .child(roomName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            setExists(dataSnapshot.exists());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
            return isExists;
        }

        protected void isHAP(String roomName, final View v, final RoomParams model) {
            final CheckForExceptions check = this;
            FirebaseDatabase
                    .getInstance()
                    .getReference("games")
                    .child(roomName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            GameParams gameParams = (GameParams)dataSnapshot.getValue(GameParams.class);
                            if(gameParams != null) setHAP(!gameParams.getSecondPlayerName().equals(""));
                            connectToRoom(v, model, check);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

    }



    private void cancelled(String text){
        Toast.makeText(this , text, Toast.LENGTH_LONG).show();
    }


    private void connectToRoom(View v, final RoomParams model, CheckForExceptions check){
        if(!check.isHaveAnotherPlayer) {
            if(!playerName.equals("") && !secondPlayerName.equals("")){
            Snackbar.make(v, "Connecting...", Snackbar.LENGTH_LONG).show();
            Intent intent = new Intent(MultiplayerActivity.this, TicTacToeActivity.class);
            intent.putExtra("clientNumber", 1);
            intent.putExtra("playerName", model.getPlayerName());
            intent.putExtra("secondPlayerName", secondPlayerName);
            intent.putExtra("roomName", model.getRoomName());
            intent.putExtra("sizeOfField", model.getSizeOfField());
            intent.putExtra("numberOfPlayers", -1);
            startActivity(intent);
            finish();
            } else {
                cancelled("Name couldn't be empty.");
            }
        } else {
            cancelled("This room is full.");
        }
    }

    private void displayRooms(){
        ListView linearLayout = (ListView)findViewById(R.id.LinLay);
        FirebaseListAdapter<RoomParams> adapter = new FirebaseListAdapter<RoomParams>(
                this, RoomParams.class, R.layout.room, FirebaseDatabase.getInstance().getReference("rooms")) {
            @Override
            protected void populateView(View v, final RoomParams model, int position) {
                final TextView roomName = v.findViewById(R.id.nameOfRoom);
                TextView playerName = v.findViewById(R.id.playerName);
                TextView sizeOfField = v.findViewById(R.id.sizeOfField);
                TextView numberOfWins = v.findViewById(R.id.numberOfWins);

                String sizeOfFieldStr = model.getSizeOfField() + "x" + model.getSizeOfField();

                roomName.setText(model.getRoomName());
                playerName.setText(model.getPlayerName());
                sizeOfField.setText(sizeOfFieldStr);
                numberOfWins.setText(String.valueOf(model.getNumberOfWins()));

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckForExceptions check = new CheckForExceptions();
                        check.isHAP(roomName.getText().toString(), v, model);
                    }
                });
            }
        };
        linearLayout.setAdapter(adapter);
    }


}
