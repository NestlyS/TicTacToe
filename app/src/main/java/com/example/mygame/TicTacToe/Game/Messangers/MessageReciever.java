package com.example.mygame.TicTacToe.Game.Messangers;

import androidx.annotation.NonNull;

import com.example.mygame.TicTacToe.multi.GameParams;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageReciever {

    private GameParams gameParams;

    private static ArrayList<Subscriber> subscribers = new ArrayList<>();

    public MessageReciever(String roomName) {
        FirebaseDatabase
                .getInstance()
                .getReference("games")
                .child(roomName)
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                gameParams = (GameParams) dataSnapshot.getValue(GameParams.class);
                                if (gameParams != null) {
                                    for (Subscriber subscriber : subscribers) {
                                        subscriber.update(gameParams);
                                    }
                                } else {
                                    for (Subscriber subscriber : subscribers) {
                                        subscriber.disconnected();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        }
                );
    }


    public static void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public static void removeSubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }
}
