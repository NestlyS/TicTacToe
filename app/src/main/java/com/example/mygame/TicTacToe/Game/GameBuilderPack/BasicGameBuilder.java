package com.example.mygame.TicTacToe.Game.GameBuilderPack;

import android.widget.ImageButton;

import com.example.mygame.Bot.Bot;
import com.example.mygame.TicTacToe.Game.BasicGame;

public abstract class BasicGameBuilder {
    protected int HEIGHT_OF_THE_GAME = 3;
    protected int WIDTH_OF_THE_GAME = 3;
    protected int NUMBER_SYM_TO_WIN = 3;
    protected int NUMBER_OF_PLAYERS = 2;
    protected int DIFFICULTY = 1;
    protected Bot bot;

    protected ImageButton[][] buttons;


    public abstract BasicGame build();


    public void setHeight(int height) {
        HEIGHT_OF_THE_GAME = height;
    }


    public void setWidth(int width) {
        WIDTH_OF_THE_GAME = width;
    }


    public void setNumberSymToWin(int symToWin) {
        NUMBER_SYM_TO_WIN = symToWin;
    }

    public void setNumberOfPlayers(int numOfPl){
        NUMBER_OF_PLAYERS = numOfPl;
    }

    public void setButtons(ImageButton[][] buttons){
        this.buttons = buttons;
    }

    public void setDiff(int diff){
        DIFFICULTY = diff;
    }

    public void setBot(){
    }
}
