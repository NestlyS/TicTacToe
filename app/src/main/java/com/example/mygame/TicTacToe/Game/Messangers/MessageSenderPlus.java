package com.example.mygame.TicTacToe.Game.Messangers;

import androidx.annotation.NonNull;

import com.example.mygame.TicTacToe.multi.RoomParams;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MessageSenderPlus{
    static private RoomParams roomParams;
    final static private MessageSenderPlus thisInstance = new MessageSenderPlus();

    private MessageSenderPlus(){
    }

    public static MessageSenderPlus getInstance(final String roomName, final int score){
        FirebaseDatabase
                .getInstance()
                .getReference("rooms")
                .child(roomName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        RoomParams roomParam = (RoomParams)dataSnapshot.getValue(RoomParams.class);
                        if(roomParam != null) roomParams = roomParam;
                        else {
                            roomParams = new RoomParams();
                            roomParams.setRoomName(roomName);
                        }
                        incWins(score);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        return thisInstance;
    }

    private static void incWins(int score){
        int wins = score;
        roomParams.setNumberOfWins(wins);
        FirebaseDatabase
                .getInstance()
                .getReference("rooms")
                .child(roomParams.getRoomName())
                .setValue(roomParams);
    }

}
