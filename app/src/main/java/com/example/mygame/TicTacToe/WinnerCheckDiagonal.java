package com.example.mygame.TicTacToe;

import androidx.core.util.Pair;

import com.example.mygame.TicTacToe.Game.BasicGame;

import java.util.Vector;

public class WinnerCheckDiagonal implements WinnerCheckInterface {
    BasicGame game;

    public WinnerCheckDiagonal(BasicGame game){
        this.game = game;
    }

    @Override
    public Pair<Player, Vector<Pair<Integer, Integer>>> checkWinner() {
        for(int i = 0; i < game.getField().length; i++){
            for(int j = 0; j < game.getField()[i].length; j++){
                if(game.getField()[i][j].getPlayer() != null){
                    int checkNumbers = 1;
                    int checkLeftTop = 0;
                    int checkRightBottom = 0;
                    if(     (i != 0 && j != 0)
                            &&
                            (game.getField()[i][j].getPlayer() == game.getField()[i-1][j-1].getPlayer())
                    ){
                        checkLeftTop = checkLeftTopSquare(i-1, j-1);
                        checkNumbers += checkLeftTop;
                    }
                    if(     (i != game.getField().length-1 && j != game.getField()[i].length-1)
                            &&
                            (game.getField()[i][j].getPlayer() == game.getField()[i+1][j+1].getPlayer())
                    ){
                        checkRightBottom = checkRightBottomSquare(i+1, j+1);
                        checkNumbers += checkRightBottom;
                    }
                    if(checkNumbers >= game.getNumberOfSymbolsToWin()) {
                        Vector<Pair<Integer, Integer>> vector = new Vector<>();
                        for(int k = 1; k <= checkLeftTop; k++)
                            vector.add(new Pair<Integer, Integer>(i-k, j-k));
                        for(int k = 0; k <= checkRightBottom; k++)
                            vector.add(new Pair<Integer, Integer>(i+k, j+k));
                        return new Pair<Player, Vector<Pair<Integer, Integer>>>(
                                game.getField()[i][j].getPlayer(),
                                vector);
                    }
                    checkNumbers = 1;
                    int checkRightTop = 0;
                    int checkLeftBottom = 0;
                    if(     (i != 0 && j != game.getField()[i].length-1)
                            &&
                            (game.getField()[i][j].getPlayer() == game.getField()[i-1][j+1].getPlayer())
                    ){
                        checkRightTop = checkRightTopSquare(i-1, j+1);
                        checkNumbers += checkRightTop;
                    }
                    if(     (i != game.getField().length-1 && j != 0)
                            &&
                            (game.getField()[i][j].getPlayer() == game.getField()[i+1][j-1].getPlayer())
                    ){
                        checkLeftBottom = checkLeftBottomSquare(i+1, j-1);
                        checkNumbers += checkLeftBottom;
                    }
                    if(checkNumbers >= game.getNumberOfSymbolsToWin()) {
                        Vector<Pair<Integer, Integer>> vector = new Vector<>();
                        for(int k = 1; k <= checkRightTop; k++)
                            vector.add(new Pair<Integer, Integer>(i-k, j+k));
                        for(int k = 0; k <= checkLeftBottom; k++)
                            vector.add(new Pair<Integer, Integer>(i+k, j-k));
                        return new Pair<Player, Vector<Pair<Integer, Integer>>>(
                                game.getField()[i][j].getPlayer(),
                                vector);
                    }
                }
            }
        }
        return null;
    }

    /*
    * Проверка, есть ли по диагонали знаки того же игрока, что и вызвавшей его ячейки
    * Если количество ячеек равно тому, сколько нужно для победы, возвращается игрок.
    * Ячейки рекурсивно вызывают сами себя, а возвращают количество ячеек после них.
    * */

    public int checkRightTopSquare(int y, int x){
        int numberOfDiagSquares = 1;
        if(x != game.getField()[y].length-1 && y != 0){
            if(game.getField()[y][x].getPlayer() == game.getField()[y-1][x+1].getPlayer())
                numberOfDiagSquares += checkRightTopSquare(y-1, x+1);
        }
        return numberOfDiagSquares;
    }

    public int checkRightBottomSquare(int y, int x){
        int numberOfDiagSquares = 1;
        if(x != game.getField()[y].length-1 && y != game.getField().length-1 ){
            if(game.getField()[y][x].getPlayer() == game.getField()[y+1][x+1].getPlayer())
                numberOfDiagSquares += checkRightBottomSquare(y+1, x+1);
        }
        return numberOfDiagSquares;
    }

    public int checkLeftBottomSquare(int y, int x){
        int numberOfDiagSquares = 1;
        if(x != 0 && y != game.getField().length-1){
            if(game.getField()[y][x].getPlayer() == game.getField()[y+1][x-1].getPlayer())
                numberOfDiagSquares += checkLeftBottomSquare(y+1, x-1);
        }
        return numberOfDiagSquares;
    }

    public int checkLeftTopSquare(int y, int x){
        int numberOfDiagSquares = 1;
        if(x != 0 && y != 0){
            if(game.getField()[y][x].getPlayer() == game.getField()[y-1][x-1].getPlayer())
                numberOfDiagSquares += checkLeftTopSquare(y-1, x-1);
        }
        return numberOfDiagSquares;
    }
}
