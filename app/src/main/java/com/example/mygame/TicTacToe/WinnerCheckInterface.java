package com.example.mygame.TicTacToe;

import androidx.core.util.Pair;

import java.util.Vector;


public interface WinnerCheckInterface {
    public Pair<Player, Vector<Pair<Integer, Integer>>> checkWinner();
}
