package com.example.mygame.TicTacToe.Game.GameBuilderPack;

import android.os.Bundle;
import android.widget.ImageButton;

import com.example.mygame.Bot.BotCircle;
import com.example.mygame.TicTacToe.Game.BasicGame;

public class Factory {

    private Factory(){

    }

    public static BasicGame getBasic(Bundle bundle, int height, int width, int numSymWin, int diff, int numOfPlayers){
        BasicGameBuilder bgb;
        if(bundle != null){
            if(!bundle.containsKey("clientNumber")) {
                bgb = new MultiplayerServerGameBuilder(bundle);
            } else{
                bgb = new MultiplayerClientGameBuilder(bundle);
            }
        } else{
            bgb = new GameBuilder();
            bgb.setDiff(diff);
            bgb.setNumberOfPlayers(numOfPlayers);
            bgb.setBot();
        }
        bgb.setHeight(height);
        bgb.setWidth(width);
        bgb.setNumberSymToWin(numSymWin);
        BasicGame game = bgb.build();
        basGamBuild = bgb;
        return game;
    }

    private static BasicGameBuilder basGamBuild;

    public static BasicGame afterBuild(BasicGame game, ImageButton[][] buttons){
        game.setButtons(buttons);
        if(game.getBot() != null) game.getBot().setButtons(buttons);
        return game;
    }
}
