package com.example.mygame.TicTacToe.Game;

import com.example.mygame.TicTacToe.Player;

public class Square {
    private Player player;
    Square(){
        player = new Player("");
    }
    public void fill(Player player){
        this.player = player;
    }

    public boolean isFilled(){
        return !player.getSymbol().equals("");
    }
    public Player getPlayer(){
        if(player != null && !player.getSymbol().equals(""))
            return player;
        else
            return new Player("");
    }
}
