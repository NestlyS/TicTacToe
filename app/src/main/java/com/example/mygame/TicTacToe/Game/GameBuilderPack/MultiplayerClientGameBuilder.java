package com.example.mygame.TicTacToe.Game.GameBuilderPack;

import android.os.Bundle;
import android.widget.TextView;

import com.example.mygame.R;
import com.example.mygame.TicTacToe.Game.BasicGame;
import com.example.mygame.TicTacToe.Game.MultiplayerGameClient;
import com.example.mygame.TicTacToe.InterfaceManager;

public class MultiplayerClientGameBuilder extends MultiplayerGameBuilder {

    public MultiplayerClientGameBuilder(Bundle bundle) {
        super(bundle);
    }

    @Override
    public BasicGame build() {
        super.build();

        TextView secondPlayer = (TextView) InterfaceManager
                .getInstance()
                .getView(R.id.second_player_name);
        secondPlayer.setText(bundle.getString("secondPlayerName", "Lena"));

        gameParams.setSecondPlayerName(secondPlayer.getText().toString());

        HEIGHT_OF_THE_GAME = bundle.getInt("sizeOfField");
        WIDTH_OF_THE_GAME = HEIGHT_OF_THE_GAME;

        MultiplayerGameClient game = new MultiplayerGameClient(HEIGHT_OF_THE_GAME, WIDTH_OF_THE_GAME, bundle.getInt("sizeOfField") , gameParams, bundle.getString("roomName", "1"));
        return game;

    }
}
