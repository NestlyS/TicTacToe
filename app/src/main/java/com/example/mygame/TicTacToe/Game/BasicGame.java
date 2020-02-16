package com.example.mygame.TicTacToe.Game;

import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.example.mygame.Bot.Bot;
import com.example.mygame.R;
import com.example.mygame.TicTacToe.Game.Messangers.Subscriber;
import com.example.mygame.TicTacToe.InterfaceManager;
import com.example.mygame.TicTacToe.Player;
import com.example.mygame.TicTacToe.WinnerCheckDiagonal;
import com.example.mygame.TicTacToe.WinnerCheckHorizontal;
import com.example.mygame.TicTacToe.WinnerCheckInterface;
import com.example.mygame.TicTacToe.WinnerCheckVertical;
import com.example.mygame.TicTacToe.multi.GameParams;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Vector;

public abstract class BasicGame {


    Player activePlayer;

    Player[] players;

    int[] scores;

    Square[][] field;

    boolean isGameStarted, isTurnMaking;

    int filledSquaresCount;

    int allSquaresCount;

    int numberOfSymbolsToWin;

    int numberOfPlayers;

    int numberOfFirstPlayer;

    int numberOfActivePlayer;

    final int NUMBER_OF_WINNER_CHECKERS = 3;

    ImageButton[][] buttons;
    //TicTacToeActivity parent;

    WinnerCheckInterface[] winnerCheckers;

    BasicGame(int height, int width, int numberOfSymbolsToWin/*, TicTacToeActivity parent*/, int numberOfPlayers){
        field = new Square[height][width];
        int squareCount = 0;

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                field[i][j] = new Square();
                squareCount++;
            }
        }
        allSquaresCount = squareCount;
        numberOfFirstPlayer = 0;

        isGameStarted = false;
        isTurnMaking = false;
        filledSquaresCount = 0;
        this.numberOfSymbolsToWin = numberOfSymbolsToWin;
        this.numberOfPlayers = numberOfPlayers;

        players = new Player[numberOfPlayers];
        scores = new int[numberOfPlayers];
        activePlayer = null;
        numberOfActivePlayer = -1;

        //this.parent = parent;
        winnerCheckers = new WinnerCheckInterface[NUMBER_OF_WINNER_CHECKERS];
        winnerCheckers[0] = new WinnerCheckDiagonal(this);
        winnerCheckers[1] = new WinnerCheckHorizontal(this);
        winnerCheckers[2] = new WinnerCheckVertical(this);
    }


    public Square[][] getField(){
        return field;
    }

    public int getNumberOfSymbolsToWin() {
        return numberOfSymbolsToWin;
    }


    public Pair<Player, Vector<Pair<Integer, Integer>>> checkWinners(){
        for(WinnerCheckInterface winnerCheck : winnerCheckers){
            Pair<Player, Vector<Pair<Integer, Integer>>> winner = winnerCheck.checkWinner();
            if(winner != null){
                return winner;
            }
        }
        return null;
    }

    void switchPlayers() {
        if (numberOfActivePlayer != players.length - 1) {
            numberOfActivePlayer++;
        } else {
            numberOfActivePlayer = 0;
        }
        activePlayer = players[numberOfActivePlayer];
        TextView textView = (TextView) InterfaceManager.getInstance().getActivity().findViewById(R.id.turn_t);
        textView.setText(activePlayer.getSymbol());
        //setCurrentActivePlayer(players[numberOfFirstPlayer], numberOfFirstPlayer);
    }


    void setCurrentActivePlayer(Player player, int numberOfActivePlayer){
        activePlayer = player;
        this.numberOfActivePlayer = numberOfActivePlayer;
    }

    public void start(@Nullable String[] playerName){
        isGameStarted = true;
    }

    protected void resetPlayers(String[] playerNames){
        for(int i = 0; i < players.length; i++)
            players[i] = new Player(playerNames[i]);
        setCurrentActivePlayer(players[numberOfFirstPlayer], numberOfFirstPlayer);
    }

    public boolean isGameStarted(){
        return isGameStarted;
    }

    public boolean getIsTurnMaking() {return isTurnMaking;}

    public void setIsTurnMaking(boolean right) {isTurnMaking = right;}

    public int getFilledSquaresCount() {
        return filledSquaresCount;
    }

    public void setFilledSquaresCount(int filledSquaresCount) {
        this.filledSquaresCount = filledSquaresCount;
    }

    public boolean isFieldFilled(){
        for (int i = 0; i < field.length; i++){
            for (int j = 0; j < field[i].length; j++){
                if(field[i][j].getPlayer().getSymbol().equals("")){
                    return false;
                }
            }
        }
        return true;
    }

    void resetField(){
        for(Square[] row: field){
            for(Square square: row){
                square.fill(new Player(""));
            }
        }
        filledSquaresCount = 0;
    }

    public int getScoreOfXPlayer(int numberOfXPlayer){
        if(numberOfXPlayer < scores.length)
            return scores[numberOfXPlayer];
        return -1;
    }

    public void reset(){
        isGameStarted = true;
        if(numberOfFirstPlayer < players.length-1) numberOfFirstPlayer++;
        else numberOfFirstPlayer = 0;
        resetField();
    }



    public void endGame(){
        isGameStarted = false;
    }


    public void playerXWon(String nameOfWinner){
        int numberOfXPlayer = scores.length+1;
        for(int i = 0; i < players.length; i++)
            if (players[i].getSymbol().equals(nameOfWinner)) {
                numberOfXPlayer = i;
                break;
            }
        if(numberOfXPlayer < scores.length)
            scores[numberOfXPlayer]++;
    }

    public boolean makeTurn(int x, int y){
        if(isGameStarted || !isTurnMaking) {
            if (field[x][y].isFilled()) {
                return false;
            }
            filledSquaresCount++;
            return true;
        }
        return false;
    }


    public void setField(int y, int x, int playerNumber){
        field[y][x].fill(players[playerNumber]);
    }

    public void onExit(){
    }

    public void setBot(Bot bot){
    }

    public void setButtons(ImageButton[][] buttons){
        this.buttons = buttons;
    }

    public boolean isTurnOfFirstPlayer(){
        return true;
    }

    public Bot getBot() {return null;}

    public Player[] getPlayers(){
        return players;
    }

    public GameParams getGameParams(){return null;}




}
