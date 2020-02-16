package com.example.mygame.TicTacToe;

import androidx.core.util.Pair;

import com.example.mygame.TicTacToe.Game.BasicGame;

import java.util.Vector;

public class WinnerCheckVertical implements WinnerCheckInterface {
    BasicGame game;

    public WinnerCheckVertical(BasicGame game){
        this.game = game;
    }

    @Override
    public Pair<Player, Vector<Pair<Integer, Integer>>> checkWinner(){
        for(int i = 0; i < game.getField().length; i++){
            int numberOfFieldsInColumn = game.getField().length;
            Player lastPlayer = game.getField()[0][i].getPlayer();
            int successCounter = 1;
            Vector<Pair<Integer, Integer>> vector = new Vector<>();
            for(int j = 1; j < numberOfFieldsInColumn; j++){
                if(game.getField()[j][i].getPlayer() != null){
                    if(game.getField()[j][i].getPlayer() == lastPlayer){
                        successCounter++;
                        vector.add(new Pair<Integer, Integer>(j-1, i));
                    } else{
                        successCounter = 1;
                        vector.removeAllElements();
                    }
                    if(successCounter >= game.getNumberOfSymbolsToWin()){
                        vector.add(new Pair<Integer, Integer>(j, i));
                        return new Pair<Player, Vector<Pair<Integer, Integer>>>
                                (game.getField()[j][i].getPlayer(), vector);
                    }
                }
                lastPlayer = game.getField()[j][i].getPlayer();
            }
        }
        return null;
    }

}
