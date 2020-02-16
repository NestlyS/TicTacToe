package com.example.mygame.TicTacToe.Game;

import android.widget.TextView;

import androidx.core.util.Pair;

import com.example.mygame.Bot.Bot;
import com.example.mygame.R;
import com.example.mygame.TicTacToe.InterfaceManager;
import com.example.mygame.TicTacToe.Player;


public class Game extends BasicGame{


    private int numberOfPlayers;

    private Bot bot = null;

    @Override
    public Bot getBot() {
        return bot;
    }

    public Game(int height, int weight, int numberOfPlayers, int numberOfSymbolsToWin/*, TicTacToeActivity parent*/){
        //super(height, weight, numberOfSymbolsToWin, parent, numberOfPlayers);
        super(height, weight, numberOfSymbolsToWin, numberOfPlayers);
        this.numberOfPlayers = numberOfPlayers;

        if(numberOfPlayers == 1) {
            players = new Player[numberOfPlayers + 1];
            scores = new int[numberOfPlayers + 1];
        }
    }


    @Override
    public void setBot(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void start(String[] playerNames){
        super.start(playerNames);
        resetPlayers(playerNames);
    }




    @Override
    public void reset() {
        super.reset();
        start(new String[]{players[0].getSymbol(), players[1].getSymbol()});
    }

    protected void resetPlayers(String[] playerNames){
        super.resetPlayers(playerNames);
        TextView textView = (TextView) InterfaceManager.getInstance().getActivity().findViewById(R.id.turn_t);
        textView.setText(activePlayer.getSymbol());
    }



    public void makeTurnAsBot(){
        if (bot != null && players[numberOfActivePlayer].getSymbol().equals("O") && isGameStarted) {
            bot.makeTurnAsBot();
 //           switchPlayers();
        }
    }
    @Override
    public boolean makeTurn(int x, int y) {
        boolean result = super.makeTurn(x,y);
        if(!result) return result;
        field[x][y].fill(activePlayer);
        switchPlayers();
        return true;
    }



    void switchPlayers(){
        super.switchPlayers();

    }

    public Player getCurrentActivePlayer(){
        return activePlayer;
    }

    public int getNumberOfPlayers(){
        return numberOfPlayers;
    }

    @Override
    public boolean isTurnOfFirstPlayer() {
        return players[numberOfActivePlayer].getSymbol().equals("X");
    }
}
