package com.example.mygame.TicTacToe;

import androidx.core.util.Pair;

import java.util.Vector;

public interface TicTacToeInterface {
    void gameOver(Pair<Player, Vector<Pair<Integer, Integer>>> bundle);

    void gameOver();

    void disconnected(String name);

    void isDisconnected();

    void refresh();
}
