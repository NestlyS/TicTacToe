package com.example.mygame.TicTacToe.Game.Messangers;

import androidx.annotation.NonNull;

import com.example.mygame.TicTacToe.Game.MultiplayerGame;
import com.example.mygame.TicTacToe.multi.GameParams;
import com.example.mygame.TicTacToe.multi.RoomParams;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MessageSender extends MessageSenderAbstract {

    public MessageSender(String roomName){
        super(roomName);
    }

    @Override
    public void sendMessage(GameParams gameParams, String message) {
        gameParams.setMessage(message);
        setValue(gameParams);
    }

    @Override
    public void sendCoordinates(GameParams gameParams, int x, int y) {
        gameParams.setX(x);
        gameParams.setY(y);
        setValue(gameParams);
    }

    @Override
    public void sendFirstPlayer(GameParams gameParams, String fpName) {
        gameParams.setFirstPlayerName(fpName);
        setValue(gameParams);
    }

    @Override
    public void sendSecondPlayer(GameParams gameParams, String spName) {
        gameParams.setSecondPlayerName(spName);
        setValue(gameParams);
    }

}

