package com.example.mygame.Bot;


import android.widget.ImageButton;

import com.example.mygame.TicTacToe.Game.Game;

public interface Bot {
    public boolean makeTurnAsBot( );

    public void setGame(Game game);

    public void setButtons(ImageButton[][] buttons);
}
