package com.example.mygame.TicTacToe;

import androidx.core.util.Pair;

import com.example.mygame.TicTacToe.Game.BasicGame;

import java.util.Vector;

public class WinnerCheckHorizontal implements WinnerCheckInterface {
    private BasicGame game;

    public WinnerCheckHorizontal(BasicGame game){
        this.game = game;
    }

    @Override
    public Pair<Player, Vector<Pair<Integer, Integer>>> checkWinner() {
        for(int i = 0; i < game.getField().length; i++){
            int numberOfFieldsInRow = game.getField()[i].length;
            Player lastPlayer = game.getField()[i][0].getPlayer();
            int successCounter = 1;
            Vector<Pair<Integer, Integer>> vector = new Vector<>();
            for(int j = 1; j < numberOfFieldsInRow; j++){
                if(game.getField()[i][j].getPlayer() != null){
                    if(game.getField()[i][j].getPlayer() == lastPlayer){
                        successCounter++;
                        vector.add(new Pair<Integer, Integer>(i, j-1));
                    } else{
                        successCounter = 1;
                        vector.removeAllElements();
                    }
                    if(successCounter >= game.getNumberOfSymbolsToWin()){
                        vector.add(new Pair<Integer, Integer>(i, j));
                        return new Pair<Player, Vector<Pair<Integer, Integer>>>
                                (game.getField()[i][j].getPlayer(), vector);
                    }
                }
                lastPlayer = game.getField()[i][j].getPlayer();
            }
        }
        return null;
    }
}
