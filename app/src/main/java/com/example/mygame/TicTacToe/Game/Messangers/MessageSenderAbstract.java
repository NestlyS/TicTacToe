package com.example.mygame.TicTacToe.Game.Messangers;

import com.example.mygame.TicTacToe.multi.GameParams;
import com.google.firebase.database.FirebaseDatabase;

abstract public class MessageSenderAbstract{

    protected String roomName;

    MessageSenderAbstract(String roomName){
        this.roomName = roomName;
    }

    public abstract void sendMessage(GameParams gameParams, String message);

    public abstract void sendCoordinates(GameParams gameParams, int x, int y);

    public abstract void sendFirstPlayer(GameParams gameParams, String fpName);

    public abstract  void sendSecondPlayer(GameParams gameParams, String spName);

    protected void setValue(GameParams gameParams){
        FirebaseDatabase
                .getInstance()
                .getReference("games")
                .child(roomName)
                .setValue(gameParams);
    }
}