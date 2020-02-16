package com.example.mygame.TicTacToe.Game.GameBuilderPack;

import android.os.Bundle;
import android.widget.ImageButton;

import com.example.mygame.TicTacToe.Game.BasicGame;
import com.example.mygame.TicTacToe.Game.MultiplayerGame;

public class MultiplayerServerGameBuilder extends MultiplayerGameBuilder {
    public MultiplayerServerGameBuilder(Bundle bundle) {
        super(bundle);
    }

    @Override
    public BasicGame build() {
        super.build();
        MultiplayerGame game = new MultiplayerGame(HEIGHT_OF_THE_GAME, WIDTH_OF_THE_GAME, NUMBER_SYM_TO_WIN, gameParams);
        game.start(bundle.getString("roomName", "1"), buttons);
        return game;
    }
}
