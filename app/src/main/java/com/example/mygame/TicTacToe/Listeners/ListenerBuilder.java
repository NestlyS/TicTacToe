package com.example.mygame.TicTacToe.Listeners;


import com.example.mygame.TicTacToe.Game.BasicGame;
import com.example.mygame.TicTacToe.Game.MultiplayerGameClient;

public class ListenerBuilder {
    public static ListenerInterface build(int x, int y, BasicGame game){
        if(game instanceof MultiplayerGameClient){
            return new ClientListener(x, y, game);
        } else {
            return new Listener(x , y , game);
        }

    }
}
