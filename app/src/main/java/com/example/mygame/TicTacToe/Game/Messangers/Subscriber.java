package com.example.mygame.TicTacToe.Game.Messangers;

import com.example.mygame.TicTacToe.multi.GameParams;

public interface Subscriber {
    public void update(GameParams gameParams);


    public void disconnected();
}
